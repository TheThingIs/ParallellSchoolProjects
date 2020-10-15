package View;

import Model.Employee;
import Model.WorkShift;
import com.sun.javafx.scene.control.IntegerField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Date;

public class SchemaWorkshift extends AnchorPane {
    private Employee employee;
    private Color color;
    private double sizeX = 900, fullDay = 1000*60*60*24;;

    @FXML AnchorPane timeBar;
    @FXML Label start, end, name, departmentName;

    public SchemaWorkshift(WorkShift workShift, Color color, String departmentName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SchemaWorkshift.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.employee = workShift.getEmployee();
        this.color = color;
        this.start.setText(new Date(workShift.START).toString());
        this.end.setText(new Date(workShift.END).toString());
        this.departmentName.setText(departmentName);
        this.timeBar.setPrefWidth(sizeX*percentageOfDayFilled(workShift.START, workShift.END));
        this.timeBar.setTranslateX(getOffset(workShift.START)*sizeX);
        if (workShift.getEmployee()==null) {
            this.timeBar.setStyle("-fx-background-color: #BBBBBB");
            this.name.setText("Ledig!");
        }
        else {
            StringBuilder red = new StringBuilder(), blue = new StringBuilder(), green = new StringBuilder();
            if (color.getRed()< 1/16.0)
                red.append("0");
            if (color.getRed()==0)
                red.append("0");
            red.append(Integer.toHexString((int)(color.getRed()*255)));
            if (color.getGreen()< 1/16.0)
                green.append("0");
            if (color.getGreen()==0)
                green.append("0");
            green.append(Integer.toHexString((int)(color.getGreen()*255)));
            if (color.getBlue()< 1/16.0) {
                blue.append("0");
            }
            if (color.getBlue()==0) {
                blue.append("0");
            }
            blue.append(Integer.toHexString((int)(color.getBlue()*255)));
            /*
            System.out.println(Integer.toHexString((int)(color.getRed()*255)));
            System.out.println(Integer.toHexString((int)(color.getGreen()*255)));
            System.out.println(Integer.toHexString((int)(color.getBlue()*255)));
            */
            this.timeBar.setStyle("-fx-background-color: #"+red.toString()+green.toString()+blue.toString());
            this.name.setText(workShift.getEmployee().getPersonalId());
        }
    }

    private double percentageOfDayFilled(long start, long end){
        long difference = end-start;
        return difference/fullDay;
    }

    private double getOffset(long start){
        Date tmp = new Date(start);
        tmp.setHours(0);
        tmp.setMinutes(0);
        tmp.setSeconds(0);
        System.out.println(start-tmp.getTime());
        return ((start-tmp.getTime())/fullDay);
    }
}
