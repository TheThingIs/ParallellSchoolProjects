package main.java.view;

import main.java.model.Admin;
import main.java.model.Department;
import main.java.model.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.*;

/**
 * @author Oliver Andersson
 * EmployeeTab of the program. Root node for "employeetab"
 * @since 2020-10-07
 */
public class DepartmentList extends AnchorPane implements Observer {
    private final Map<Department, DepartmentView> departmentDepartmentViewMap;
    private final List<DepartmentView> departmentViews;
    @FXML
    private ListView<DepartmentView> departmentViewPane;
    @FXML
    private Button buttonCreateDepartment;
    @FXML
    private AnchorPane paneDetailView;

    public DepartmentList(List<Department> departments) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DepartmentList.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.departmentDepartmentViewMap = new HashMap<>();
        this.departmentViews = new ArrayList<>();
        generateDepartmentViews(departments);
        generateButtons();
        Admin.getInstance().addObserver(this);
    }

    private void generateButtons() {
        buttonCreateDepartment.setOnAction(actionEvent -> {
            createDepartment();
        });
    }

    private void createDepartment() {
        paneDetailView.getChildren().clear();
        paneDetailView.getChildren().add(new DetailDepartmentView());
        paneDetailView.setVisible(true);
    }

    private void sortDepartmentsAlphabetically(List<Department> departments) {
        departments.sort(Comparator.comparing(Department::getName));
    }

    private void generateDepartmentViews(List<Department> departments) {
        sortDepartmentsAlphabetically(departments);
        departmentViewPane.getItems().clear();
        for (Department d : departments) {
            if (departmentDepartmentViewMap.get(d) == null) {
                DepartmentView departmentView = new DepartmentView(d);
                departmentDepartmentViewMap.put(d, departmentView);
                departmentViews.add(departmentView);
                departmentView.setOnMouseClicked(mouseEvent -> {
                    paneDetailView.getChildren().clear();
                    paneDetailView.getChildren().add(new DetailDepartmentView(d));
                });
            }
            departmentViewPane.getItems().add(departmentDepartmentViewMap.get(d));
        }
    }

    @Override
    public void update() {
        paneDetailView.setVisible(Admin.getInstance().getDepartments().size() != 0);

        generateDepartmentViews(Admin.getInstance().getDepartments());
    }
}