package com.patrick;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoConverterUI extends Application {
    private TextField pathField;
    private TextField crf;
    private CheckBox convertSubDirBox;
    private CheckBox deleteCheckbox;
    private ProgressBar progressBar;
    private boolean isDirectory = false;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Convert MKV and WEBM to MP4");

        pathField = new TextField();
        pathField.setPromptText("Select a file or directory...");
        Button selectButton = new Button("Select File");
        Button selectDirectoryButton = new Button("Select Directory");
        convertSubDirBox = new CheckBox("Include Sub-Directories");

        crf = new TextField("17");
        crf.setPromptText("Enter a CRF Value (Default = 17, Lower Values = Higher Quality)");
        Button convertButton = new Button("Start Conversion");
        progressBar = new ProgressBar(0);
        progressBar.setVisible(false);

        // Event listeners for buttons
        selectButton.setOnAction(e -> selectFile());
        selectDirectoryButton.setOnAction(e -> selectDirectory());
        convertButton.setOnAction(e -> performConversion());

        deleteCheckbox = new CheckBox("Delete original files after conversion");

        // Layout
        VBox layout = new VBox();
        layout.getChildren().addAll(pathField, selectButton, selectDirectoryButton, deleteCheckbox, convertSubDirBox, new Label("CRF Value:"), crf, convertButton, progressBar);
        Scene scene = new Scene(layout, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void performConversion() {
        String inputPath = pathField.getText();
        if (inputPath.isEmpty()) {
            showAlert("Error", "Please select a file or directory.");
            return;
        }
        String crfInput = crf.getText();
        int crf;
        try {
            crf = Integer.parseInt(crfInput);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid CRF value. Please enter a valid integer.");
            return;
        }
        boolean convertSubDir = convertSubDirBox.isSelected();
        progressBar.setVisible(true);

        // Create a background task to handle the conversion process
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    if (isDirectory) {
                        // Convert the directory or directory with subdirectories
                        if (convertSubDir) {
                            VideoConverter.convertDirectoryAndSub(inputPath, crf);
                        } else {
                            VideoConverter.convertDirectory(inputPath, crf);
                        }
                    } else {
                        // Convert a single file
                        VideoConverter.convertToMp4(inputPath, crf);
                    }

                    // Delete original file if the checkbox is selected
                    if (deleteCheckbox.isSelected()) {
                        deleteOriginalFile(inputPath);
                    }
                } catch (Exception e) {
                    showAlert("Error", "Error during conversion: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                progressBar.setVisible(false);
                showAlert("Success", "Conversion completed!");
            }

            @Override
            protected void failed() {
                super.failed();
                progressBar.setVisible(false);
                showAlert("Error", "Conversion failed: " + getException().getMessage());
            }
        };

        // Bind progress bar to task progress
        progressBar.progressProperty().bind(task.progressProperty());

        // Start the task in a separate thread
        new Thread(task).start();
    }

    private void deleteOriginalFile(String inputPath) {
        try {
            Files.deleteIfExists(Paths.get(inputPath));
            System.out.println("Deleted original file: " + inputPath);
        } catch (Exception e) {
            System.err.println("Failed to delete original file: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        // Add file extension filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.webm"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            pathField.setText(selectedFile.getPath());
            isDirectory = false;  // This is a file, not a directory
            convertSubDirBox.setDisable(true);  // Disable the subdirectory option for a file
        }
    }

    // Method to let the user select a directory
    private void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");

        // Show the directory chooser dialog
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getPath());
            isDirectory = true;  // This is a directory
            convertSubDirBox.setDisable(false);  // Enable the subdirectory option for a directory
        }
    }
}
