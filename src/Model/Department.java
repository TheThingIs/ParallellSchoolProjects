package Model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a department with a specified name and a list for work shifts where the department can be manned. It also has a minimum value of persons that has to work at the same time
 */
public class Department implements Observable {

    private final List<WorkShift> allShifts;
    private final List<WorkShift> addedShifts;
    private final List<WorkShift> removedShifts;
    private final List<Observer> observers;
    private String name;
    private int minPersonsOnShift;
    private final BreakHandler breakHandler;
    private Color paint = new Color(1, 1, 1, 1);

    /**
     * Constructs a department with a list for the work shifts where the department can be manned and a specified name and a minimum value of persons that has to work at the same time
     *
     * @param name              the name of the department
     * @param minPersonsOnShift minimum value of persons who must work at the same time (not be on break)
     */
    protected Department(String name, int minPersonsOnShift) {
        this.allShifts = new ArrayList<>();
        this.name = name;
        this.minPersonsOnShift = minPersonsOnShift;
        this.breakHandler = BreakHandler.getInstance();
        this.observers = new ArrayList();
        this.addedShifts = new ArrayList();
        this.removedShifts = new ArrayList();
        for (int i = 0; i < OurCalendar.getInstance().getOurDateSize(); i++) {
            addObserver(OurCalendar.getInstance().getWorkday(i));
        }
    }

    public Color getColor() {
        return paint;
    }

    public void setColor(Color c) {
        this.paint = c;
    }

    public String getName() {
        return name;
    }


    /**
     * Creates a break for a work shift
     *
     * @param startForTheWorkShift start time of the work shift
     * @param stopForTheWorkShift  stop time of the work shift
     * @return a calculated break, placed as close to the middle of the work shift as possible, in form of an OccupiedTime
     */
    private OccupiedTime createBreak(long startForTheWorkShift, long stopForTheWorkShift) {
        long breakLength = breakHandler.calculateLengthOfBreak(startForTheWorkShift, stopForTheWorkShift);
        long breakStart = (((stopForTheWorkShift + startForTheWorkShift) / 2) - (breakLength / 2));
        int numberOfBreakTogether = 0;
        int numberOfWorkingPersonel = countPersonelOnDepartment(startForTheWorkShift, stopForTheWorkShift);
        if (minPersonsOnShift == 0) {
            return null;
        } //TODO exception?
        while (true) {
            for (WorkShift s : allShifts) {
                if (s.getBreakTime().inBetween(breakStart, breakStart + breakLength)) {
                    numberOfBreakTogether++;
                }
            }
            if (numberOfWorkingPersonel >= minPersonsOnShift) {
                if ((numberOfWorkingPersonel == 0) || ((numberOfWorkingPersonel - numberOfBreakTogether) >= minPersonsOnShift)) {
                    return new OccupiedTime(breakStart, breakStart + breakLength);
                }
            } else {//mindre schemalagda än vad som krävs
                return new OccupiedTime((breakStart + 1 + breakLength * numberOfWorkingPersonel), ((breakStart + 1 + breakLength * numberOfWorkingPersonel) + breakLength));
            }
            breakStart = breakStart + WeekHandler.plusMinutes(5);
            numberOfBreakTogether = 0;
        }
    }

    private int countPersonelOnDepartment(long startForTheWorkShift, long stopForTheWorkShift) {
        int count = 0;
        for (WorkShift s : allShifts) {
            if (s.END >= startForTheWorkShift && stopForTheWorkShift >= s.START)
                count++;
        }
        return count;
    }

    /**
     * Creates a work shift with a specified time span to the department where chosen certificates are required from the employee
     *
     * @param start        start time of the shift
     * @param stop         end time of the shift
     * @param certificates list of which certificates are required at the shift
     * @param repeat       boolean array for each day of the week if the workshift should be repeated where 0 = sunday, 1 = monday, 6 = saturday and so on
     */
    protected void createShift(long start, long stop, List<Certificate> certificates, boolean[] repeat) {
        WorkShift ws = new WorkShift(start, stop, certificates, createBreak(start, stop), true);
        if (setRepeat(ws, repeat)) {
            WorkShift shift = new WorkShift(start, stop, certificates, createBreak(start, stop), false);
            allShifts.add(shift);
            addedShifts.add(shift);
        }
        notifyObservers();
    }

