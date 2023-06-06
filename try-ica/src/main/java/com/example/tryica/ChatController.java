//The ChatController class represents the controller in the Model-View-Controller architecture for the chat viewer application.

package com.example.tryica;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChatController implements Initializable {
    @FXML
    private Label fileLabel;
    @FXML
    private TextFlow textFlow;
    @FXML
    private Label errorLabel;

//    The handleOpenFile method is called when the user clicks the "Open File" button.
//    It opens a file chooser dialog, allows the user to select a file,
//    and calls the processFile method to handle the selected file.
    @FXML
    private void handleOpenFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            processFile(file);
            fileLabel.setText("File: " + file.getAbsolutePath());
        }
    }
    private Image smileyImage;
    private Image sadImage;
    public ChatController() {
        //Default constructor
    }

//    This method is called when the FXML file is loaded and initializes the controller.
//    It sets the initial state of the UI components, sets the error label to an empty text, and loads the smiley images.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setText(""); // Initialize error label with empty text
        // Load smiley images
        smileyImage = new Image(getClass().getResourceAsStream("pictures/smile_happy.gif"));
        sadImage = new Image(getClass().getResourceAsStream("pictures/smile_sad.gif"));
    }

//    This method processes the selected file.
//    It clears the text flow, reads the file line by line, and extracts the timestamp, nickname, and content of each chat message.
//    It validates the file format and displays an error if it is invalid
//    It formats the extracted elements and adds them to the textFlow.
    private void processFile(File file) {
        textFlow.getChildren().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String previousNickname = null;
            int lineCounter = 1; // Track the line number for error reporting

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                // Process message elements
                String timestamp = line.substring(6); // Extract timestamp

                // Validate and extract nickname
                String nicknameLine = reader.readLine();
                if (nicknameLine == null || !nicknameLine.startsWith("Name:")) {
                    showError("Invalid file format at line " + lineCounter);
                    return; // Stop processing the file
                }
                String nickname = nicknameLine.substring(5);

                // Validate and extract content
                String contentLine = reader.readLine();
                if (!contentLine.startsWith("Message:")) {
                    showError("Invalid content format at line " + lineCounter);
                    return; // Stop processing the file
                }
                String content = contentLine.substring(8);

                // Format elements and add to TextFlow
                formatTimestamp(timestamp);
                formatNickname(nickname, previousNickname);
                formatContent(content);

                previousNickname = nickname;
                lineCounter += 3; // Increment line counter for each message element
            }

        } catch (IOException e) {
            showError("Error reading the file: " + e.getMessage());
        }
    }


//    This method formats the timestamp of a chat message by adding brackets and sets the style and color of the text.
//    It creates a Text object with the formatted timestamp and adds it to the textFlow.
    private void formatTimestamp(String timestamp) {
        Text timestampText = new Text("[" + timestamp + "] ");
        timestampText.setFill(Color.BLACK);
        timestampText.setFont(Font.font("System", 12));
        textFlow.getChildren().add(timestampText);
    }


//    This method formats the nickname of a chat message. If the nickname is the same as the previous nickname,
//    it adds "..." to indicate a continuation of the previous message. Otherwise, it adds the nickname in brackets.
//    It sets the style and color of the text and adds it to the textFlow.
    private void formatNickname(String nickname, String previousNickname) {
        if (previousNickname != null && nickname.equals(previousNickname)) {
            Text nicknameText = new Text("...");
            nicknameText.setFont(Font.font("System", 12));
            nicknameText.setFill(Color.BLUE);
            textFlow.getChildren().add(nicknameText);
        } else {
            Text nicknameText = new Text("[" + nickname + "]: ");
            nicknameText.setFont(Font.font("System", 12));
            nicknameText.setFill(Color.BLUE);
            textFlow.getChildren().add(nicknameText);
        }
    }

//    This method formats the content of a chat message.
//    It splits the content into words and checks if a word matches a smiley symbol (":)" or ":(").
//    if it matches, it replaces the word with an image of a smiley.Otherwise, it creates a Text object with the word and adds it to the contentTextFlow.
//    The formatted content is then added to the textFlow.
    private void formatContent(String content) {
        TextFlow contentTextFlow = new TextFlow();

        String[] words = content.split(" ");
        for (String word : words) {
            if (word.equals(":)")){ //Convert smiley to image
                ImageView smileyImageView = new ImageView(smileyImage);
                smileyImageView.setFitWidth(16);
                smileyImageView.setFitHeight(16);
                contentTextFlow.getChildren().add(smileyImageView);
            } else if (word.equals(":(")){ //Convert smiley to image
                ImageView sadImageView = new ImageView(sadImage);
                sadImageView.setFitWidth(16);
                sadImageView.setFitHeight(16);
                contentTextFlow.getChildren().add(sadImageView);
            } else {
                Text wordText = new Text(word + " ");
                wordText.setFont(Font.font("System", FontWeight.BOLD, 12));
                wordText.setFill(Color.BLACK);
                contentTextFlow.getChildren().add(wordText);
            }
        }
        textFlow.getChildren().add(contentTextFlow);
        textFlow.getChildren().add(new Text("\n")); //Add line break
    }


//    This method displays an error message in an alert dialog.
//    It creates an Alert of type ERROR with the specified error message, sets the header text to null (to remove the default header),
//    and displays the alert with an "OK" button.
    private void showError(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR, errorMessage, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
