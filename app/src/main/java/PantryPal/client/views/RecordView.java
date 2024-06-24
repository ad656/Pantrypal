package PantryPal.client.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RecordView extends VBox {

    private Button recordButton;
    private Button backButton;
    private Button stopButton;
    private Label prompt;
    

    // Constructor for recordView that stores the record/stop buttons and prompt
    public RecordView() {
        this.setSpacing(10);
        this.setPrefSize(500, 500); 
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setAlignment(Pos.CENTER);

        prompt = new Label("What is your mealtype?");
        // Record Button
        recordButton = new DefaultButton("Record");
        // Back Button
        backButton = new DefaultButton("Back");
        // Stop button
        stopButton = new DefaultButton("Stop");
    }

    public void setBackButtonAction(EventHandler<ActionEvent> eventHandler) {
        backButton.setOnAction(eventHandler);
    }
    public void setRecordButtonAction(EventHandler<ActionEvent> eventHandler) {
        recordButton.setOnAction(eventHandler);
    }
    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopButton.setOnAction(eventHandler);
    }

    // Getters
    public Button getRecordButton() {
        return this.recordButton;
    }

    public Button getBackButton() {
        return this.backButton;
    }

    public Button getStopButton(){
        return this.stopButton;
    }

    public Label getPrompt(){
        return this.prompt;
    }
}
