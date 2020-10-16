package Model;

import java.util.*;

/**
 * Represents a work day with a specified date, a hash map(with departments, work shifts and employees),and a list of departments
 */
public class WorkDay implements Observer{
    public final long DATE;
    private static List<Department> departments = new ArrayList<>();
    private HashMap<Department, List<WorkShift>> departmentLinks;

    /**
     * Constructs a work day with a specified date and with hash map
     *
     * @param date Date of the work day
     */
    protected WorkDay(long date) {
        this.DATE = date;
        this.departmentLinks = new HashMap<>();
        for (Department d : departments){
            d.addObserver(this);
        }
    }

    public int getDepartmentSize(){
        return departments.size();
    }

    public Department getDepartment( int index){
        return departments.get(index);
    }



    /*
     * Checks if all departments are filled
     * @return true if all the departments are filled and otherwise false
     */
    /*
    boolean allDepartmentsFilled() {
        for (Department d : departments) {
            for (WorkShift w : d.getAllShifts()) {
                if (w.requiredPersonnel > departmentListHashMap.get(d).get(w).size())
                    return false;
            }
        }
        return true;
    }*/

    /**
     * Gets the employees that are working in a specified department
     *
     * @param department A department
     * @return the list of employees that are working in the department
     */
    List<Employee> getWorkingPersonnel(Department department) {
        return null;
    }

    /**
     * Gets all the departments
     *
     * @return a list of all the departments
     */
    List<Department> getAllDepartments() {
        return null;
    }

    /*
     * A method that schedules an employee on a work shift in a department
     * @param employee The employee that will be scheduled
     * @param department The department where the employee will be scheduled
     * @param workShift The work shift the employee will be scheduled on
     */
    /*
    public void scheduleEmployee(Employee employee, Department department, WorkShift workShift) {
        departmentListHashMap.computeIfAbsent(department, k -> new HashMap<>());
        departmentListHashMap.get(department).computeIfAbsent(workShift, k -> new ArrayList<>());
        departmentListHashMap.get(department).get(workShift).add(employee);
        employee.occupiedTimes.add(workShift);
    }*/

    /*
     * A method that schedules employees on a work shift in a department
     * @param employees The list of employees that will be scheduled
     * @param department The department where the employees will be scheduled
     * @param workShift The work shift the employees will be scheduled on
     */
    /*
    public void scheduleEmployees(List<Employee> employees, Department department, WorkShift workShift) {
        departmentListHashMap.computeIfAbsent(department, k -> new HashMap<>());
        departmentListHashMap.get(department).computeIfAbsent(workShift, k -> new ArrayList<>());
        departmentListHashMap.get(department).get(workShift).addAll(employees);
        for (Employee employee : employees)
            employee.occupiedTimes.add(workShift);
    }*/

    private void ScheduleEmployees(Collection<? extends Employee> employees, Department department) {
    }

    /**
     * Registers an Employee for a Workshift and ensures they get their free time
     *
     * @param workShift A WorkShift
     * @param e         An Employee
     */
    public void occupiesEmployee(WorkShift workShift, Employee e) {
        ArrayList<Certificate> certificates = new ArrayList<>();
        for (int i = 0; i < workShift.getCertificatesSize(); i++) {
            certificates.add(workShift.getCertificate(i));
        }
        if (!e.isOccupied(workShift.START, workShift.END) && e.hasCertifices(certificates)) {
            long endOccupiedTime = (workShift.END) + Admin.getInstance().getGuaranteedFreeTime();
            OccupiedTime ot = new OccupiedTime(workShift.START, endOccupiedTime);
            e.registerOccupation(ot);
            workShift.registerOccupation(e, ot);
        } else {
            //TODO
        }
    }

    /**
     * Swaps out an Employee for another one on that WorkShift
     *
     * @param workShift a WorkShift
     * @param e         an Employee
     */
    public void reOccupieEmployee(WorkShift workShift, Employee e) {
        ArrayList<Certificate> certificates = new ArrayList<>();
        for (int i = 0; i < workShift.getCertificatesSize(); i++) {
            certificates.add(workShift.getCertificate(i));
        }
        if (!e.isOccupied(workShift.START, workShift.END) && e.hasCertifices(certificates)) {
            workShift.clearWorkShiftOccupation();
            long endOccupiedTime = (workShift.END) + Admin.getInstance().getGuaranteedFreeTime();
            OccupiedTime ot = new OccupiedTime(workShift.START, endOccupiedTime);
            e.registerOccupation(ot);
            workShift.registerOccupation(e, ot);
        } else {
            //TODO
        }
    }

