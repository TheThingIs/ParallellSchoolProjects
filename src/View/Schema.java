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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
        Admin.getInstance().addObserver(this);
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
                viewSelection(newValue);
            }
        });
    }

    private void viewSelection(String mode){
        this.mode = mode;
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE));
        switch (mode) {
            case "Månad":
                dateIndex = dateIndex-(instance.get(Calendar.DAY_OF_MONTH)-1);
                monthView.toFront();
                monthView.setVisible(true);
                dayView.setVisible(false);
                weekView.setVisible(false);
                workshiftPane.setVisible(false);
                updateMonth();
                break;
            case "Vecka":
                dateIndex = dateIndex-(dayConverter(instance.get(Calendar.DAY_OF_WEEK)));
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

    private void next(){
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
    private void previous(){
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

    private int dayConverter(int dayOfWeek){
        switch (dayOfWeek){
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 4;
            case 7:
                return 5;
            default: return 0;
        }
    }
    private void updateMonth(){
        currentIndex = new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE);
        int daysInMonth = YearMonth.of(currentIndex.getYear(), currentIndex.getMonth()).lengthOfMonth();
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(currentIndex);
        int startingDay;
        startingDay = dayConverter(tmp.get(Calendar.DAY_OF_WEEK));
        OurCalendar tmpCalendar = OurCalendar.getInstance();
        int anotherIndex = 0, thirdIndex = 0;
        for (int i=startingDay; i<daysInMonth+startingDay; i++){
            if (i%7 == 0)
                thirdIndex++;
            DayScheduleViewMonth tmpNode = new DayScheduleViewMonth(OurCalendar.getInstance().getWorkday(dateIndex+anotherIndex));
            tmpNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    dateIndex += tmpNode.getDayOfMonth()-1;
                    viewSelection("Dag");
                    viewSelector.setValue("Dag");
                }
            });
            monthGrid.add(tmpNode, i%7, thirdIndex);
            anotherIndex++;
        }
    }

    private void updateWeek(){
        for (int i = 0; i<7; i++){
            DayScheduleViewWeek tmpNode = new DayScheduleViewWeek(OurCalendar.getInstance().getWorkday(dateIndex+i));
            tmpNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    dateIndex += dayConverter(tmpNode.getDayOfWeek());
                    viewSelection("Dag");
                    viewSelector.setValue("Dag");
                }
            });
            weekGrid.add(tmpNode, i, 0);
        }
    }
    private void updateDay(){
        //OurCalendar.getInstance().getWorkday(dateIndex).setWorkDay();
        listOfWorkshifts.getItems().clear();
        for (Department d : Admin.getInstance().getDepartments()){
            for (WorkShift w : OurCalendar.getInstance().getWorkday(dateIndex).getWorkShifts(d))
                listOfWorkshifts.getItems().add(new SchemaWorkshift(w, d.getColor(), d.getName()));
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
                CreateShiftView createShiftView = (CreateShiftView) workshiftPane.getChildren().get(0);
                createShiftView.warningCreateWorkshift.setVisible(false);
                workshiftPane.setVisible(false);
                workshiftPane.toBack();
                workshiftPane.getChildren().remove(0);
            }
        });
        saveButtonCreateNewShift.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CreateShiftView createShiftView = (CreateShiftView) workshiftPane.getChildren().get(0);
                if ((createShiftView.departmentComboBox.getValue()==null) || (createShiftView.min1.getText().isEmpty())|| (createShiftView.min2.getText().isEmpty())|| (createShiftView.hour1.getText().isEmpty())|| (createShiftView.hour2.getText().isEmpty()) || (createShiftView.datePicker.getValue()==null)){
                    createShiftView.warningCreateWorkshift.setVisible(true);
                }
                else {
                    createShiftView.warningCreateWorkshift.setVisible(false);
                    createShiftView.save();
                    workshiftPane.setVisible(false);
                    workshiftPane.toBack();
                    workshiftPane.getChildren().remove(0);
                }
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
        switch (mode){
            case "Dag":
                updateDay();
                break;
            case "Vecka":
                updateWeek();
                break;
            case "Månad":
                updateMonth();
                break;
        }
    }
}
