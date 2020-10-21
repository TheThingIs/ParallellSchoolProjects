package Model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Markus Grahn
 * Represents an static admin for the project with is the heart of the application
 * @since 2020-10-16
 */
public class ImportServicePackage {

    public static void loadPackage() {
        Admin admin = Admin.getInstance();
        Date date = new Date();

        admin.createNewUser("Moa", "Hemligt");
        date.setTime(OurCalendar.getInstance().getDate(date).DATE + WeekHandler.plusDays(1));

        admin.createNewEmployee("Moa Borg", "200005174938", "Moa@gmail.com", "03185552266");
        admin.createNewEmployee("Henrik Efson", "197902238120", "Henrik.Ef@gmail.com", "03185552267");
        admin.createNewEmployee("Crazy Jim", "196512247027", "Not.Crazy@gmail.com", "03185552268");
        admin.createNewEmployee("Fredrik Johanson", "198511097928", "Fredrik-Johanson13@hotmail.com", "03185552269");
        admin.createNewEmployee("Sara Samson", "198806015472", "samsonsara@gmail.com", "03185552262");
        admin.createNewEmployee("Elsa Frost", "199010311276", "Elsa.Frost@email.com", "03185552263");
        admin.createNewEmployee("Dwight Schrute", "197001201337", "Dwight@Schrutefarms.com", "03185552264");
        admin.createNewEmployee("Marvin Lost", "197905279746", "Marvin@find.com", "03185552265");
        admin.createNewEmployee("Toph Beifong", "196804284928", "Toph@gaang.com", "03185552260");

        admin.createNewDepartment("Kassa", 2, Color.BLUE);
        admin.createNewDepartment("Chark", 2, Color.RED);
        admin.createNewDepartment("Frukt", 2, Color.GREEN);

        admin.createCertificate("Kassa Behörig");
        admin.createCertificate("Kassa Manager");
        admin.createCertificate("Kött hantering");
        admin.createCertificate("Fisk hantering");
        admin.createCertificate("Packetering");

        admin.setGuaranteedFreeTime(8);

        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Manager"), admin.getEmployeeByName("Moa Borg"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Manager"), admin.getEmployeeByName("Henrik Efson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Manager"), admin.getEmployeeByName("Marvin Lost"));

        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Behörig"), admin.getEmployeeByName("Henrik Efson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Behörig"), admin.getEmployeeByName("Fredrik Johanson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Behörig"), admin.getEmployeeByName("Sara Samson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Behörig"), admin.getEmployeeByName("Elsa Frost"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Behörig"), admin.getEmployeeByName("Dwight Schrute"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kassa Behörig"), admin.getEmployeeByName("Toph Beifong"));

        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kött hantering"), admin.getEmployeeByName("Moa Borg"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kött hantering"), admin.getEmployeeByName("Crazy Jim"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kött hantering"), admin.getEmployeeByName("Fredrik Johanson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Kött hantering"), admin.getEmployeeByName("Dwight Schrute"));

        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Fisk hantering"), admin.getEmployeeByName("Moa Borg"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Fisk hantering"), admin.getEmployeeByName("Crazy Jim"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Fisk hantering"), admin.getEmployeeByName("Sara Samson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Fisk hantering"), admin.getEmployeeByName("Dwight Schrute"));

        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Packetering"), admin.getEmployeeByName("Moa Borg"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Packetering"), admin.getEmployeeByName("Fredrik Johanson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Packetering"), admin.getEmployeeByName("Crazy Jim"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Packetering"), admin.getEmployeeByName("Sara Samson"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Packetering"), admin.getEmployeeByName("Elsa Frost"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Packetering"), admin.getEmployeeByName("Marvin Lost"));
        admin.createEmployeeCertificate(admin.getCertificatehandler().getCertificate("Packetering"), admin.getEmployeeByName("Toph Beifong"));

        ArrayList<Certificate> certificates = new ArrayList<>();
        certificates.add(admin.getCertificatehandler().getCertificate("Kassa Manager"));
        certificates.add(admin.getCertificatehandler().getCertificate("Kassa Behörig"));
        boolean arr[] = {false, true, false, false, true, false, false};
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + WeekHandler.plusHours(6), date.getTime() + WeekHandler.plusHours(16), certificates, arr);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + WeekHandler.plusHours(12), date.getTime() + WeekHandler.plusHours(22), admin.getCertificatehandler().getCertificate("Kassa Behörig"), arr);

        arr = new boolean[]{false, false, true, true, false, true, true};
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + WeekHandler.plusHours(6), date.getTime() + WeekHandler.plusHours(15), admin.getCertificatehandler().getCertificate("Kassa Behörig"), arr);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + WeekHandler.plusHours(11), date.getTime() + WeekHandler.plusHours(20), admin.getCertificatehandler().getCertificate("Kassa Behörig"), arr);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + WeekHandler.plusHours(14), date.getTime() + WeekHandler.plusHours(22), admin.getCertificatehandler().getCertificate("Kassa Manager"), arr);

        certificates.clear();
        certificates.add(admin.getCertificatehandler().getCertificate("Packetering"));
        certificates.add(admin.getCertificatehandler().getCertificate("Kött hantering"));
        arr = new boolean[]{false, true, true, false, false, true, true};
        admin.createWorkshift(admin.getDepartmentByName("Chark"), date.getTime() + WeekHandler.plusHours(7), date.getTime() + WeekHandler.plusHours(15), certificates, arr);
        admin.createWorkshift(admin.getDepartmentByName("Chark"), date.getTime() + WeekHandler.plusHours(12), date.getTime() + WeekHandler.plusHours(20), admin.getCertificatehandler().getCertificate("Kött hantering"), arr);
        admin.createWorkshift(admin.getDepartmentByName("Chark"), date.getTime() + WeekHandler.plusHours(13), date.getTime() + WeekHandler.plusHours(21), certificates, arr);

        certificates.clear();
        certificates.add(admin.getCertificatehandler().getCertificate("Packetering"));
        certificates.add(admin.getCertificatehandler().getCertificate("Fisk hantering"));
        arr = new boolean[]{false, false, false, true, false, false, false};
        admin.createWorkshift(admin.getDepartmentByName("Chark"), date.getTime() + WeekHandler.plusHours(5), date.getTime() + WeekHandler.plusHours(12), certificates, arr);
        admin.createWorkshift(admin.getDepartmentByName("Chark"), date.getTime() + WeekHandler.plusHours(11), date.getTime() + WeekHandler.plusHours(19), admin.getCertificatehandler().getCertificate("Fisk hantering"), arr);
        admin.createWorkshift(admin.getDepartmentByName("Chark"), date.getTime() + WeekHandler.plusHours(13), date.getTime() + WeekHandler.plusHours(21), certificates, arr);

        arr = new boolean[]{false, true, true, true, true, true, true};
        admin.createWorkshift(admin.getDepartmentByName("Frukt"), date.getTime() + WeekHandler.plusHours(6), date.getTime() + WeekHandler.plusHours(13), admin.getCertificatehandler().getCertificate("Packetering"), arr);
        admin.createWorkshift(admin.getDepartmentByName("Frukt"), date.getTime() + WeekHandler.plusHours(13), date.getTime() + WeekHandler.plusHours(21), admin.getCertificatehandler().getCertificate("Packetering"), arr);

        arr = new boolean[]{true, false, false, false, false, false, false};
        admin.createWorkshift(admin.getDepartmentByName("Frukt"), date.getTime() + WeekHandler.plusHours(6), date.getTime() + WeekHandler.plusHours(12), admin.getCertificatehandler().getCertificate("Packetering"), arr);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + WeekHandler.plusHours(8), date.getTime() + WeekHandler.plusHours(12), admin.getCertificatehandler().getCertificate("Packetering"), arr);
        admin.createWorkshift(admin.getDepartmentByName("Kassa"), date.getTime() + WeekHandler.plusHours(8), date.getTime() + WeekHandler.plusHours(12), admin.getCertificatehandler().getCertificate("Packetering"), arr);

        ArrayList<Employee> employees = new ArrayList<>();
        for (int i = 0; i < admin.getEmployeeListSize(); i++) {

            employees.add(admin.getEmployee(i));
        }
        ArrayList<WorkDay> workDays = new ArrayList<>();
        for (int i = date.getDate(); i < 14 + date.getDate(); i++) {
            //System.out.println("dagar");
            workDays.add(OurCalendar.getInstance().getWorkday(i));
        }
        admin.getEmployeeSorter().sortPotentialWorkShiftCandidate(employees, workDays);
        admin.getEmployeeSorter().delegateEmployeeToWorkshift();
    }

}
