package View;

import Model.Admin;
import Model.BreakHandler;
import Model.Observer;
import Model.OurCalendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;

import java.awt.*;
import java.util.Calendar;

public class Settings extends AnchorPane implements Observer {

    @FXML TextField guarantedFreetime, minBreak, midBreak, maxBreak;
   @FXML Button saveButton;


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
     saveButton.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent actionEvent) {
             Admin.getInstance().setGuaranteedFreeTime(Integer.parseInt(guarantedFreetime.getText()));
             BreakHandler.getInstance().setMinBreakLength(Integer.parseInt(minBreak.getText()));
             BreakHandler.getInstance().setMidBreakLength(Integer.parseInt(midBreak.getText()));
             BreakHandler.getInstance().setMaxBreakLength(Integer.parseInt(maxBreak.getText()));
         }
         });
    }

    private  void generateTextFields(javafx.scene.control.TextField tf){

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
    @Override
    public void update() {
            setDefaultValues();
    }
}
