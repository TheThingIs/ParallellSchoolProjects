package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import main.java.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class testWorkShift {

    @Test
    public void testAddCertificateToWorkShift() {
        Admin a = Admin.getInstance();
        boolean repeat[] = {false, false, false, false, false, false, false};
        CertificateHandler ch = CertificateHandler.getInstance();
        Date d = new Date();
        List<Certificate> allcert = new ArrayList<>();
        ch.createNewCertificate("Frukt");
        allcert.add(ch.getCertificate("Frukt"));
        a.createNewDepartment("Frukt", 1);
        a.createWorkshift(a.getDepartmentByName("Frukt"), d.getTime()+1000, (d.getTime() + WeekHandler.plusHours(8)), allcert, repeat);
        ch.createNewCertificate("Kassa");
        a.getDepartmentByName("Frukt").getShift(0).addCertificate(ch.getCertificate("Kassa"));
        assertTrue(a.getDepartmentByName("Frukt").getShift(0).getCertificatesSize() == 2);
    }


    @Test
    public void testRemoveCertificateFromWorkShift() {
        boolean repeat[] = {false, false, false, false, false, false, false};
        Admin a = Admin.getInstance();
        Date d = new Date();
        CertificateHandler ch = CertificateHandler.getInstance();
        ch.createNewCertificate("Kassa");
        List<Certificate> allcert = new ArrayList<>();
        allcert.add(ch.getCertificate("Kassa"));
        a.createNewDepartment("Frukt", 1);
        a.createWorkshift(a.getDepartmentByName("Frukt"), d.getTime()+1000, (d.getTime() + WeekHandler.plusHours(8)), allcert, repeat);
        a.getDepartmentByName("Frukt").getShift(0).removeCertificate(ch.getCertificate("Kassa"));
        assertTrue(a.getDepartmentByName("Frukt").getShift(0).getCertificatesSize() == 0);
    }

    @Test
    public void testRemoveWorkShift() {
        Admin a = Admin.getInstance();
        Date d = new Date();
        boolean repeat[] = {false, false, false, false, false, false, false};
        a.createNewDepartment("Kassa", 1);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() + 1111, d.getTime() + 11111, repeat);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() + 1111, d.getTime() + 11111, repeat);
        a.removeWorkshift(a.getDepartmentByName("Kassa"), a.getDepartmentByName("Kassa").getShift(0));
        assertEquals(1, a.getDepartmentByName("Kassa").getSizeAllShifts());
    }

    @Test
    public void testEditEmployees() {
        Admin a = Admin.getInstance();
        Date d = new Date();
        boolean repeat[] = {true, true, true, true, true, true, true};
        a.createNewDepartment("Kassa", 1);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() + 1111, d.getTime() + 11111, repeat);
        a.createNewEmployee("Cristian är kass", "133742042069", "kass@gmail.com", "0315552266");
        a.createWorkshift(a.getDepartmentByName("Kassa"), OurCalendar.getInstance().getWorkday(d.getDate()+1).DATE + 10, OurCalendar.getInstance().getWorkday(d.getDate()+1).DATE + 1100, repeat);
        OurCalendar.getInstance().getWorkday(1).occupiesEmployee(a.getDepartmentByName("Kassa").getShift(0), a.getEmployeeByID("133742042069"));
        assertTrue(a.getDepartmentByName("Kassa").getShift(0).getEmployee().getPersonalId().equals("133742042069"));
        a.createNewEmployee("Markus passar bättre här", "694201337420", "bättre@gmail.com", "0315552666");
        OurCalendar.getInstance().getWorkday(1).reOccupieEmployee(a.getDepartmentByName("Kassa").getShift(0), a.getEmployeeByID("694201337420"));
        assertTrue(a.getDepartmentByName("Kassa").getShift(0).getEmployee().getPersonalId().equals("694201337420"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWorkShiftExeptions() {
        Admin a = Admin.getInstance();
        Date d = new Date();
        boolean repeat[] = {true, true, true, true, true, true, true};
        a.createNewDepartment("Kassa", 1);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime(), d.getTime() + 11111, repeat);
    }
}
