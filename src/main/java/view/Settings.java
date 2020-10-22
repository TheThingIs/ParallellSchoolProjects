package main.java.view;

import main.java.model.Admin;
import main.java.model.BreakHandler;
import main.java.model.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * @author Moa Berglund, Victor Cousin
 * Settings view for the application
 * @since 2020-10-16
 */

public class Settings extends AnchorPane implements Observer {

    @FXML private TextField guarantedFreetime, minBreak, midBreak, maxBreak;
   @FXML private Button saveButton;


    public Settings(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
       generateSave();
        setDefaultValues();
        generateTextFields(minBreak);
        generateTextFields(midBreak);
        generateTextFields(maxBreak);
        generateTextFields(guarantedFreetime);

    }

    private void setDefaultValues(){
        guarantedFreetime.clear();
        midBreak.clear();
        maxBreak.clear();
        minBreak.clear();
        guarantedFreetime.setText(Long.toString(Admin.getInstance().getHoursOfGuaranteedFreeTime()));
        midBreak.setText(Long.toString(BreakHandler.getInstance().getMinutesOfMidBreakLength()));
        minBreak.setText(Long.toString(BreakHandler.getInstance().getMinutesOfMinBreakLength()));
        maxBreak.setText(Long.toString(BreakHandler.getInstance().getMinutesOfMaxBreakLength()));
    }


   private void generateSave(){
     saveButton.setOnAction(actionEvent -> {
         Admin.getInstance().setGuaranteedFreeTime(Integer.parseInt(guarantedFreetime.getText()));
         BreakHandler.getInstance().setMinBreakLength(Integer.parseInt(minBreak.getText()));
         BreakHandler.getInstance().setMidBreakLength(Integer.parseInt(midBreak.getText()));
         BreakHandler.getInstance().setMaxBreakLength(Integer.parseInt(maxBreak.getText()));
     });
    }

    private  void generateTextFields(TextField tf){

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
    @Override
    public void update() {
            setDefaultValues();
    }
}
