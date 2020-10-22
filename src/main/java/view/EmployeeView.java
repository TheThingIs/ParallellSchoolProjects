package main.java.view;

import main.java.model.Admin;
import main.java.model.Employee;
import main.java.model.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * @author Oliver Andersson
 * Information on an employee. Is viewed in a ListView
 * @since 2020-10-07
 */
public class EmployeeView extends AnchorPane implements Observer {

    Employee employee;
    @FXML
    Label name, personalID;

    public EmployeeView(Employee employee) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EmployeeView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.employee = employee;
        name.setText(employee.getName());
        personalID.setText(employee.getPersonalId());
        Admin.getInstance().addObserver(this);
    }

    @Override
    public void update() {
        name.setText(employee.getName());
        personalID.setText(employee.getPersonalId());
    }
}