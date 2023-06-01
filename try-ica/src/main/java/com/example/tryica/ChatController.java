package com.example.tryica;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChatController implements Initializable {
    @FXML
    private Label fileLabel;
    @FXML
    private Button openFileButton;
    @FXML
    private VBox errorContainer;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private HBox buttonContainer;
    @FXML
    private Button okButton;
    @FXML
    private TextFlow textFlow;

//    @FXML
//    private Label errorLabel; // label to display error messages
    @FXML
    private Image smileyImage;
    private Image sadImage;
    private File lastOpenDir;

    public ChatController(){
        // Default constructor
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lastOpenDir = null;
        errorMessageLabel.setText(""); // Initialise error label with empty text
        // Load smiley images
        smileyImage = new Image(getClass().getResourceAsStream("pictures/smile_happy.gif"));
        sadImage = new Image(getClass().getResourceAsStream("pictures/smile_sad.gif"));

        errorContainer.setVisible(false);
        buttonContainer.setVisible(true); // Make buttonContainer visible
        errorMessageLabel.setVisible(true); // Make errorMessageLabel visible
        okButton.setVisible(false);
    }

   @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Chat File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Chat Files", "*.msg"));
        if (lastOpenDir != null) {
            fileChooser.setInitialDirectory(lastOpenDir);
        }
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
                processFile(selectedFile);
                lastOpenDir = selectedFile.getParentFile();
                fileLabel.setText("File: " + selectedFile.getAbsolutePath());
        } else {
            displayErrorMessage("No file selected");
        }
    }

    private void displayErrorMessage(String message) {
        errorMessageLabel.setText(message);
        errorContainer.setVisible(true);
        openFileButton.setVisible(true);
        okButton.setVisible(true);
    }
    private void processFile(File file) {
        textFlow.getChildren().clear();
        errorMessageLabel.setText(""); // Clear the error message

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String previousNickname = null;
            //Track the line number for error reporting
            int lineCounter = 1;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                // Process message elements
                String timestamp = line.substring(6); // Extract timestamp

                // Extract and validate nickname
                String nicknameLine = reader.readLine();
                if (!nicknameLine.startsWith("Name")) {
                    // print error message if syntax is wrong
                    showError("Invalid nickname format at line " + lineCounter);
                    //stop processing the file
                    return;
                }
                String nickname = nicknameLine.substring(5);

                // Extract and validate message
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
                //Increment line counter for each message element
                lineCounter += 3;
            }

            // Check if there is an extra empty line at the end
            String lastLine = reader.readLine();
            if (lastLine != null && !lastLine.trim().isEmpty()){
                showError("Invalid file format: Extra line after last message");
                return; // Stop processing file
            }
        } catch (IOException e) {
            showError("Error reading the file: " + e.getMessage());
        }
    }

    private void formatTimestamp(String timestamp) {
        Text timestampText = new Text("[" + timestamp + "] ");
        timestampText.setFill(Color.BLACK);
        timestampText.setFont(Font.font("System", 12));

        textFlow.getChildren().add(timestampText);
    }

    private void formatNickname(String nickname, String previousNickname) {
        Text nicknameText = new Text(nickname + ": ");
        nicknameText.setFont(Font.font("System", 12));
        if (previousNickname != null && nickname.equals(previousNickname)) {
            nicknameText.setText("...: ");
        } else {
            nicknameText.setFill(Color.BLUE);
        }

        textFlow.getChildren().add(nicknameText);
    }

    private void formatContent(String content) {
        TextFlow contentTextFlow = new TextFlow();

        String[] words = content.split(" ");
        for (String word : words){
            if (word.equals(":)")) {
                ImageView smileyImageView = new ImageView(smileyImage);
                smileyImageView.setFitHeight(16);
                smileyImageView.setFitWidth(16);
                contentTextFlow.getChildren().add(smileyImageView);
            } else if (word.equals(":(")){
                ImageView sadImageView = new ImageView(sadImage);
                sadImageView.setFitHeight(16);
                sadImageView.setFitWidth(16);
                contentTextFlow.getChildren().add(sadImageView);
            } else {
                Text wordText = new Text(word + " ");
                wordText.setFont(Font.font("System", FontWeight.BOLD, 12));
                wordText.setFill(Color.BLACK);
                contentTextFlow.getChildren().add(wordText);
            }
        }
        textFlow.getChildren().add(contentTextFlow);
        textFlow.getChildren().add(new Text("\n")); // Add line break
    }

    private void showError(String message) {
        errorMessageLabel.setText(message); // Update the error label with the error message
    }
}
