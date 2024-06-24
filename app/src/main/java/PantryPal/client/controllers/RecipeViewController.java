package PantryPal.client.controllers;

import PantryPal.client.models.BasicHTTP;
import PantryPal.client.views.RecipeView;
import javafx.event.ActionEvent;

public class RecipeViewController{
   
    private SceneController sceneManager;

    String title, description, mealtype, uuid;
    RecipeView view;

    /**
     * Constructor for the RecipeViewController, assigns the refrence to the scene controller
     * and the recipe view
     * @param sceneManager The app's scene controller
     * @param view The recipe view scene
     */
    public RecipeViewController(SceneController sceneManager, RecipeView view){
        this.sceneManager = sceneManager;
        this.view = view;

        view.setSaveButtonAction(this::handleSaveButton);
        view.setBackButtonAction(this::handleBackButton);
        view.setDeleteButtonAction(this::handleDeleteButton);
        view.setEditButtonAction(this::handleEditButton);
    }

    /**
     * Sets the recipe's information in recipe view
     * @param title
     * @param description
     * @param mealtype
     * @param uuid
     */
    public void setRecipe(String title, String description, String mealtype, String uuid) {
        this.title = title;
        this.description = description;
        this.mealtype = mealtype;
        this.uuid = uuid;
        this.view.setRecipe(title, description);
        this.view.setEditable(false);
    }


    //Action Listener for Buttons

    /**
     * Action handler for the save button to put recipe info into server
     * @param event
     */
    public void handleSaveButton(ActionEvent event) {
        //switch scene to recipe list
        BasicHTTP.performRequest("PUT", null, view.getTitleText(),
            mealtype,
            view.getDescriptionText(),
            uuid);
        sceneManager.setRecipeListStage();
    }

    /**
     * Action handler for the back button to switch scene back to recipeList scene
     * @param event
     */
    public void handleBackButton(ActionEvent event) {
        sceneManager.setRecipeListStage();
    }

    /**
     * Action handler for the delete button to remove recipe from server
     * @param event
     */
    public void handleDeleteButton(ActionEvent event) {
        // make HTTP request for deletion, providing the UUID
        BasicHTTP.performRequest("DELETE", uuid, null, null, null, null);
        sceneManager.setRecipeListStage();
    }

    /**
     * Action handler for edit button to toggle title field and description field's editability
     * @param event
     */
    public void handleEditButton(ActionEvent event) {
        view.setEditable(!view.isEditable());
    }
}
