package Model;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Oliver Andersson and Christian Lind
 * Represents a calendar filled with work days
 * @since 2020-09-08
 */
public class OurCalendar {
    private Calendar calendar;
    private final List<WorkDay> workDays;
    private static OurCalendar instance = null;

    /**
     * Initalizes the class with a Java calender as singleton
     */
    public static OurCalendar getInstance() {
        if (instance == null)
            instance = new OurCalendar();
        return instance;
    }

    private OurCalendar() {
        this.calendar = java.util.Calendar.getInstance();
        this.workDays = new ArrayList<>();
        this.generateDates();
    }

    /**
     * Generates Date objects for the calender and places it in a sorted list
     */
    private void generateDates() {
        Date throwAwayDate = new Date();
        int startingMonth = throwAwayDate.getMonth();
        for (int month = 0; month < 12; month++) { //how many months you want to generate in the future
            for (int day = 1; day <= YearMonth.of(throwAwayDate.getYear(), throwAwayDate.getMonth() + 1).lengthOfMonth(); day++) {
                throwAwayDate.setDate(day);
                throwAwayDate.setMonth((startingMonth + month) % 12);
                resetThrowable(throwAwayDate);
                workDays.add(new WorkDay(throwAwayDate.getTime()));
            }
            if (throwAwayDate.getMonth() == Calendar.DECEMBER) {
                throwAwayDate.setYear(throwAwayDate.getYear() + 1);
                throwAwayDate.setMonth(Calendar.JANUARY);
            }
        }
    }


    /**
     * Returns a workday at the specified index, returns modulo if bigger than and abs if lower than 0
     *
     * @param index the index of the workday to return
     * @return A workday at the specified index
     */
    public WorkDay getWorkday(int index) {
        if (index < 0) index = -index;
        index = index % getOurDateSize();
        return workDays.get(index);
    }

    public int getOurDateSize() {
        return workDays.size();
    }

    /**
     * As we only want to get the day, month and year We reset seconds, minutes and hours
     *
     * @param throwAwayDate The Date to reset
     */
    private void resetThrowable(Date throwAwayDate) {
        throwAwayDate.setSeconds(0);
        throwAwayDate.setMinutes(0);
        throwAwayDate.setHours(0);
    }

    /**
     * Gets the work day of the specified date
     *
     * @param date The Java.Date
     * @return the work day of the date
     */
    public WorkDay getDate(Date date) {
        for (WorkDay workday : workDays) {
            Date date2 = new Date(workday.DATE);
            if (date.getDate() == date2.getDate() && date.getMonth() == date2.getMonth() && date.getYear() == date2.getYear()) {
                return workday;
            }
        }
        throw new IllegalArgumentException("date must be between today and 365 days forward");
    }

    /**
     * Returns the index in the WorkDays list of the specified Date object
     *
     * @param date The Java.Date object to get the index from
     * @return The index for the specified Java.Date object in the list over WorkDays
     */
    public int getDateIndex(Date date) {
        for (int i = 0; i < workDays.size(); i++) {
            Date date2 = new Date(workDays.get(i).DATE);
            if (date.getDate() == date2.getDate() && date.getMonth() == date2.getMonth() && date.getYear() == date2.getYear()) {
                return i;
            }
        }
        throw new IllegalArgumentException("date must be between today and 365 days forward");
    }
}
