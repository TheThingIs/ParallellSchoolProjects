package main.java.view;

import main.java.model.WeekHandler;
import main.java.model.WorkShift;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.Date;

public class SchemaWorkshift extends AnchorPane {
    /**
     * @author Oliver Andersson
     * Visual representation of a WorkShift. Is viewed in a ListView
     * @since 2020-10-07
     */
    @FXML
    AnchorPane timeBar;
    @FXML
    Label start, end, name, departmentName, breakeStart, breakeEnd;

    public SchemaWorkshift(WorkShift workShift, Color color, String departmentName) {
        double sizeX = 900;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SchemaWorkshift.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date d = new Date(workShift.START);
        this.start.setText("Arbetspass mellan " + d.getHours() + ":" + d.getMinutes());
        d.setTime(workShift.END);
        this.end.setText(d.getHours() + ":" + d.getMinutes());
        d.setTime(workShift.getBreakTime().START);
        this.breakeStart.setText("Rast mellan " + d.getHours() + ":" + d.getMinutes());
        d.setTime(workShift.getBreakTime().END);
        this.breakeEnd.setText(d.getHours() + ":" + d.getMinutes());
        this.departmentName.setText(departmentName);
        this.timeBar.setPrefWidth(sizeX * percentageOfDayFilled(workShift.START, workShift.END));
        this.timeBar.setTranslateX(getOffset(workShift.START) * sizeX);
        if (workShift.getEmployee() == null) {
            BackgroundFill tmp = new BackgroundFill(new Color(0.7, 0.7, 0.7, 1), new CornerRadii(0), new Insets(0));
            this.timeBar.setBackground(new Background(tmp));
            this.name.setText("Ledig!");
        } else {
            BackgroundFill tmp = new BackgroundFill(color, new CornerRadii(0), new Insets(0));
            this.timeBar.setBackground(new Background(tmp));
            this.name.setText(workShift.getEmployee().getPersonalId());
        }
    }

    private double percentageOfDayFilled(long start, long end) {
        long difference = end - start;
        return ((double) difference) / ((double) WeekHandler.plusDays(1));
    }

    private double getOffset(long start) {
        Date tmp = new Date(start);
        tmp.setHours(0);
        tmp.setMinutes(0);
        tmp.setSeconds(0);
        return ((double) (start - tmp.getTime()) / ((double) WeekHandler.plusDays(1)));
    }
}
