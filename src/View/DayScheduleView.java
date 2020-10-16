package View;

import Model.WorkDay;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class DayScheduleView extends AnchorPane {
    public DayScheduleView(WorkDay day) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DayScheduleView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if ()
    }
}
