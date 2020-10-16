package View;

import Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

/**
 * @author Moa och Victor
 * View for creating and editing a department
 * @since 2020-10-09
 */

public class DetailDepartmentView extends AnchorPane implements Observer {
    Department department;

    @FXML javafx.scene.control.TextField name;
    @FXML Spinner minPersonsOnShift;
    @FXML Button saveChanges, deleteDepartment;
    @FXML ColorPicker colorPicker;
    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Admin.getInstance().getEmployeeListSize()+100,1,1);


    public DetailDepartmentView(Department department) {
        this.department = department;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DetailDepartmentView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        minPersonsOnShift.setValueFactory(valueFactory);
        generateButtons();
        generateFXMLObjects();
        Admin.getInstance().addObserver(this);
    }

    public DetailDepartmentView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DetailDepartmentView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        minPersonsOnShift.setValueFactory(valueFactory);

        generateButtons();
        generateFXMLObjects();
        Admin.getInstance().addObserver(this);
    }



    private void generateButtons(){
        saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (department == null) {
                    Admin.getInstance().createNewDepartment(name.getText(), Integer.parseInt(minPersonsOnShift.getEditor().getText()),colorPicker.getValue());
                    department= Admin.getInstance().getDepartmentByName(name.getText());
                }
                else{
                    Admin.getInstance().changeDepartmentName(department, name.getText());
                    Admin.getInstance().getDepartmentByName(name.getText()).setColor(colorPicker.getValue());
                    Admin.getInstance().getDepartmentByName(name.getText()).setMinPersonsOnShift(Integer.parseInt(minPersonsOnShift.getEditor().getText()));//TODO change maxPersonsOnBreak
                }
            }
        });
        deleteDepartment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteAction();
            }
        });

    }

    private void deleteAction(){
        if (department == null) {
            name.setText("");
            minPersonsOnShift.getEditor().setText("");
        }
        else
            Admin.getInstance().removeDepartment(department);
        Admin.getInstance().removeObserver(this);
    }

    private void generateFXMLObjects(){
        if (department != null){
            this.name.setText(department.getName().split(" ")[0]);
            this.minPersonsOnShift.getEditor().setText(String.valueOf(department.getMinPersonsOnShift()));
            this.colorPicker.setValue(department.getColor());
        }
    }

    @Override
    public void update() {
        generateFXMLObjects();
    }
}