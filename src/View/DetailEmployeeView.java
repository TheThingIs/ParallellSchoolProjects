package View;

import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;

/**
 * @author Oliver Andersson, Moa Berglund, Victor Cousin
 * Interface for editing and viewing information about an employee
 * @since 2020-10-07
 */

public class DetailEmployeeView extends AnchorPane implements Observer {

    Employee employee;

    @FXML DatePicker datePicker, date1, date2;
    @FXML javafx.scene.control.TextField firstName, lastName, personalID, phoneNumber,email;
    @FXML Button saveChanges, deleteEmployee, addCertificate, removeCertificate, createCertificate, discardCertificate, addVacation, registerVacationButton, discardVacationButton;
    @FXML ListView<EmployeeCertificateObject> certificateList;
    @FXML ListView<CertificateObject> availableCertificates;
    @FXML AnchorPane certificatePicker, information, registerVacation;
    @FXML TextField hour1, hour2, min1, min2;
    @FXML Label confirmVacText;

    Certificate selected;


    public DetailEmployeeView(Employee employee) {
        this.employee = employee;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DetailEmployeeView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateFXMLObjects();
        generateButtons();
        generateTextFields(hour1);
        generateTextFields(hour2);
        generateTextFields(min1);
        generateTextFields(min2);
        generatephoneNumber(phoneNumber);
        generateCertificates();
        Admin.getInstance().addObserver(this);
    }

    public DetailEmployeeView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DetailEmployeeView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateButtons();
        generateTextFields(hour1);
        generateTextFields(hour2);
        generateTextFields(min1);
        generateTextFields(min2);
        generatephoneNumber(phoneNumber);
        generateCertificates();
        Admin.getInstance().addObserver(this);
    }

    private void generateCertificates() {
        availableCertificates.getItems().clear();
        Iterator<Certificate> certificateIterator = Admin.getInstance().getCertificatehandler().getAllCertificates();
        while (certificateIterator.hasNext()) {
            CertificateObject tmp = new CertificateObject(certificateIterator.next());
            tmp.checked.setVisible(false);
            tmp.setOnMouseClicked(mouseEvent -> selected = tmp.certificate);
            availableCertificates.getItems().add(tmp);
        }
    }


    private void generateTextFields(TextField tf) {

        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > 2) {
                String s = tf.getText().substring(0, 2);
                tf.setText(s);
            }
        });

        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }

            }
        });
    }
    private void generatephoneNumber(TextField tf){
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }

            }
        });
    }
    


    private void generateButtons() {

        saveChanges.setOnAction(actionEvent -> {
            if (employee == null) {
                Admin.getInstance().createNewEmployee(firstName.getText() + " " + lastName.getText(), personalID.getText(), email.getText(),phoneNumber.getText());
            } else {
                Admin.getInstance().changeEmployeeName(employee, firstName.getText() + " " + lastName.getText());
            }
        });

        registerVacationButton.setOnAction(actionEvent -> {
            LocalDate localDate = date1.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            long vacStart = date.getTime() + WeekHandler.plusMinutes(Integer.parseInt(min1.getText())) + WeekHandler.plusHours(Integer.parseInt(hour1.getText()));//TODO kolla om fungerar
            long vacStop = date.getTime() + WeekHandler.plusMinutes(Integer.parseInt(min1.getText())) + WeekHandler.plusHours(Integer.parseInt(hour1.getText()));
            Admin.getInstance().setVacation(employee, vacStart, vacStop);
            confirmVacText.setVisible(true);
            registerVacation.toBack();
        });

        addVacation.setOnAction(actionEvent -> registerVacation.toFront());

        deleteEmployee.setOnAction(actionEvent -> deleteAction());
        removeCertificate.setOnAction(actionEvent -> {
            for (EmployeeCertificateObject e : certificateList.getItems()) {
                if (e.checked.isSelected()) {
                    Admin.getInstance().removeEmployeeCertificate(e.certificate.getCertificate(), employee);
                    e.checked.setSelected(false);
                }
            }
        });
        addCertificate.setOnAction(actionEvent -> {
            information.toBack();
            certificatePicker.toFront();
        });
        discardCertificate.setOnAction(actionEvent -> {
            information.toFront();
            certificatePicker.toBack();
        });
        discardVacationButton.setOnAction(actionEvent -> {
            information.toFront();
            registerVacation.toBack();
        });
        createCertificate.setOnAction(actionEvent -> {
            LocalDate localDate = datePicker.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            Admin.getInstance().createEmployeeCertificate(selected, employee, date);
            registerVacation.toBack();
            certificatePicker.toBack();
            information.toFront();
        });
    }

    private void deleteAction() {
        if (employee == null) {
            firstName.setText("");
            lastName.setText("");
            personalID.setText("");
            certificateList.getItems().clear();
        } else
            Admin.getInstance().removeEmployee(employee);
        Admin.getInstance().removeObserver(this);
    }

    private void generateFXMLObjects() {
        if (employee != null) {
            this.firstName.setText(employee.getName().split(" ")[0]);
            this.lastName.setText(employee.getName().split(" ")[1]);
            this.personalID.setText(employee.getPersonalId());
            this.certificateList.getItems().clear();
            this.email.setText(employee.getEmail());
            this.phoneNumber.setText(employee.getPhoneNumber());
            for (int i = 0 ; i < employee.getCertificatesSize() ; i++){
                EmployeeCertificate employeeCertificate = employee.getCertificate(i);
                this.certificateList.getItems().add(new EmployeeCertificateObject(employeeCertificate));
            }
            addVacation.setVisible(true);
        }
    }

    @Override
    public void update() {
        generateFXMLObjects();
    }
}
