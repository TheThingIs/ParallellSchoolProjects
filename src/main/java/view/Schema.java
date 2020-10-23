package main.java.view;

import main.java.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import main.java.model.Observer;

import java.time.YearMonth;
import java.util.*;
/**
 * @author Oliver Andersson
 * Visual representation of the OurCalendar in model
 * @since 2020-10-07
 */
public class Schema extends AnchorPane implements Observer {
    @FXML
    private Button next, previous, createWorkshift, discardButtonCreateNewShift,
            saveButtonCreateNewShift, cancelButton, switchButton, removeShiftButton, autoFillButton, removeEmployee;
    @FXML
    private GridPane monthGrid, weekGrid;
    @FXML
    private AnchorPane dayView, monthView, weekView, workshiftPane;
    @FXML
    private ComboBox<String> viewSelector;
    @FXML
    private Label currentFormatInfo;
    @FXML
    private ListView<SchemaWorkshift> listOfWorkshifts;
    @FXML
    private ListView<EmployeeView> listOfAvailableEmployees;

    private int dateIndex;
    private Date currentIndex;
    private String mode = "";
    private WorkShift toBeSwitched;

    public Schema() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Schema.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateDate();
        generateComboBox();
        generateButtons();
        Admin.getInstance().addObserver(this);
    }

    private void generateEmployeePicker(WorkShift workShift) {
        if (toBeSwitched != null) {
            OurCalendar.getInstance().getWorkday(dateIndex).swapOccupation(toBeSwitched, workShift);
            updateDay();
            toBeSwitched = null;
            return;
        }
        listOfAvailableEmployees.getItems().clear();
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < Admin.getInstance().getEmployeeListSize(); i++)
            employees.add(Admin.getInstance().getEmployee(i));
        employees = Admin.getInstance().getEmployeeSorter().getAvailablePersons(workShift.START, workShift.END, employees);
        employees = Admin.getInstance().getEmployeeSorter().getQualifiedPersons(workShift, employees);
        for (Employee e : employees) {
            EmployeeView tmp = new EmployeeView(e);
            tmp.setOnMouseClicked(mouseEvent -> assignEmployeeToWorkshift(workShift, e));
            listOfAvailableEmployees.getItems().add(tmp);
        }
        removeShiftButton.setOnAction(actionEvent -> removeWorkShift(workShift));
        removeEmployee.setOnAction(actionEvent -> removeEmployee(workShift));
        listOfAvailableEmployees.toFront();
        listOfAvailableEmployees.setVisible(true);
        listOfWorkshifts.toBack();
        listOfWorkshifts.setVisible(false);
        switchButton.setOnAction(actionEvent -> {
            toBeSwitched = workShift;
            listOfAvailableEmployees.toBack();
            listOfAvailableEmployees.setVisible(false);
            listOfWorkshifts.toFront();
            listOfWorkshifts.setVisible(true);
        });
    }

    private void removeEmployee(WorkShift workShift) {
        workShift.clearWorkShiftOccupation();
        updateDay();
    }

    private void removeWorkShift(WorkShift workShift) {
        OurCalendar calendar = OurCalendar.getInstance();
        calendar.getWorkday(calendar.getDateIndex(new Date(workShift.START))).removeWorkshift(workShift);
        listOfAvailableEmployees.toBack();
        listOfAvailableEmployees.setVisible(false);
        listOfWorkshifts.toFront();
        updateDay();
    }

    private void assignEmployeeToWorkshift(WorkShift workShift, Employee employee) {
        OurCalendar calendar = OurCalendar.getInstance();
        if (workShift.isOccupied()) {
            calendar.getWorkday(calendar.getDateIndex(new Date(workShift.START))).reOccupieEmployee(workShift, employee);
        } else {
            calendar.getWorkday(calendar.getDateIndex(new Date(workShift.START))).occupiesEmployee(workShift, employee);
        }
        listOfAvailableEmployees.toBack();
        listOfAvailableEmployees.setVisible(false);
        listOfWorkshifts.toFront();
        updateDay();
    }

    private void generateLabels() {
        switch (mode) {
            case "Dag" -> currentFormatInfo.setText(new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).toString());
            case "Vecka" -> currentFormatInfo.setText("Vecka " + YearMonth.of((new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getYear() + 1900), (new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getMonth()) + 1));
            case "Månad" -> currentFormatInfo.setText((new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getYear() + 1900) + "/" + (new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getMonth() + 1));
        }
    }

    private void generateDate() {
        currentIndex = new Date(OurCalendar.getInstance().getWorkday(0).DATE);
        dateIndex = 0;
    }

    private void generateComboBox() {
        viewSelector.getItems().clear();
        viewSelector.getItems().add("Månad");
        viewSelector.getItems().add("Vecka");
        viewSelector.getItems().add("Dag");
        viewSelector.valueProperty().addListener((observableValue, oldValue, newValue) -> viewSelection(newValue));
    }

    private void viewSelection(String mode) {
        this.mode = mode;
        generateLabels();
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE));
        switch (mode) {
            case "Månad" -> {
                dateIndex = dateIndex - (instance.get(Calendar.DAY_OF_MONTH) - 1);
                monthView.toFront();
                monthView.setVisible(true);
                dayView.setVisible(false);
                weekView.setVisible(false);
                workshiftPane.setVisible(false);
                updateMonth();
            }
            case "Vecka" -> {
                dateIndex = dateIndex - (dayConverter(instance.get(Calendar.DAY_OF_WEEK)));
                weekView.toFront();
                weekView.setVisible(true);
                dayView.setVisible(false);
                monthView.setVisible(false);
                workshiftPane.setVisible(false);
                updateWeek();
            }
            case "Dag" -> {
                dayView.toFront();
                dayView.setVisible(true);
                weekView.setVisible(false);
                monthView.setVisible(false);
                workshiftPane.setVisible(false);
                updateDay();
            }
        }
    }

    private void next() {
        if (dateIndex >= 365)
            return;
        switch (mode) {
            case "Dag" -> {
                this.dateIndex++;
                updateDay();
            }
            case "Vecka" -> {
                this.dateIndex += 7;
                updateWeek();
            }
            case "Månad" -> {
                this.dateIndex += YearMonth.of(new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getYear() + 1900, new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getMonth() + 1).lengthOfMonth();
                updateMonth();
            }
        }
        generateLabels();
    }

    private void previous() {
        if (dateIndex <= 0)
            return;
        switch (mode) {
            case "Dag" -> {
                this.dateIndex--;
                updateDay();
            }
            case "Vecka" -> {
                this.dateIndex -= 7;
                updateWeek();
            }
            case "Månad" -> {
                this.dateIndex -= YearMonth.of(new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE).getYear() + 1900, new Date(OurCalendar.getInstance().getWorkday(dateIndex - 1).DATE).getMonth() + 1).lengthOfMonth();
                updateMonth();
            }
        }
        generateLabels();
    }

    private int dayConverter(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> 6;
            case 2 -> 0;
            case 3 -> 1;
            case 4 -> 2;
            case 5 -> 3;
            case 6 -> 4;
            case 7 -> 5;
            default -> 0;
        };
    }

    private void updateMonth() {
        monthGrid.getChildren().clear();
        currentIndex = new Date(OurCalendar.getInstance().getWorkday(dateIndex).DATE);
        int daysInMonth = YearMonth.of(currentIndex.getYear() + 1900, currentIndex.getMonth() + 1).lengthOfMonth();
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(currentIndex);
        int startingDay;
        startingDay = dayConverter(tmp.get(Calendar.DAY_OF_WEEK));
        int anotherIndex = 0, thirdIndex = 0;
        for (int i = startingDay; i < daysInMonth + startingDay; i++) {
            if (i % 7 == 0)
                thirdIndex++;
            DayScheduleViewMonth tmpNode = new DayScheduleViewMonth(OurCalendar.getInstance().getWorkday(dateIndex + anotherIndex));
            tmpNode.setOnMouseClicked(mouseEvent -> {
                dateIndex += tmpNode.getDayOfMonth() - 1;
                viewSelection("Dag");
                viewSelector.setValue("Dag");
            });
            monthGrid.add(tmpNode, i % 7, thirdIndex);
            anotherIndex++;
        }
    }

    private void updateWeek() {
        weekGrid.getChildren().clear();
        for (int i = 0; i < 7; i++) {
            DayScheduleViewWeek tmpNode = new DayScheduleViewWeek(OurCalendar.getInstance().getWorkday(dateIndex + i));
            tmpNode.setOnMouseClicked(mouseEvent -> {
                dateIndex += dayConverter(tmpNode.getDayOfWeek());
                viewSelection("Dag");
                viewSelector.setValue("Dag");
            });
            weekGrid.add(tmpNode, i, 0);
        }
    }

    private void updateDay() {
        listOfWorkshifts.getItems().clear();
        for (Department d : Admin.getInstance().getDepartments()) {
            for (WorkShift w : OurCalendar.getInstance().getWorkday(dateIndex).getWorkShifts(d)) {
                SchemaWorkshift tmp = new SchemaWorkshift(w, d.getColor(), d.getName());
                tmp.setOnMouseClicked(mouseEvent -> generateEmployeePicker(w));
                listOfWorkshifts.getItems().add(tmp);
            }
        }
    }

    private void generateButtons() {
        next.setOnAction(actionEvent -> next());
        previous.setOnAction(actionEvent -> previous());
        discardButtonCreateNewShift.setOnAction(actionEvent -> {
            CreateShiftView createShiftView = (CreateShiftView) workshiftPane.getChildren().get(0);
            createShiftView.warningCreateWorkshift.setVisible(false);
            workshiftPane.setVisible(false);
            workshiftPane.toBack();
            workshiftPane.getChildren().remove(0);
        });
        saveButtonCreateNewShift.setOnAction(actionEvent -> {
            CreateShiftView createShiftView = (CreateShiftView) workshiftPane.getChildren().get(0);
            if ((createShiftView.departmentComboBox.getValue() == null) || (createShiftView.min1.getText().isEmpty()) || (createShiftView.min2.getText().isEmpty()) || (createShiftView.hour1.getText().isEmpty()) || (createShiftView.hour2.getText().isEmpty()) || (createShiftView.datePicker.getValue() == null)) {
                createShiftView.warningCreateWorkshift.setVisible(true);
            } else {
                createShiftView.warningCreateWorkshift.setVisible(false);
                createShiftView.save();
                workshiftPane.setVisible(false);
                workshiftPane.toBack();
                workshiftPane.getChildren().remove(0);
            }
        });
        createWorkshift.setOnAction(actionEvent -> {
            workshiftPane.getChildren().add(new CreateShiftView());
            workshiftPane.setVisible(true);
            workshiftPane.toFront();
            discardButtonCreateNewShift.toFront();
            saveButtonCreateNewShift.toFront();
        });
        cancelButton.setOnAction(actionEvent -> {
            listOfAvailableEmployees.toBack();
            listOfAvailableEmployees.setVisible(false);
            listOfWorkshifts.toFront();
            listOfWorkshifts.setVisible(true);
        });

        autoFillButton.setOnAction(actionEvent -> autoFill(7, dateIndex));
    }

    private void autoFill(int daysAhead, int offset) {
        EmployeeSorter tmp = Admin.getInstance().getEmployeeSorter();
        OurCalendar calendar = OurCalendar.getInstance();
        List<WorkDay> tmpList = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < Admin.getInstance().getEmployeeListSize(); i++)
            employees.add(Admin.getInstance().getEmployee(i));
        for (int i = 0; i < daysAhead; i++) {
            tmpList.add(calendar.getWorkday(+i + offset));
        }
        tmp.sortPotentialWorkShiftCandidate(employees, tmpList);
        tmp.delegateEmployeeToWorkshift();
        updateWeek();
        updateMonth();
        updateDay();
    }

    @Override
    public void update() {
        switch (mode) {
            case "Dag" -> updateDay();
            case "Vecka" -> updateWeek();
            case "Månad" -> updateMonth();
        }
    }
}
