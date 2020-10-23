package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import main.java.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class testWorkday {

    @Test
    public void testoccupiesEmployee() {
        Admin a = Admin.getInstance();
        boolean repeat[] = {true, true, true, true, true, true, true};
        a.createNewEmployee("moa", "000211444444", "moa@email.com", "0315552266");
        Date d = new Date();
        d.setTime(d.getTime() + 1000);
        CertificateHandler ch = CertificateHandler.getInstance();
        ch.createNewCertificate("Kassa");
        Certificate allcert = ch.getCertificate("Kassa");
        a.createNewDepartment("Kassa", 1);
        a.createEmployeeCertificate(ch.getCertificate("Kassa"), a.getEmployeeByName("moa"));
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime(), (d.getTime() + WeekHandler.plusHours(8)), allcert, repeat);
        WorkDay workday = OurCalendar.getInstance().getWorkday(d.getDate() - 1);
        a.setGuaranteedFreeTime(10);
        workday.occupiesEmployee(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0), a.getEmployeeByName("moa"));
        assertTrue(a.getEmployeeByName("moa").isOccupied((d.getTime() + WeekHandler.plusHours(17)), (d.getTime() + WeekHandler.plusHours(31))));
    }

    @Test
    public void testWeekly() { //yes, this is an insane test and I dont know what to do about it
        int dag = new Date().getDate() + 3;//the date we wanna set the workshifts at + however many days in the future you wanna set
        Admin a = Admin.getInstance();
        boolean repeat[] = {true, true, true, false, false, true, false}; //what days we wanna set workshifts at
        boolean repeat2[] = {true, false, true, false, false, false, false};
        a.createNewDepartment("Kassa", 1);
        a.createNewDepartment("Frukt", 1);
        a.createWorkshift(a.getDepartmentByName("Kassa"), OurCalendar.getInstance().getWorkday(dag).DATE + 100, OurCalendar.getInstance().getWorkday(dag).DATE + WeekHandler.plusMinutes(60), repeat);
        a.createWorkshift(a.getDepartmentByName("Kassa"), OurCalendar.getInstance().getWorkday(dag).DATE + 100, OurCalendar.getInstance().getWorkday(dag).DATE + WeekHandler.plusMinutes(60), repeat2);
        a.createWorkshift(a.getDepartmentByName("Frukt"), OurCalendar.getInstance().getWorkday(dag).DATE + 100, OurCalendar.getInstance().getWorkday(dag).DATE + WeekHandler.plusMinutes(60), repeat2);
        int countKassa[] = {0, 0, 0, 0, 0, 0, 0}; //counts all WorkShifts for Kassa, starts at "dag"s weekday
        int countFrukt[] = {0, 0, 0, 0, 0, 0, 0}; //counts all Workshifts for Frukt, starts at "dag"s weekday
        for (int i = 0; i < 7; i++) { //we set and check how many workshifts that actually have been set
            countKassa[i] = OurCalendar.getInstance().getWorkday(i + dag).getWorkShifts(a.getDepartmentByName("Kassa")).size();
            countFrukt[i] = OurCalendar.getInstance().getWorkday(i + dag).getWorkShifts(a.getDepartmentByName("Frukt")).size();
        }
        int offSet = new Date(OurCalendar.getInstance().getWorkday(dag).DATE).getDay(); //setting an offset so the order will match the order the days are set
        int countExpectedKassa[] = {2, 1, 2, 0, 0, 1, 0}; //expected value of Kassa in the correct order sun-sat
        int countExpectedFrukt[] = {1, 0, 1, 0, 0, 0, 0}; //expected value of Frukt in the correct order sun-sat
        assertTrue(testArray(countExpectedKassa, countKassa, offSet));
        assertTrue(testArray(countExpectedFrukt, countFrukt, offSet));

    }

    public boolean testArray(int[] expected, int[] acctual, int offSet) {
        for (int i = 0; i < 7; i++) {
            if (expected[(i + offSet) % 7] != acctual[i]) {
                System.out.print(expected[i] + " " + acctual[i] + " " + i);
                return false;
            }
        }
        return true;
    }


    @Test
    public void testOccupieAnOccupiedEmployee() {
        Admin a = Admin.getInstance();
        a.createNewEmployee("moa", "000211444444", "moa@email.com", "0315552255");
        Date d = new Date();
        boolean repeat[] = {false, false, false, false, false, false, false};
        d.setTime(d.getTime() + (24 * 60 * 60 * 1000));
        CertificateHandler ch = CertificateHandler.getInstance();
        ch.createNewCertificate("Kassa");
        Certificate allcert = ch.getCertificate("Kassa");
        a.createEmployeeCertificate(ch.getCertificate("Kassa"), a.getEmployeeByName("moa"));
        a.createNewDepartment("Kassa", 1);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime(), (d.getTime() + WeekHandler.plusHours(8)), allcert, repeat);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() + 1000, (d.getTime() + 1000 + WeekHandler.plusHours(8)), allcert, repeat);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() - 1000, (d.getTime() - 1000 + WeekHandler.plusHours(8)), allcert, repeat);
        WorkDay workday = OurCalendar.getInstance().getWorkday(d.getDate() - 1);
        a.setGuaranteedFreeTime(10);
        workday.occupiesEmployee(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0), a.getEmployeeByName("moa"));
        try {
            workday.occupiesEmployee(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(1), a.getEmployeeByName("moa"));
        } catch(IllegalArgumentException e) {
            assertEquals("The employee cannot occupy the shift", e.getMessage());
        }
        try {
            workday.occupiesEmployee(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(2), a.getEmployeeByName("moa"));
        } catch(IllegalArgumentException e) {
            assertEquals("The employee cannot occupy the shift", e.getMessage());
        }
    }

    @Test
    public void testCertifiedEmployee() {
        Admin a = Admin.getInstance();
        boolean repeat[] = {true, true, true, true, true, true, true};
        a.createNewEmployee("moa", "000211444444", "moa@email.com", "0315552566");
        Date d = new Date();
        d.setTime(d.getTime() + (WeekHandler.plusHours(24)));
        CertificateHandler ch = CertificateHandler.getInstance();
        ch.createNewCertificate("Kassa");
        ch.createNewCertificate("Frukt");
        List<Certificate> allcert = new ArrayList<>();
        allcert.add(ch.getCertificate("Kassa"));
        allcert.add(ch.getCertificate("Frukt"));
        a.createNewDepartment("Kassa", 1);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime(), (d.getTime() + WeekHandler.plusHours(8)), allcert, repeat);
        a.createEmployeeCertificate(ch.getCertificate("Kassa"), a.getEmployeeByName("moa"));
        WorkDay workday = OurCalendar.getInstance().getWorkday(d.getDate());
        a.setGuaranteedFreeTime(10);
        try {
            workday.occupiesEmployee(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0), a.getEmployeeByName("moa"));
        } catch(IllegalArgumentException e) {
            assertEquals("The employee cannot occupy the shift", e.getMessage());
        }
        assertTrue(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0).getEmployee() != a.getEmployeeByName("moa"));
    }

    @Test
    public void testSwapOccupation() {
        boolean repeat[] = {false, false, false, false, false, false, false};
        Admin a = Admin.getInstance();
        a.createNewEmployee("moa", "000211444444", "moa@email.se", "0315552466"); //e1
        a.createNewEmployee("sam", "000211444442", "sam@ntiskolan.se", "0315352266"); //e2
        a.createNewEmployee("mas", "000211444443", "mas@nej.com", "0315552226"); //e3
        Date d = new Date();
        d.setTime(d.getTime() + (WeekHandler.plusHours(24)));
        CertificateHandler ch = CertificateHandler.getInstance();
        ch.createNewCertificate("Kassa");
        List<Certificate> allcert = new ArrayList<>();
        allcert.add(ch.getCertificate("Kassa"));
        a.createNewDepartment("Kassa", 1);
        a.createEmployeeCertificate(ch.getCertificate("Kassa"), a.getEmployeeByName("moa"));
        a.createEmployeeCertificate(ch.getCertificate("Kassa"), a.getEmployeeByName("sam"));
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime(), (d.getTime() + WeekHandler.plusHours(8)), repeat);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime(), (d.getTime() + WeekHandler.plusHours(8)), repeat);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() + WeekHandler.plusDays(1), (d.getTime() + (WeekHandler.plusHours(8)) + WeekHandler.plusDays(1)), allcert, repeat);
        a.createWorkshift(a.getDepartmentByName("Kassa"), d.getTime() + WeekHandler.plusDays(1), (d.getTime() + (WeekHandler.plusHours(8)) + WeekHandler.plusDays(1)), repeat);
        WorkDay workday = OurCalendar.getInstance().getWorkday(d.getDate() - 1);
        WorkDay workday2 = OurCalendar.getInstance().getWorkday(d.getDate());
        a.setGuaranteedFreeTime(10);
        workday.occupiesEmployee(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0), a.getEmployeeByName("moa")); //w1
        workday.occupiesEmployee(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(1), a.getEmployeeByName("mas")); //w2
        workday.occupiesEmployee(workday2.getWorkShifts(a.getDepartmentByName("Kassa")).get(0), a.getEmployeeByName("sam")); //w3
        workday.occupiesEmployee(workday2.getWorkShifts(a.getDepartmentByName("Kassa")).get(1), a.getEmployeeByName("mas")); //w4

        workday.swapOccupation(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0), workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(1));
        assertTrue(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0).getEmployee() == a.getEmployeeByName("mas"));
        workday.swapOccupation(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0), workday2.getWorkShifts(a.getDepartmentByName("Kassa")).get(0));
        assertTrue(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(0).getEmployee() == a.getEmployeeByName("mas"));
        workday.swapOccupation(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(1), workday2.getWorkShifts(a.getDepartmentByName("Kassa")).get(0));
        assertTrue(workday.getWorkShifts(a.getDepartmentByName("Kassa")).get(1).getEmployee() == a.getEmployeeByName("sam"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void GetInvalidDepartmentName() {
        Admin a = Admin.getInstance();
        a.createNewDepartment("Kassa", 1);
        a.getDepartmentByName("kassa");
    }

    @Test(expected = IllegalArgumentException.class)
    public void GetInvalidDate() {
        Date d = new Date();
        d.setYear(2010);
        OurCalendar calendar = OurCalendar.getInstance();
        calendar.getDate(d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GetInvalidDateIndex() {
        Date d = new Date();
        d.setYear(2010);
        OurCalendar calendar = OurCalendar.getInstance();
        calendar.getDate(d);
    }


}
