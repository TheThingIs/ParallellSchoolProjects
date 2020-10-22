package Model;

import java.util.Date;

/** @author Markus Grahn, Oliver Andersson
 * Uses Certificate. Used by Employee, CertificateHandler
 * A certificate that is assigned to a employee. Has an expiry date and a linked certificate
 */
public class EmployeeCertificate {

    private Date expiryDate;
    private final Certificate CERTIFICATE;

    /**
     * Constructs an EmployeeCertificate with an expiry date and a certificate
     *
     * @param certificate Assign a certificate to the new EmployeeCertificate
     * @param expiryDate  The date this certificate should expire
     */
    protected EmployeeCertificate(Certificate certificate, Date expiryDate) {
        this.expiryDate = expiryDate;
        this.CERTIFICATE = certificate;
    }

    /**
     * Constructs an EmployeeCertificate with a certificate
     *
     * @param certificate Assign a certificate to the new EmployeeCertificate
     */
    protected EmployeeCertificate(Certificate certificate) {
        this.CERTIFICATE = certificate;
    }

    /**
     * Gets the linked certificate of the EmployeeCertificate
     *
     * @return the certificate
     */
    public Certificate getCertificate() {
        return CERTIFICATE;
    }

    /**
     * Gets the name of the certificate
     *
     * @return the name of the certificate
     */
    public String getCertificateName() {
        return this.CERTIFICATE.NAME;
    }

    /**
     * Converts the expiryDate to a string and returns it
     *
     * @return the expiry date as a string
     */
    public String getExpiryDateAsString() {
        if (expiryDate == null){
            return "";
        }
        return expiryDate.toString();
    }
}
