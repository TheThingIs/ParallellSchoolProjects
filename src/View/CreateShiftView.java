package View;

import Controller.AdminController;
import Model.*;
import Model.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Moa Berglund, Victor Cousin
 * Creation of workshifts is done through this view
 * @since 2020-10-07
 */
public class CreateShiftView extends AnchorPane implements Observer {

    @FXML ComboBox departmentComboBox;
    @FXML DatePicker datePicker;
    @FXML TextField hour1,hour2,min1,min2;
    @FXML Spinner numberPersonel;
    @FXML CheckBox monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    @FXML Button saveButton, addCertificate,discardCertificateButton,saveCertificateButton;
    @FXML AnchorPane ListOfCertificatesAnchorPane,StartPage;
    @FXML ListView<CertificateObject> listOfCertificates;
    @FXML Label warningCreateWorkshift;
    private List<Certificate> certificates= new ArrayList<>();
    private List<CertificateObject>certificateObjects= new ArrayList<>();
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Admin.getInstance().getEmployeeListSize()+100,1,1);




    public CreateShiftView() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateShiftView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (Exception e){
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

    private  void generateTextFields(TextField tf){

        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > 2) {
                    String s = tf.getText().substring(0, 2);
                    tf.setText(s);
                }
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
    private void generateComboBox(List<Department> departmentList){
        for(Department d: departmentList){
            departmentComboBox.getItems().add(d.getName());
        }
    }
    private void generateButtons(){

        addCertificate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                ListOfCertificatesAnchorPane.toFront();
                for(CertificateObject certificateObject: certificateObjects){
                    certificateObject.checked.setSelected(true);
                }
            }
        });
        saveCertificateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                certificates.clear(); certificateObjects.clear();
                for (CertificateObject certificateObject: listOfCertificates.getItems()){
                    if (certificateObject.checked.isSelected())
                        certificates.add(certificateObject.certificate);
                        certificateObjects.add(certificateObject);
                }
                ListOfCertificatesAnchorPane.toBack();
            }
        });
        discardCertificateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                ListOfCertificatesAnchorPane.toBack();
            }
        });

    }
    private void loadCertificates() {
        listOfCertificates.getItems().clear();
        Iterator<Certificate> certificateIterator = Admin.getInstance().getCertificatehandler().getAllCertificates();
        while (certificateIterator.hasNext()) {
            CertificateObject tmp = new CertificateObject(certificateIterator.next());
            listOfCertificates.getItems().add(tmp);
        }
    }


    public List<Certificate> getCertificates(){return certificates;}

    public void save(){

            LocalDate localDate = datePicker.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            long workStart= date.getTime()+ ((Long.parseLong(min1.getText()))*1000*60) + ((Long.parseLong(hour1.getText()))*1000*60*60); //TODO weekhandlder
            long workStop= date.getTime()+ ((Long.parseLong(min2.getText()))*1000*60) + ((Long.parseLong(hour2.getText()))*1000*60*60);
            Department d= Admin.getInstance().getDepartmentByName(departmentComboBox.getValue().toString());
            boolean repeat []={sunday.isSelected(),monday.isSelected(),tuesday.isSelected(),wednesday.isSelected(),thursday.isSelected(),friday.isSelected(),saturday.isSelected()};
            for(int i = 0; i<Integer.parseInt(numberPersonel.getEditor().getText());i++){
                Admin.getInstance().createWorkshift(d,workStart,workStop,certificates,repeat);
            }
        }




    @Override
    public void update() {

    }
}
