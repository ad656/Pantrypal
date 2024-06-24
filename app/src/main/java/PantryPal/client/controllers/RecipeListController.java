package PantryPal.client.controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

import PantryPal.client.models.BasicHTTP;
import PantryPal.client.views.RecipeListView;
import PantryPal.client.views.RecipeTileView;


public class RecipeListController {
    
    SceneController sceneControllerReference;
    RecipeListView listView;

    /**
     * Constructor for the RecipeListController, assigns the refrence to the scene controller
     * and the recipe list view
     * @param sceneControllerReference The app's scene controller
     * @param listView The list of recipes view object
     */
    public RecipeListController(SceneController sceneControllerReference, RecipeListView listView) {
        this.listView = listView;
        this.sceneControllerReference = sceneControllerReference;

        // Set listeners
        listView.setGenerateButtonAction(this::handleGenerateScreen);
        this.updateRecipeList();
    }

    /**
     * Moves the app from the recipe list scene to the next scene for recipe generation
     * @param event
     */
    public void handleGenerateScreen(ActionEvent event) {
        sceneControllerReference.setRecordStage();
    }

    /**
     * Updates the list of recipes by first recieving the list from the server as a JSON string.
     * Then creates a JSONArray object from that string, then populates each of the buttons in
     * the listView's button list with each recipe.
     */
    public void updateRecipeList() {
        String recipeListString = BasicHTTP.performRequest("GET", null, null, 
            null, null, null);
        
        JSONArray recipes = new JSONObject(recipeListString).getJSONArray("recipes");
        ListView<RecipeTileView> btnList = listView.getBtnList();
        btnList.getItems().clear();
        for(int i = 0; i < recipes.length(); i++) {
            JSONObject recipeJSON = recipes.getJSONObject(i);

            /*
             * Create a new Tile using all of the recipes we've recieved from the backend.
             * Iterate through each JSON and create a tile from these.
             */
            RecipeTileView j = new RecipeTileView(recipeJSON.getString("title"));
            btnList.getItems().add(j);
            j.setTileButtonAction((e) -> {
                sceneControllerReference.setRecipeViewStage(
                    recipeJSON.getString("title"),
                    recipeJSON.getString("recipe"),
                    recipeJSON.getString("mealtype"),
                    recipeJSON.getString("UUID")
                );
            });
        }
        listView.updateBtnList(btnList);
    }

}
