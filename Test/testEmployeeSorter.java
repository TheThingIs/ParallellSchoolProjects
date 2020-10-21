import Model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

public class testEmployeeSorter {
    @Test
    public void testGetQualifiedPersons() {
        Admin admin = Admin.getInstance();
        CertificateHandler ch = admin.getCertificatehandler();
        admin.createNewEmployee("moa", "123456789231", "moa@email.nej", "0315552267");
        admin.createNewEmployee("Victor", "123456789234", "Victor@haha.ha", "0315552866");
        ch.createNewCertificate("Kassa");
        ch.createNewCertificate("Frukt");
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("moa"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("moa"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("Victor"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("Victor"), new Date());
        List<Certificate> allcert = new ArrayList<>();
        allcert.add(ch.getCertificate("Kassa"));
        allcert.add(ch.getCertificate("Frukt"));
        //WorkShift w= new WorkShift(d.getTime(),(d.getTime()+(1000 * 60 * 60 * 8)),new OccupiedTime(2,2));
        //assertTrue(admin.getEmployeeSorter().getQualifiedPersons(w, admin.getEmployees()).size() == 2);
    }

    @Test
    public void testGetListOfPotentialPersons() {
        Admin admin = Admin.getInstance();
        CertificateHandler ch = admin.getCertificatehandler();
        admin.createNewEmployee("moa", "123456789231", "moa@email.nej", "031555226");
        admin.createNewEmployee("Victor", "123456789234", "Victor@haha.ha", "0315554286");
        admin.createNewEmployee("markus1", "123456789225", "moa@email.nejs", "0315562266");
        admin.createNewEmployee("markus2", "123456789236", "Victor@haha.had", "0315252266");
        admin.createNewEmployee("Oliver", "123456789245", "moa@email.nejg", "0315052266");
        admin.createNewEmployee("Sam", "123456789247", "Victor@haha.har", "0315559266");
        admin.createNewEmployee("Abethenoob", "123456789288", "moa@email.nejh", "0315552666");
        admin.createNewEmployee("Volvoraggare", "123456789299", "Victor@haha.markus", "0315552286");

        ch.createNewCertificate("Kassa");
        ch.createNewCertificate("Frukt");
        ch.createNewCertificate("Volvoexpert");

        List<Certificate> allcert = new ArrayList<>();
        allcert.add(ch.getCertificate("Kassa"));
        allcert.add(ch.getCertificate("Frukt"));
        allcert.add(ch.getCertificate("Volvoexpert"));


        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("moa"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("Victor"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("markus2"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("Abethenoob"), new Date());

        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("moa"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("Victor"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("markus2"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("Abethenoob"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("Sam"), new Date());

        admin.createEmployeeCertificate(ch.getCertificate("Volvoexpert"), admin.getEmployeeByName("Volvoraggare"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Volvoexpert"), admin.getEmployeeByName("markus2"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Volvoexpert"), admin.getEmployeeByName("Abethenoob"), new Date());

        admin.createNewDepartment("Kassa", 10);
        admin.createNewDepartment("Bilmekaniker", 8);
        admin.createNewDepartment("Frukt", 2);
        admin.createNewDepartment("Soffliggare", 1000);

        boolean repeat[] = {true, true, true, true, true, true, true};
        Date d = new Date();

        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), ch.getCertificate("Kassa"), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Frukt"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), ch.getCertificate("Frukt"), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Soffliggare"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Bilmekaniker"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), allcert, repeat);

        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<WorkDay> workDays = new ArrayList<>();

        for (int j = 0; j < admin.getEmployeeListSize(); j++) {
            employees.add(admin.getEmployee(j));
        }

        for (int j = 0; j < 7; j++) {
            workDays.add(OurCalendar.getInstance().getWorkday(j));
        }


        admin.getEmployeeSorter().sortPotentialWorkShiftCandidate(employees, workDays);

        ArrayList<WorkDay> wd = new ArrayList<>();

        ArrayList<WorkShift> workShifts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            wd.add(OurCalendar.getInstance().getWorkday(i));
        }

        assertEquals(4, admin.getEmployeeSorter().getPotentialEmployees(wd.get(0).getWorkShifts(admin.getDepartmentByName("Kassa")).get(0)).size());
        assertEquals(5, admin.getEmployeeSorter().getPotentialEmployees(wd.get(0).getWorkShifts(admin.getDepartmentByName("Frukt")).get(0)).size());
        assertEquals(8, admin.getEmployeeSorter().getPotentialEmployees(wd.get(0).getWorkShifts(admin.getDepartmentByName("Soffliggare")).get(0)).size());
        assertEquals(2, admin.getEmployeeSorter().getPotentialEmployees(wd.get(0).getWorkShifts(admin.getDepartmentByName("Bilmekaniker")).get(0)).size());


    }

    @Test
    public void testSortWorkShiftPotentialWorkShiftCandidate() {
        Admin admin = Admin.getInstance();
        CertificateHandler ch = admin.getCertificatehandler();
        admin.createNewEmployee("moa", "123456789231", "moa@email.nej", "0315552266");
        admin.createNewEmployee("Victor", "123456789234", "Victor@haha.ha", "0315552261");
        admin.createNewEmployee("markus1", "123456789225", "moa@email.nejs", "0315552268");
        admin.createNewEmployee("markus2", "123456789236", "Victor@haha.had", "0315552286");
        admin.createNewEmployee("Oliver", "123456789245", "moa@email.nejg", "0315552466");
        admin.createNewEmployee("Sam", "123456789247", "Victor@haha.har", "0315582266");
        admin.createNewEmployee("Abethenoob", "123456789288", "moa@email.nejh", "0315552666");
        admin.createNewEmployee("Volvoraggare", "123456789299", "Victor@haha.markus", "031555266");

        ch.createNewCertificate("Kassa");
        ch.createNewCertificate("Frukt");
        ch.createNewCertificate("Volvoexpert");

        List<Certificate> allcert = new ArrayList<>();
        allcert.add(ch.getCertificate("Kassa"));
        allcert.add(ch.getCertificate("Frukt"));
        allcert.add(ch.getCertificate("Volvoexpert"));


        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("moa"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("Victor"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("markus2"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Kassa"), admin.getEmployeeByName("Abethenoob"), new Date());

        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("moa"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("Victor"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("markus2"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("Abethenoob"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Frukt"), admin.getEmployeeByName("Sam"), new Date());

        admin.createEmployeeCertificate(ch.getCertificate("Volvoexpert"), admin.getEmployeeByName("Volvoraggare"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Volvoexpert"), admin.getEmployeeByName("markus2"), new Date());
        admin.createEmployeeCertificate(ch.getCertificate("Volvoexpert"), admin.getEmployeeByName("Abethenoob"), new Date());

        admin.createNewDepartment("Kassa", 10);
        admin.createNewDepartment("Bilmekaniker", 8);
        admin.createNewDepartment("Frukt", 2);
        admin.createNewDepartment("Soffliggare", 1000);

        boolean repeat[] = {true, true, true, true, true, true, true};
        Date d = new Date();

        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), ch.getCertificate("Kassa"), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Frukt"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), ch.getCertificate("Frukt"), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Soffliggare"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Bilmekaniker"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), allcert, repeat);

        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<WorkDay> workDays = new ArrayList<>();

        for (int j = 0; j < admin.getEmployeeListSize(); j++) {
            employees.add(admin.getEmployee(j));
        }

        for (int j = 0; j < 7; j++) {
            workDays.add(OurCalendar.getInstance().getWorkday(j));
        }


        admin.getEmployeeSorter().sortPotentialWorkShiftCandidate(employees, workDays);

        ArrayList<WorkDay> wd = new ArrayList<>();

        ArrayList<WorkShift> workShifts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            wd.add(OurCalendar.getInstance().getWorkday(i));
        }

        // Test sort
        Collections.sort(admin.getEmployeeSorter().workShifts, admin.getEmployeeSorter());

        assertEquals(2, admin.getEmployeeSorter().potentialWorkShiftCandidate.get(admin.getEmployeeSorter().workShifts.get(0)).size());
        assertEquals(4, admin.getEmployeeSorter().potentialWorkShiftCandidate.get(admin.getEmployeeSorter().workShifts.get(7)).size());
        assertEquals(5, admin.getEmployeeSorter().potentialWorkShiftCandidate.get(admin.getEmployeeSorter().workShifts.get(14)).size());
        assertEquals(8, admin.getEmployeeSorter().potentialWorkShiftCandidate.get(admin.getEmployeeSorter().workShifts.get(21)).size());

        admin.getEmployeeSorter().delegateEmployeeToWorkshift();
        for (int i = 0; i < admin.getEmployeeSorter().workShifts.size(); i++) {
            assertTrue(admin.getEmployeeSorter().workShifts.get(i).isOccupied());
        }

    }

    @Test
    public void testAllWorkShiftNotFull() {
        Admin admin = Admin.getInstance();
        CertificateHandler ch = admin.getCertificatehandler();
        admin.createNewEmployee("moa", "123456789231", "moa@email.nej", "0315552566");
        admin.createNewEmployee("Victor", "123456789234", "Victor@haha.ha", "0315562266");

        admin.createNewDepartment("Kassa", 10);

        boolean repeat[] = {true, true, true, true, true, true, true};
        Date d = new Date();

        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), d.getTime() + 1000, d.getTime() + WeekHandler.plusHours(3), repeat);

        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<WorkDay> workDays = new ArrayList<>();

        for (int j = 0; j < admin.getEmployeeListSize(); j++) {
            employees.add(admin.getEmployee(j));
        }

        for (int j = 0; j < 7; j++) {
            workDays.add(OurCalendar.getInstance().getWorkday(j));
        }

        admin.getEmployeeSorter().sortPotentialWorkShiftCandidate(employees, workDays);
        admin.getEmployeeSorter().delegateEmployeeToWorkshift();


    }

}
