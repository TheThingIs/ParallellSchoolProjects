package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a WorkShift in which an employee can work in if the have the specified certificates required
 */
public class WorkShift {
    private List<Certificate> certificates = new ArrayList<>();
    private Employee employee;
    private OccupiedTime occupiedTime;
    private OccupiedTime breakTime;
    private boolean occupied = false;
    final public boolean REPEAT;
    final public long START, END;
    private static long idStart = 0;
    final public long ID;

    /**
     * Creates a new workshift
     *
     * @param start       The starting time for the Workshift
     * @param end         The ending time for the Workshift
     * @param certificate Required Certificates for the Workshift
     */
    protected WorkShift(long start, long end, Certificate certificate, OccupiedTime breakTime, boolean repeat) {
        this.START = start;
        this.END = end;
        this.certificates.add(certificate);
        this.breakTime = breakTime;
        this.REPEAT = repeat;
        this.ID = idStart;
        idStart++;
    }

    /**
     * Creates a new workshift
     *
     * @param start        The starting time for the Workshift
     * @param end          The ending time for the Workshift
     * @param certificates A list of required Certificates
     */
    protected WorkShift(long start, long end, List<Certificate> certificates, OccupiedTime breakTime, boolean repeat) {
        this.START = start;
        this.END = end;
        this.certificates.addAll(certificates);
        this.breakTime = breakTime;
        this.REPEAT = repeat;
        this.ID = idStart;
        idStart++;
    }

    /**
     * Creates a new Workshift
     *
     * @param start The starting time for the Workshift
     * @param end   The ending time for the Workshift
     */
    protected WorkShift(long start, long end, OccupiedTime breakTime, boolean repeat) {
        this.START = start;
        this.END = end;
        this.breakTime = breakTime;
        this.REPEAT = repeat;
        this.ID = idStart;
        idStart++;
    }

    /**
     * Creates a copy of a previous Workshift without employee and occupation and moves it forward a week
     *
     * @param workShift The Workshift you wish to copy and place on another day
     * @param date      The date in a long format for Java.Date
     */
    protected WorkShift(WorkShift workShift, long date) {
        Date wsStart = new Date(workShift.START);
        Date wsEnd = new Date(workShift.END);
        this.START = date + WeekHandler.plusHours(wsStart.getHours()) + WeekHandler.plusMinutes(wsStart.getMinutes());
        long tempEnd = date + WeekHandler.plusHours(wsEnd.getHours()) + WeekHandler.plusMinutes(wsEnd.getMinutes());
        this.END = setEnd(tempEnd);
        this.certificates = workShift.certificates;
        this.REPEAT = workShift.REPEAT;
        this.breakTime = workShift.breakTime;
        this.ID = workShift.ID;
    }

    private long setEnd(long End) {
        if (End < this.START) {
            return End + WeekHandler.plusDays(1);
        } else {
            return End;
        }
    }

    /**
     * Creates a copy of a previous Workshift without employee and occupation and moves it forward
     *
     * @param workShift The Workshift you wish to copy and place on another day
     * @param date      The amount of days to add to worshift
     */
    protected WorkShift(WorkShift workShift, OccupiedTime breakTime, int date) {
        if (date < 0) {
            date = -date;
        }
        this.START = workShift.START + WeekHandler.plusDays(date);
        this.END = workShift.END + WeekHandler.plusDays(date);
        this.certificates = workShift.certificates;
        this.REPEAT = workShift.REPEAT;
        this.breakTime = breakTime;
        this.ID = idStart;
        idStart++;
    }

    /**
     * Registers a new Employee to the Workshift
     *
     * @param e  The Employee
     * @param ot The Employees OccupiedTime
     */
    protected void registerOccupation(Employee e, OccupiedTime ot) {
        if (!occupied) {
            //TODO checka att employee har certificate via metod
            this.employee = e;
            this.occupiedTime = ot;
            occupied = true;
        }
    }

    /**
     * Checks if the Workshift is properly occupied
     *
     * @return occupied
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Clears the occupation for the Workshift
     */
    public void clearWorkShiftOccupation() {
        employee.unRegisterOccupation(occupiedTime);
        occupied = false;
    }

    /**
     * Adds a certificate that is required for the workshift
     *
     * @param certificate The certificate to add to be requiredfor the workshift
     */
    public void addCertificate(Certificate certificate) {
        certificates.add(certificate);
    }

    /**
     * Gets a certificate at the specified index that is required for the workshift
     *
     * @param index the index to get the specified wprkshift
     * @return The workshift at the specified index
     */
    public Certificate getCertificate(int index) {
        return certificates.get(index);
    }

    /**
     * Returns the amount of certificates required for the workshift
     *
     * @return the amount of certificates required for the workshift
     */
    public int getCertificatesSize() {
        return certificates.size();
    }

    public OccupiedTime getBreakTime() {
        return breakTime;
    }

    /**
     * Removes a specified certificate from being required by the employees
     *
     * @param c the certificate that shall be removed
     */
    public void removeCertificate(Certificate c) {
        certificates.remove(c);
    }

    /**
     * Get the Employee that is occupied to the workshift
     *
     * @return The employee that occupies this workshift
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Get the time this workshift is in between
     *
     * @return The OccupiedTime this workshift has
     */
    public OccupiedTime getOccupation() {
        return occupiedTime;
    }
}