    /**
     * Creates a work shift with a specified time span to the department where chosen certificates are required from the employee
     *
     * @param start       start time of the shift
     * @param stop        end time of the shift
     * @param certificate A certificate of which certificates are required at the shift
     * @param repeat      boolean array for each day of the week if the workshift should be repeated where 0 = sunday, 1 = monday, 6 = saturday and so on
     */
    protected void createShift(long start, long stop, Certificate certificate, boolean[] repeat) {
        WorkShift ws = new WorkShift(start, stop, certificate, createBreak(start, stop), true);
        if (setRepeat(ws, repeat)) {
            WorkShift shift = new WorkShift(start, stop, certificate, createBreak(start, stop), false);
            allShifts.add(shift);
            addedShifts.add(shift);
        }
        notifyObservers();
    }

    /**
     * @param start  start time of the shift
     * @param stop   end time of the shift
     * @param repeat boolean array for each day of the week if the workshift should be repeated where 0 = sunday, 1 = monday, 6 = saturday and so on
     */
    protected void createShift(long start, long stop, boolean[] repeat) {
        WorkShift ws = new WorkShift(start, stop, createBreak(start, stop), true);
        if (setRepeat(ws, repeat)) {
            WorkShift shift = new WorkShift(start, stop, createBreak(start, stop), false);
            allShifts.add(shift);
            addedShifts.add(shift);
        }
        notifyObservers();
    }

    /**
     * Copies the details of another workshift
     *
     * @param workshift The workshift to copy from
     */
    protected void createShift(WorkShift workshift) {
        allShifts.add(new WorkShift(workshift, 0));
    }

    /**
     * Copies the details of another workshift and places it plusDays forwars
     *
     * @param workshift The workshift to copy from
     * @param plusDays  How many days forward this workshift should be placed
     */
    protected void createShift(WorkShift workshift, int plusDays) {
        WorkShift shift = new WorkShift(workshift, createBreak(workshift.START + WeekHandler.plusDays(plusDays), workshift.END + WeekHandler.plusDays(plusDays)), plusDays);
        allShifts.add(shift);
        addedShifts.add(shift);
    }

    /**
     * Copies the Workshift according to what days it should be repeated on
     *
     * @param workshift The workshift to copy from
     * @param repeat    boolean array for each day of the week if the workshift should be repeated where 0 = sunday, 1 = monday, 6 = saturday and so on
     * @return If this workshift is being repeated
     */
    private boolean setRepeat(WorkShift workshift, boolean[] repeat) {
        boolean single = true;
        int c = new Date(workshift.START).getDay();
        for (int i = 0; i < 7; i++) {
            if (repeat[(i + c) % 7]) {
                createShift(workshift, i);
                single = false;
            }
        }
        return single;
    }

    /**
     * Removes a specified workshift
     *
     * @param workshift The workshift to remove
     */
    protected void removeShift(WorkShift workshift) {
        allShifts.remove(workshift);
        removedShifts.add(workshift);
        notifyObservers();
    }

    /**
     * Get a workshift at the specified index in the list of workshifts
     *
     * @param index The index the wanted workshift is at
     * @return The workshift at the specified index
     */
    public WorkShift getShift(int index) {
        return allShifts.get(index);
    }

    /**
     * Returns how many workshifts exist
     *
     * @return How many workshifts there are
     */
    public int getSizeAllShifts() {
        return allShifts.size();
    }

    /**
     * Checks if all work shift of the department are manned
     *
     * @return true if all work shifts are manned, else false
     */
    public boolean isAllShiftsFilled() {
        for (WorkShift ws : allShifts) {
            if (!ws.isOccupied())
                return false;
        }
        return true;
    }

    public void setMinPersonsOnShift(int minPersonsOnShift) {
        this.minPersonsOnShift = minPersonsOnShift;
    }

    public int getMinPersonsOnShift() {
        return minPersonsOnShift;
    }

    public BreakHandler getBreakHandler() {
        return breakHandler;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets a workshift that needs to be added
     * @param index index of workshift to add
     * @return an workshift to add
     */
    public WorkShift getAddWorkShift(int index) {
        return addedShifts.get(index);
    }

    /**
     * Checks how many workshifts that needs to be added
     * @return how many workshifts that needs to be added
     */
    public int getAddWorkShiftSize() {
        return addedShifts.size();
    }

    /**
     * Gets one workshift to be removed
     * @param index index of workshift
     * @return a workshift to be removed
     */
    public WorkShift getRemoveWorkShift(int index) {
        return removedShifts.get(index);
    }

    /**
     * Checks how many workshifts that should be removed
     * @return How many workshifts that needs to be removed
     */
    public int getRemoveWorkShiftSize() {
        return removedShifts.size();
    }

    /**
     * Adds an observer to the department
     * @param observer a workDay that will observe the department
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer
     * @param observer a workDay that will stop observing the department
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers to update
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
        addedShifts.clear();
        removedShifts.clear();
    }


}
