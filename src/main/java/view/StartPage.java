package main.java.view;

import main.java.model.Admin;
import main.java.model.ImportServicePackage;
import main.java.model.Observer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Oliver Andersson, Christian Lind, Victor Cousin, Moa Berglund
 * Root node in the view, everything is build upon this
 * @since 2020-10-07
 */
public class StartPage implements Initializable {
    @FXML
    private AnchorPane backGround;
    @FXML
    private AnchorPane startPage;
    @FXML
    private AnchorPane defaultPage;
    @FXML
    private AnchorPane loginPage;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Text falseDetails;
    @FXML
    private Button buttonNewFile;
    @FXML
    private Button buttonSaveAndExit;
    @FXML
    private Button buttonLoadFile;
    @FXML
    private Tab tabSchedule;
    @FXML
    private Tab tabEmployees;
    @FXML
    private Tab tabSettings;
    @FXML
    private Tab tabDepartments;
    @FXML
    private Tab tabCertificates;
    @FXML
    private AnchorPane tabEmployeesPane;
    @FXML
    private AnchorPane tabDepartmentsPane;
    @FXML
    private TabPane tabPane;
    private Admin admin;
    private final Settings settings = new Settings();

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
        tabSettings.setContent(settings);
    }

    private void setButtons() {
        loginButton.setOnAction(actionEvent -> checklogin());
        buttonSaveAndExit.setOnAction(actionEvent -> saveAndExit());
        buttonNewFile.setOnAction(actionEvent -> startNewFile());

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> settings.update()
        );
        buttonLoadFile.setOnAction(actionEvent -> loadFile());
    }

    private void checklogin() {
        if (admin.isLoginInformationCorrect(userNameField.getText(), passwordField.getText())) {
            loginPage.setDisable(true);
            loginPage.setVisible(false);
            loginPage.toBack();
        } else {
            falseDetails.setText("Användarnamn eller lösenord är felaktigt");
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

    private void loadFile(){
        ImportServicePackage.loadPackage();
        startPage.toFront();
        defaultPage.toBack();
        defaultPage.setVisible(false);
        startPage.setVisible(true);
    }

    private void saveAndExit() {
        save();
        exit();
    }

}
