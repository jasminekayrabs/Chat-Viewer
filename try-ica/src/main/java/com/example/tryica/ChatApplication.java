//The ChatApplication class extends the Application class from the JavaFX framework
//and represents the main entry point for the chat viewer application.
// Here's a brief explanation of each method in the class:
//start(Stage primaryStage): This method is called when the application is launched.
//It loads the FXML file ChatViewer.fxml using the FXMLLoader class.
//The loaded file is set as the root node for the UI scene. It sets the title of the primary stage, creates a new scene with the root node and specified dimensions (500x450),
//sets the scene on the primary stage, and shows the primary stage.

//main(String[] args): This is the entry point of the application. It calls the launch method from the Application class,
//which in turn initializes the JavaFX runtime and starts the application.

package com.example.tryica;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ChatApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ChatViewer.fxml")); // Load the FXML file

        primaryStage.setTitle("Chat Viewer");
        primaryStage.setScene(new Scene(root, 500, 450));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
