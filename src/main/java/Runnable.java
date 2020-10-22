package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.model.Admin;

import java.io.File;
import java.net.URL;

/**
 * @author Oliver Andersson
 * main.java.Runnable class for the project
 * @since 2020-10-07
 */
public class Runnable extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Admin.getInstance().createNewUser("Moa", "Hemligt");
        URL url = new File("src/main/java/view/StartPage.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        }));
    }
}
