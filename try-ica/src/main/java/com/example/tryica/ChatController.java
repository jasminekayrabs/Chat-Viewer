package com.example.tryica;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ChatController implements Initializable {
    @FXML
    private Label fileLabel;
    @FXML
    private TextFlow textFlow;
    @FXML
    private Label errorLabel;
    private Image smileyImage;
    private Image sadImage;
    private File lastOpenDir;
    public ChatController() {
        //Default constructor
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lastOpenDir = null;
        errorLabel.setText(""); // Initialize error label with empty text
        
        // Load smiley images
        smileyImage = new Image(getClass().getResourceAsStream("pictures/smile_happy.gif"));
        sadImage = new Image(getClass().getResourceAsStream("pictures/smile_sad.gif"));
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
        }
    }

    private void processFile(File file) {
        textFlow.getChildren().clear();
        errorLabel.setText(""); //Clear error message

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
                if (!nicknameLine.startsWith("Name:")) {
                    showError("Invalid nickname format at line " + lineCounter);
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

    private void formatTimestamp(String timestamp) {
        Text timestampText = new Text("[" + timestamp + "] ");
        timestampText.setFill(Color.BLACK);
        timestampText.setFont(Font.font("System", 12));

        textFlow.getChildren().add(timestampText);
    }

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

    private void showError(String message) {
        errorLabel.setText(message); // Update the error label with the error message
    }
}
