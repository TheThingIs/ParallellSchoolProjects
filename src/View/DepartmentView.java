package View;


import Model.Admin;
import Model.Department;
import Model.Employee;
import Model.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * @author Victor Cousin and Moa Berglund
 * Displays all departments with their name and choosen color
 * @since 2020-10-09
 */
public class DepartmentView extends AnchorPane implements Observer {
    Department department;
    @FXML Label name;
    @FXML Pane departmentColor;

    public DepartmentView(Department department) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DepartmentView.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            this.department = department;
            name.setText(department.getName());
            Color color=department.getColor();
            BackgroundFill backgroundFill=new BackgroundFill(color,new CornerRadii(5.0), new Insets(-5));
            departmentColor.setBackground(new Background(backgroundFill));
            Admin.getInstance().addObserver(this);
    }

    @Override
    public void update() {
        name.setText(department.getName());
        BackgroundFill backgroundFill= new BackgroundFill(department.getColor(), new CornerRadii(5.0), new Insets(-5));
        this.departmentColor.setBackground(new Background(backgroundFill));
    }
}