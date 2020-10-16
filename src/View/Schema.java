package View;

import Model.*;
import Model.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

public class Schema extends AnchorPane implements Observer {
    @FXML Button next, previous, createWorkshift,discardButtonCreateNewShift,saveButtonCreateNewShift;
    @FXML GridPane monthGrid, weekGrid;
    @FXML AnchorPane dayView, monthView, weekView, workshiftPane;
    @FXML ComboBox<String> viewSelector;
    @FXML Label currentFormatInfo;
    @FXML ListView listOfWorkshifts;

    private int dateIndex;
    private Date currentIndex;
    private String mode = "";

    public Schema() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Schema.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        generateDate();
        generateComboBox();
        generateLabels();
        generateButtons();
    }

    private void generateLabels(){
        switch (mode){
            case "Dag":
                currentFormatInfo.setText(currentIndex.toString());
                break;
            case "Vecka":
                Calendar.getInstance().setTime(currentIndex);
                currentFormatInfo.setText("Vecka " + Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                break;
            case "Månad":
                currentFormatInfo.setText(currentIndex.getYear() + "/" + currentIndex.getMonth());
        }
    }

    private void generateDate(){
        currentIndex = new Date(OurCalendar.getInstance().getWorkday(0).DATE);
        dateIndex = 0;
    }

    private void generateComboBox(){
        viewSelector.getItems().clear();
        viewSelector.getItems().add("Månad");
        viewSelector.getItems().add("Vecka");
        viewSelector.getItems().add("Dag");
        viewSelector.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                mode = newValue;
                switch (newValue) {
                    case "Månad":
                        monthView.toFront();
                        monthView.setVisible(true);
                        dayView.setVisible(false);
                        weekView.setVisible(false);
                        workshiftPane.setVisible(false);
                        updateMonth();
                        break;
                    case "Vecka":
                        weekView.toFront();
                        weekView.setVisible(true);
                        dayView.setVisible(false);
                        monthView.setVisible(false);
                        workshiftPane.setVisible(false);
                        updateWeek();
                        break;
                    case "Dag":
                        dayView.toFront();
                        dayView.setVisible(true);
                        weekView.setVisible(false);
                        monthView.setVisible(false);
                        workshiftPane.setVisible(false);
                        updateDay();
                        break;
                }
            }
        });
    }

    public void next(){
        switch (mode){
            case "Dag":
                this.dateIndex++;
                updateDay();
                break;
            case "Vecka":
                this.dateIndex+=7;
                updateWeek();
                break;
            case "Månad":
                this.dateIndex+= YearMonth.of(new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getYear(), new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getMonth()).lengthOfMonth();
                updateMonth();
                break;
        }
    }
    public void previous(){
        switch (mode){
            case "Dag":
                this.dateIndex--;
                updateDay();
                break;
            case "Vecka":
                this.dateIndex-=7;
                updateWeek();
                break;
            case "Månad":
                this.dateIndex-= YearMonth.of(new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getYear(), new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getMonth()).lengthOfMonth();
                updateMonth();
                break;
        }
    }

    private void updateMonth(){
        for (int i = 0; i < YearMonth.of(currentIndex.getYear(), currentIndex.getMonth() + 1).lengthOfMonth(); i++){

        }
    }
    private void updateWeek(){
        int index = OurCalendar.getInstance().getWorkday(dateIndex).getDayOfWeekOffset();
        int secondIndex = 0;
        for (int i = -index; i<=7-index; i++){
            weekGrid.add(new DayScheduleView(OurCalendar.getInstance().getWorkday(i+dateIndex)), 0, secondIndex);
        }
    }
    private void updateDay(){
        //OurCalendar.getInstance().getWorkday(dateIndex).setWorkDay();
        listOfWorkshifts.getItems().clear();
        for (Department d : Admin.getInstance().getDepartments()){
            for (WorkShift w : OurCalendar.getInstance().getWorkday(dateIndex).getWorkShifts(d))
                listOfWorkshifts.getItems().add(new SchemaWorkshift(w, d.getColor()));
        }
    }

    private void generateButtons(){
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                next();
            }
        });
        previous.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                previous();
            }
        });
        discardButtonCreateNewShift.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                workshiftPane.setVisible(false);
                workshiftPane.toBack();
                workshiftPane.getChildren().remove(0);
            }
        });
        saveButtonCreateNewShift.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               CreateShiftView createShiftView= (CreateShiftView) workshiftPane.getChildren().get(0);
               createShiftView.save();
                workshiftPane.setVisible(false);
                workshiftPane.toBack();
                workshiftPane.getChildren().remove(0);

            }
        });
        createWorkshift.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                workshiftPane.getChildren().add(new CreateShiftView());
                workshiftPane.setVisible(true);
                workshiftPane.toFront();
                discardButtonCreateNewShift.toFront();
                saveButtonCreateNewShift.toFront();
            }
        });
    }

    private void sortEmployeesAlphabetically(List<Employee> employees){
        employees.sort(Comparator.comparing(Employee::getName));
    }

    private void generateMonth(){

    }

    private void generateWeek(){

    }

    private void generateDay(){

    }
    @Override
    public void update() {

    }
}
