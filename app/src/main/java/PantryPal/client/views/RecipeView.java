package PantryPal.client.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RecipeView extends VBox {
    private TextArea titleText;
    private TextArea descriptionText;

    private Button saveButton;
    private Button backButton;
    private Button deleteButton;
    private Button editButton;
    
    /**
     * This will set and assign all of the various UI elements for viewing a single recipe.2px
     * Elements that are intialized are the recipe's title, its description, and buttons
     * to communicate modifications to the back end.
     */
    public RecipeView(){
        this.setPrefSize(500, 500); 
        this.setStyle("-fx-background-color: #F0F8FF;");

        // Recipe Title
        titleText = new TextArea("RECIPE TITLE");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        titleText.setPrefHeight(40);
        titleText.setEditable(false);        
        titleText.setWrapText(true);


        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the LEFT 

        // Recipe Discription
        descriptionText = new TextArea("RECIPE DESCRIPTION");

        descriptionText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        descriptionText.setEditable(false);
        descriptionText.setWrapText(true);
        this.getChildren().add(descriptionText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center 
        
        //Save Button
        saveButton = new DefaultButton("Save");

        //Back Button
        backButton = new DefaultButton("Back");

        //Delete Button
        deleteButton = new DefaultButton("Delete");

        //Edit button
        editButton = new DefaultButton("Edit");


        HBox hbox = new HBox();
        hbox.getChildren().addAll(saveButton, backButton, deleteButton, editButton);
        hbox.setAlignment(Pos.CENTER);
        this.getChildren().add(hbox);
    }
    
     /*
     * Getters and Setters
     */

    public void setRecipe(String title, String description) {
        titleText.setText(title);
        descriptionText.setText(description);
    }


    //Getter for save button
    public Button getSaveButton(){
        return this.saveButton;
    }

    //Getter for back button
    public Button getBackButton(){
        return this.backButton;
    }

    //Getter for delete button
    public Button getDeleteButton(){
        return this.deleteButton;
    }

    public Button getEditButton(){
        return this.editButton;
    }

    public String getTitleText(){
        return this.titleText.getText();
    }

    public String getDescriptionText(){
        return this.descriptionText.getText();
    }

    public TextArea getTitleTextArea(){
        return this.titleText;
    }

    public TextArea getDescriptionTextArea(){
        return this.descriptionText;
    }

    public void setEditable(Boolean editable){
        titleText.setEditable(editable);
        descriptionText.setEditable(editable);
    }

    public Boolean isEditable(){
        return (titleText.isEditable() || descriptionText.isEditable());
    }

    //Action Listeners for Buttons
    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler) {
        saveButton.setOnAction(eventHandler);
    }
    public void setBackButtonAction(EventHandler<ActionEvent> eventHandler) {
        backButton.setOnAction(eventHandler);
    }
    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
        deleteButton.setOnAction(eventHandler);
    }
    public void setEditButtonAction(EventHandler<ActionEvent> eventHandler) {
        editButton.setOnAction(eventHandler);
    }
}
