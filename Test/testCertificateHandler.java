import Model.Admin;
import Model.CertificateHandler;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testCertificateHandler {

    @Test
    public void testWhoHasCertificate() {
        Admin admin = Admin.getInstance();
        admin.createNewEmployee("moa", "123456789231", "moa@gmail.com", "03185552266");
        admin.createNewEmployee("moa", "213456789123", "moa3@gmail.com", "03185552267");
        admin.createNewEmployee("crilllle", "312123456789", "llllllll@gmail.com", "03185552268");
        CertificateHandler ch = admin.getCertificatehandler();
        ch.createNewCertificate("Kassa");
        ch.createNewCertificate("Frukt");
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("123456789231"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("213456789123"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByID("213456789123"), new Date());
        assertEquals(2, ch.getEmployeeWithCertificateSize(ch.getCertificate("Kassa")));
        assertEquals(1, ch.getEmployeeWithCertificateSize(ch.getCertificate("Frukt")));
        assertTrue(!ch.checkEmployeeWithCertificate(ch.getCertificate("Kassa"), (admin.getEmployeeByID("312123456789"))));
    }


    @Test
    public void testRemoveGlobalCertificate() {
        Admin admin = Admin.getInstance();
        admin.createNewEmployee("moa", "123456789231", "moa@gmail.com", "03185552267");
        admin.createNewEmployee("moa", "123456789232", "moa2@gmail.com", "03185552268");
        admin.createNewEmployee("crilllle", "1234567892315", "chrille@gmail.com", "03185552269");
        CertificateHandler ch = admin.getCertificatehandler();
        ch.createNewCertificate("Kassa");
        ch.createNewCertificate("Frukt");
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("123456789231"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("123456789232"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByID("123456789232"), new Date());
        ch.deleteCertificate("Kassa");
        assertEquals(1, admin.getEmployeeByID("123456789232").getCertificatesSize());
    }


}