    public void swapOccupation(WorkShift ws1, WorkShift ws2) {
        ArrayList<Certificate> certificates = new ArrayList<>();
        for (int i = 0; i < ws1.getCertificatesSize(); i++) {
            certificates.add(ws1.getCertificate(i));
        }
        ArrayList<Certificate> certificates2 = new ArrayList<>();
        for (int i = 0; i < ws2.getCertificatesSize(); i++) {
            certificates2.add(ws2.getCertificate(i));
        }
        if (ws1.isOccupied() && ws2.isOccupied() && ws1.getEmployee().hasCertifices(certificates2) && ws2.getEmployee().hasCertifices(certificates)) {
            Employee e1 = ws1.getEmployee();
            Employee e2 = ws2.getEmployee();
            ws1.clearWorkShiftOccupation();
            ws2.clearWorkShiftOccupation();
            if (e1.isOccupied(ws2.START, ws2.END) || e2.isOccupied(ws1.START, ws1.END)) {
                occupiesEmployee(ws1, e1);
                occupiesEmployee(ws2, e2);
            } else {
                occupiesEmployee(ws2, e1);
                occupiesEmployee(ws1, e2);
            }
        }
    }

    /*
    public void setWorkShifts(ArrayList<WorkShift> wss) {
        for (Department d : departments) {
            for (WorkShift ws1 : d.getAllShifts()) {
                for (WorkShift ws2 : wss) {
                    if (ws1 == ws2) {
                        departmentLinks.computeIfAbsent(d, k -> new ArrayList<WorkShift>());
                        departmentLinks.get(d).add(new WorkShift(ws2));
                    }
                }
            }
        }
    }*/

    public List<WorkShift> getWorkShifts(Department d) {
        return departmentLinks.get(d);
    }

    public void setWorkDay() { //funkar inte
        updateDepartments();
        WorkShift ws;
        for (Department d : departments) {
            for (int i = 0; i < d.getSizeAllShifts(); i++) {
                ws = d.getShift(i);
                Date wsDate = new Date(ws.START);
                Date thisDate = new Date(this.DATE);
                if ((ws.REPEAT && (wsDate.getDay() == thisDate.getDay())) || (!ws.REPEAT && (wsDate.getDay() == thisDate.getDay()) && (wsDate.getDate() == thisDate.getDate()))) {
                    this.departmentLinks.get(d).add(new WorkShift(ws, this.DATE));
                }
            }
        }
    }

    public void updateDepartments() {
        for (Department d : departments) {
            departmentLinks.computeIfAbsent(d, k -> new ArrayList<WorkShift>());
        }
    }

    protected static void addDepartment(Department d) {
        departments.add(d);
    }

    protected static void removeDepartment(Department d) {
        departments.remove(d);
    }

    public void unRegisterOccupations(Employee e, long start, long end) {
        for (Department d : departments) {
            if(!(departmentLinks.isEmpty())){
            for (WorkShift ws : departmentLinks.get(d)) {
                if(ws.isOccupied()){
                    if (ws.getOccupation().inBetween(start, end) && ws.getEmployee() == e) {
                    ws.clearWorkShiftOccupation();
                    }
                }
                }
        }}
    }

    public int getDayOfWeekOffset(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(DATE));
        return DayOfWeek.getDay(calendar.get(Calendar.DAY_OF_WEEK)).offset;
    }

    protected void clearDay() {
        departmentLinks = new HashMap<>();
        updateDepartments();
    }

    @Override
    public void update() {
        updateDepartments();
        for (Department d : departments) {
            if (d.getAddWorkShiftSize() > 0) {
                ArrayList<WorkShift> addShifts = new ArrayList<>();
                for (int i = 0 ; i < d.getAddWorkShiftSize() ; i++){
                    addShifts.add(d.getAddWorkShift(i));
                }
                for (WorkShift addWorkShift : addShifts) {
                    Date wsDate = new Date(addWorkShift.START);
                    Date thisDate = new Date(this.DATE);
                    if ((addWorkShift.REPEAT && (wsDate.getDay() == thisDate.getDay())) || (!addWorkShift.REPEAT && (wsDate.getDay() == thisDate.getDay()) && (wsDate.getDate() == thisDate.getDate()))) {
                        this.departmentLinks.get(d).add(new WorkShift(addWorkShift, this.DATE));
                    }
                }
            } if (d.getRemoveWorkShiftSize() > 0){
                ArrayList<WorkShift> removeShifts = new ArrayList<>();
                for (int i = 0 ; i < d.getRemoveWorkShiftSize() ; i++){
                    removeShifts.add(d.getRemoveWorkShift(i));
                }
                for (WorkShift removeShift : removeShifts){
                    for (WorkShift checkShift : departmentLinks.get(d)){
                        if (removeShift.ID == checkShift.ID){
                            departmentLinks.get(d).remove(checkShift);
                        }
                    }
                }
            }
        }
    }
}
