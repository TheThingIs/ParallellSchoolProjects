package test.java;

import main.java.model.*;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class testEmployeeCertificate {
    @Test
    public void testDeligateCertificate() {
        Admin admin = Admin.getInstance();
        admin.createNewEmployee("moa", "123456789123", "moa@gmail.com", "03185552266");
        CertificateHandler ch = CertificateHandler.getInstance();
        ch.createNewCertificate("Kassa");
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("123456789123"), new Date());
        Employee e = admin.getEmployeeByName("moa");
        Certificate kassa = ch.getCertificate("Kassa");
        EmployeeCertificate ec = e.getEmployeeCertificate(kassa);
        assertEquals(ec.getCertificateName(), "Kassa");
    }
}
