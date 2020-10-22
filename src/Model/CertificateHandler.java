package Model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Christian Lind, Markus Grahn.
 * Used by Admin. Uses Employee, certificate
 * A class that handles the certificates. Has a list with certificates and a hash map that link the employees with certificates
 * @since 2020-09-18
 */
public class CertificateHandler {
    private static CertificateHandler single_instance = null;
    private final List<Certificate> ALLCERTIFICATES;
    private final HashMap<Certificate, List<Employee>> EMPLOYEELINKCERTIFICATE;


    private CertificateHandler() {
        this.ALLCERTIFICATES = new ArrayList<>();
        this.EMPLOYEELINKCERTIFICATE = new HashMap<>();
    }

    /**
     * Gets the instance of the CertificateHandler which handles all certificates
     *
     * @return the single instance of the CertificateHandler
     */
    public static CertificateHandler getInstance() {
        if (single_instance == null)
            single_instance = new CertificateHandler();
        return single_instance;
    }

    /**
     * Links the given employee to a certain certificate in the hash map
     *
     * @param certificate The certificate that the employee will be linked to
     * @param employee    The employee that will be linked to a certificate
     */
    public void linkEmployeeToCertificate(Certificate certificate, Employee employee) {
        employeeLinkCertificate.get(certificate).add(employee);
    }

    /**
     * Unlinks the given employee with a certain certificate in the hash map
     *
     * @param certificate The certificate that the employee is linked to
     * @param employee    The employee that will be unliked to a certificate
     */
    public void unlinkEmployeeToCertificate(Certificate certificate, Employee employee) {
        employeeLinkCertificate.get(certificate).remove(employee);
    }

    /**
     * Gets a list with employees that are linked to a certificate
     *
     * @param certificate A certificate
     * @return A list with employees that are linked to the certificate
     */
    public Iterator<Employee> getEmployeeWithCertificate(Certificate certificate) {
        return employeeLinkCertificate.get(certificate).iterator();
    }

    /**
     * Takes in a certificate to check which employees has it
     *
     * @param certificate The certificate to check which employee has
     * @return How many employees has the certificate
     */
    public int getEmployeeWithCertificateSize(Certificate certificate) {
        return EMPLOYEELINKCERTIFICATE.get(certificate).size();
    }

    /**
     * Checks if a specified employee has a specified certificate
     *
     * @param certificate The certificate to check if the employee has
     * @param employee    Employee to check if has certificate
     * @return true if employee has the certificate or false if otherwise
     */
    public boolean checkEmployeeWithCertificate(Certificate certificate, Employee employee) {
        return EMPLOYEELINKCERTIFICATE.get(certificate).contains(employee);
    }

    /**
     * Gets all the certificates using an Iterator
     *
     * @return the iterator of all the certificates
     */
    public Iterator<Certificate> getAllCertificates() {
        return ALLCERTIFICATES.iterator();
    }

    /**
     * Gets the certificate by its name
     *
     * @param name Name of the certificate
     * @return the certificate that has the given name, or returns null if the name is invalid
     */
    public Certificate getCertificate(String name) {
        for (Certificate c : ALLCERTIFICATES) {
            if (c.NAME.equals(name))
                return c;
        }
        System.out.println("invalid name");
        return null;
    }

    /**
     * Gets the certificate by its ID
     *
     * @param ID Id of the certificate
     * @return the certificate that has the given ID, or returns null if the ID is invalid
     */
    public Certificate getCertificate(long ID) {
        for (Certificate c : ALLCERTIFICATES) {
            if (c.ID == ID)
                return c;
        }
        throw new InvalidParameterException("invalid ID");
    }

    /**
     * Creates a new Certificate and adds it to the list of all certificates and to the hash map
     *
     * @param nameOfCertificate The name of the new certificate
     */
    public void createNewCertificate(String nameOfCertificate) {
        Certificate tmp = new Certificate(nameOfCertificate);
        this.ALLCERTIFICATES.add(tmp);
        EMPLOYEELINKCERTIFICATE.put(tmp, new ArrayList<>());
    }

    /**
     * Deletes a certificate from the hash map (removes all the links to it to the employees) and removes it from the list of all certificates
     *
     * @param certificate The certificate that will be removed
     */
    public void deleteCertificate(Certificate certificate) {
        for (Employee e : EMPLOYEELINKCERTIFICATE.get(certificate)) {
            e.unAssignCertificate(e.getEmployeeCertificate(certificate));
        }
        this.ALLCERTIFICATES.remove(certificate);
    }

    /**
     * Deletes the certificate that has the given name from the list of all certificates
     *
     * @param name The name of the certificate that will be removed
     */
    public void deleteCertificate(String name) {
        for (Certificate c : ALLCERTIFICATES) {
            if (c.NAME.equalsIgnoreCase(name)) {
                deleteCertificate(c);
                break;
            }
        }
    }
}
