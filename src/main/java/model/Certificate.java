package main.java.model;

/**
 * @autor Oliver Andersson, Christian Lind
 * Used by EmployeeCertificate, WorkShift
 * Represents a certificate with a specified name and a ID value
 */
public class Certificate {
    public final String NAME;
    private static long idStart = 0;
    public final long ID;


    /**
     * Constructs a certificate with a name and assigns a unique ID value to it
     *
     * @param name the name of the certificate
     */

    protected Certificate(String name) {
        this.NAME = name;
        this.ID = idStart++;
    }

    /**
     * Returns the name of the certificate
     *
     * @return the name of the certificate
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns the unique ID of yhe certificate
     *
     * @return the ID of the certificate
     */
    public long getID() {
        return ID;
    }
}
