package main.java.view;

import main.java.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import main.java.model.Observer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Moa Berglund, Victor Cousin
 * Creation of workshifts is done through this view
 * @since 2020-10-13
 */
public class CreateShiftView extends AnchorPane implements Observer {

    @FXML
    protected ComboBox departmentComboBox;
    @FXML
    protected DatePicker datePicker;
    @FXML
    protected TextField hour1, hour2, min1, min2;
    @FXML
    private Spinner numberPersonel;
    @FXML
    private CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    @FXML
    private Button saveButton, addCertificate, discardCertificateButton, saveCertificateButton;
    @FXML
    private AnchorPane ListOfCertificatesAnchorPane, StartPage;
    @FXML
    private ListView<CertificateObject> listOfCertificates;
    @FXML
    protected Label warningCreateWorkshift;
    private final List<Certificate> certificates = new ArrayList<>();
    private final List<CertificateObject> certificateObjects = new ArrayList<>();
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Admin.getInstance().getEmployeeListSize() + 100, 1, 1);


    public CreateShiftView() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateShiftView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        numberPersonel.setValueFactory(valueFactory);
        generateTextFields(hour1);
        generateTextFields(hour2);
        generateTextFields(min1);
        generateTextFields(min2);
        generateComboBox(Admin.getInstance().getDepartments());
        loadCertificates();
        generateButtons();

        Admin.getInstance().addObserver(this);
    }

    private void generateTextFields(TextField tf) {

        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > 2) {
                String s = tf.getText().substring(0, 2);
                tf.setText(s);
            }
        });

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }

        });
    }

    private void generateComboBox(List<Department> departmentList) {
        for (Department d : departmentList) {
            departmentComboBox.getItems().add(d.getName());
        }
    }

    private void generateButtons() {

        addCertificate.setOnAction(actionEvent -> {
            ListOfCertificatesAnchorPane.toFront();
            for (CertificateObject certificateObject : certificateObjects) {
                certificateObject.checked.setSelected(true);
            }
        });
        saveCertificateButton.setOnAction(actionEvent -> {
            certificates.clear();
            certificateObjects.clear();
            for (CertificateObject certificateObject : listOfCertificates.getItems()) {
                if (certificateObject.checked.isSelected())
                    certificates.add(certificateObject.certificate);
                certificateObjects.add(certificateObject);
            }
            ListOfCertificatesAnchorPane.toBack();
        });
        discardCertificateButton.setOnAction(actionEvent -> ListOfCertificatesAnchorPane.toBack());

    }

    private void loadCertificates() {
        listOfCertificates.getItems().clear();
        Iterator<Certificate> certificateIterator = Admin.getInstance().getCertificatehandler().getAllCertificates();
        while (certificateIterator.hasNext()) {
            CertificateObject tmp = new CertificateObject(certificateIterator.next());
            listOfCertificates.getItems().add(tmp);
        }
    }


    public void save() {

        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        long workStart = date.getTime() + WeekHandler.plusMinutes(Integer.parseInt(min1.getText())) + WeekHandler.plusHours(Integer.parseInt(hour1.getText()));
        long workStop = date.getTime() + WeekHandler.plusMinutes(Integer.parseInt(min2.getText())) + WeekHandler.plusHours(Integer.parseInt(hour2.getText()));
        Department d = Admin.getInstance().getDepartmentByName(departmentComboBox.getValue().toString());
        boolean[] repeat = {sunday.isSelected(), monday.isSelected(), tuesday.isSelected(), wednesday.isSelected(), thursday.isSelected(), friday.isSelected(), saturday.isSelected()};
        for (int i = 0; i < Integer.parseInt(numberPersonel.getEditor().getText()); i++) {
            Admin.getInstance().createWorkshift(d, workStart, workStop, certificates, repeat);
        }
    }


    @Override
    public void update() {

    }
}
