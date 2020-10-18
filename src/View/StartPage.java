package View;

import Controller.AdminController;
import Model.Admin;
import Model.Employee;
import Model.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Oliver Andersson
 * Root node in the view, everything is build upon this
 * @since 2020-10-07
 */
public class StartPage implements Observer, Initializable {
    @FXML
    AnchorPane backGround;
    @FXML
    AnchorPane startPage;
    @FXML
    AnchorPane defaultPage;
    @FXML
    AnchorPane loginPage;
    @FXML
    TextField userNameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton;
    @FXML
    Text falseDetails;
    @FXML
    Button buttonNewFile;
    @FXML
    Button buttonSaveAndExit; //TODO Implement load and save functionality
    @FXML
    Button buttonLoadFile; //TODO Implement load and save functionality
    @FXML
    Tab tabSchedule;
    @FXML
    Tab tabEmployees;
    @FXML
    Tab tabSettings;
    @FXML
    Tab tabDepartments;
    @FXML
    Tab tabCertificates;
    @FXML
    AnchorPane tabEmployeesPane;
    @FXML
    AnchorPane tabDepartmentsPane;
    @FXML
    TabPane tabPane;
    private Admin admin;
    private Settings s = new Settings();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtons();
        admin = Admin.getInstance();
        setTabs();
        startPage.setVisible(false);
    }

    private void setTabs() {
        Schema schema = new Schema();
        tabSchedule.setContent(schema);
        PersonList personList = new PersonList();
        DepartmentList departmentList = new DepartmentList(admin.getDepartments());
        tabEmployees.setContent(personList);
        tabEmployeesPane.getChildren().clear();
        tabEmployeesPane.getChildren().add(personList);
        tabCertificates.setContent(new CertificateList());
        tabDepartmentsPane.getChildren().clear();
        tabDepartmentsPane.getChildren().add(departmentList);
        tabSettings.setContent(s);
    }

    private void setButtons() {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                checklogin();
            }
        });
        buttonSaveAndExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                saveAndExit();
            }
        });
        buttonNewFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                startNewFile();
            }
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        s.update();
                    }
                }
        );
    }

    private void checklogin() {
        if(admin.isLoginInformationCorrect(userNameField.getText(), passwordField.getText())){
            loginPage.setDisable(true);
            loginPage.setVisible(false);
            loginPage.toBack();
        } else {
            falseDetails.setText("Username or password not correct");
            passwordField.setText("");
            userNameField.setText("");
        }
    }

    private void startNewFile() {
        startPage.toFront();
        defaultPage.toBack();
        defaultPage.setVisible(false);
        startPage.setVisible(true);
    }

    private void exit() {
        System.exit(0);
    }

    private void save() {
        //TODO implement
    }

    private void saveAndExit() {
        save();
        exit();
    }

    @Override
    public void update() {

    }
}
