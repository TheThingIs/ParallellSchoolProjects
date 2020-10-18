import Model.*;
import View.Schema;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Oliver Andersson
 * Runnable class for the project
 * @since 2020-10-07
 */
public class Runnable extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.getProperties();
        /*
        boolean repeat[] = {false, false, false, false, false, false, false};
        long tmp = OurCalendar.getInstance().getWorkday(16).DATE;
        Admin.getInstance().createNewDepartment("Disken", 2, new Color(1, 0.8, 0.4, 0.6));
        Admin.getInstance().createWorkshift(Admin.getInstance().getDepartmentByName("Disken"), tmp+1000*3600*18, tmp+1000*3600*23, repeat);
        Admin.getInstance().createWorkshift(Admin.getInstance().getDepartmentByName("Disken"), tmp+1000*3600*17, tmp+1000*3600*23, repeat);
        Admin.getInstance().createWorkshift(Admin.getInstance().getDepartmentByName("Disken"), tmp+1000*3600*18, tmp+1000*3600*20, repeat);
        Admin.getInstance().createWorkshift(Admin.getInstance().getDepartmentByName("Disken"), tmp+1000*3600*19, tmp+1000*3600*23, repeat);
        Admin.getInstance().createWorkshift(Admin.getInstance().getDepartmentByName("Disken"), tmp+1000*3600*18, tmp+1000*3600*23, repeat);
        Admin.getInstance().createNewEmployee("Oliver Andersson", "200011221122", "oliver@gallerit.se");
        Department tmpDepartment = Admin.getInstance().getDepartmentByName("Disken");
        Employee tmpEmployee = Admin.getInstance().getEmployee(0);
        WorkDay tmpWorkDay = OurCalendar.getInstance().getWorkday(16);
        //tmpWorkDay.setWorkDay();
        WorkShift tmpWorkshift = tmpWorkDay.getWorkShifts(tmpDepartment).get(0);
        tmpWorkDay.occupiesEmployee(tmpWorkshift, tmpEmployee);*/
        ImportServicePackage.loadPackage();
        /*
        for (int index = 0; index<10; index++)
            Admin.getInstance().createNewEmployee("Oliver Andersson", Long.toString(200006010000L+index));
        Admin.getInstance().getCertificatehandler().createNewCertificate("Bil");
        Admin.getInstance().getCertificatehandler().createNewCertificate("Kassa");
        Admin.getInstance().createEmployeeCertificate(Admin.getInstance().getCertificatehandler().getCertificate("Bil"), Admin.getInstance().getEmployees().get(0), new Date(9999999999L));
        Admin.getInstance().createEmployeeCertificate(Admin.getInstance().getCertificatehandler().getCertificate("Kassa"), Admin.getInstance().getEmployees().get(0), new Date(9999999999L));
        */
        URL url = new File("src/View/StartPage.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
            primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                //TODO shutdownhook
            }
        });
    }
}
