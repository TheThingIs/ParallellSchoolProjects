import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import Model.*;
import org.junit.Test;

import java.util.Date;

public class testAdmin {

    @Test
    public void testCreateNewEmployee() {  //kollar så createNewEmployee lägger till i listan och att man inte kan lägga till om personnummret inte är 12 långt samt om det redan finns
        Admin admin = Admin.getInstance();
        admin.createNewEmployee("moa", "1234789123", "moa@gmail.com");
        assertTrue(admin.getEmployeeListSize() == 0);
        admin.createNewEmployee("moa", "123456789123", "moa1@gmail.com");
        assertTrue(admin.getEmployeeListSize() == 1);
        admin.createNewEmployee("moa", "123456789123", "moa3@gmail.com");
        assertTrue(admin.getEmployeeListSize() == 1);
        admin.createNewEmployee("moa", "123456089123", "moa2@gmail.com");
        assertTrue(admin.getEmployeeListSize() == 2);
    }

    @Test
    public void testDeleteEmployee() {
        Admin admin = Admin.getInstance();
        admin.createNewEmployee("moa", "123456789123", "moa@gmail.com");
        admin.createNewEmployee("markus", "213456789123", "markus@gmail.com");
        admin.removeEmployee(admin.getEmployeeByName("moa"));
        assertTrue(admin.getEmployeeListSize() == 1);
        admin.createNewEmployee("Crille", "312456789123", "ush@gmail.com");
        admin.removeEmployee("312456789123");
        assertTrue(admin.getEmployeeListSize() == 1);
    }


    @Test
    public void testRemoveEmployeeCertificate() {
        Admin admin = Admin.getInstance();
        admin.createNewEmployee("moa", "123456789231", "moa@gmail.com"); //TODO det ska inte finnas dubletter av personnummer samt 10 siffror långt
        admin.createNewEmployee("moa", "123456789235", "moa2@gmail.com");
        admin.createNewEmployee("crilllle", "123456789239", "lll@gmail.com");
        CertificateHandler ch = admin.getCertificatehandler();
        ch.createNewCertificate("Kassa");
        ch.createNewCertificate("Frukt");
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("123456789231"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("123456789235"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByID("123456789235"), new Date());
        assertTrue(ch.getEmployeeWithCertificateSize(ch.getCertificate("Kassa")) == 2);
        admin.removeEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByID("123456789235"));
        assertTrue(ch.getEmployeeWithCertificateSize(ch.getCertificate("Kassa")) == 1);
        assertTrue(ch.getEmployeeWithCertificateSize(ch.getCertificate("Frukt")) == 1);
    }

    @Test
    public void testCreateWorkshift() {
        Admin admin = Admin.getInstance();
        admin.createNewDepartment("Kassa", 3);
        Date date = new Date();
        boolean repeat[] = {true, false, false, false, false, false, false};
        admin.getDepartmentByName("Kassa").getBreakHandler().setMinBreakLength(WeekHandler.plusMinutes(15));
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + (WeekHandler.plusHours(1)), date.getTime() + (WeekHandler.plusHours(5)), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + (WeekHandler.plusHours(1)), date.getTime() + (WeekHandler.plusHours(5)), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + (WeekHandler.plusHours(1)), date.getTime() + (WeekHandler.plusHours(5)), repeat);

        assertTrue(admin.getDepartmentByName("Kassa").getSizeAllShifts() == 3);
    }

    @Test
    public void removeDepartment() {
        Admin a = Admin.getInstance();
        a.createNewDepartment("Kassa", 2);
        Date d = new Date();
        boolean repeat[] = {true, true, true, true, true, true, true};
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() + (WeekHandler.plusHours(1)), d.getTime() + (WeekHandler.plusHours(5)), repeat);
        assertEquals(1, a.getDepartmentListSize());
        a.removeDepartment(a.getDepartmentByName("Kassa"));
        assertEquals(0, a.getDepartmentListSize());
        boolean didItNotWork = false;
        try {
            OurCalendar.getInstance().getWorkday(d.getDate()).getWorkShifts(a.getDepartmentByName("Kassa")).size();
        } catch (NullPointerException e) {
            didItNotWork = true;
        }
        assertTrue(didItNotWork);
    }
}
