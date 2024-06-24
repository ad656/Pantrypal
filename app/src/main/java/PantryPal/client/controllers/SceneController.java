package PantryPal.client.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import PantryPal.client.views.*;

public class SceneController {

    Stage activeStage;
    
    //List of Controllers
    RecipeListController recipeListController;
    RecordController recordController;
    RecipeViewController recipeController;

    RecipeView recipeView;
    RecordView recordView;
    RecipeListView recipeListView;

    Scene recordScene;
    Scene recipeScene;
    Scene recipeListScene;

    /**
     * Constructor for SceneManager.
     * @param token OpenAI API token
     */
    public SceneController(Stage stage, String fp){ 
        activeStage = stage;

        // initialize views, controllers, and the scenes.
        recordView = new RecordView();
        recordController = new RecordController(this, recordView);
        recordScene = new Scene(recordView);

        recipeView = new RecipeView();
        recipeController = new RecipeViewController(this, recipeView);
        recipeScene = new Scene(recipeView);

        recipeListView = new RecipeListView();
        recipeListController = new RecipeListController(this, recipeListView);
        recipeListScene = new Scene(recipeListView);
        
        // we want to start on the recipelistscene (main scene).
        activeStage.setScene(recipeListScene);
    }

    // set activeStage
    public void setPrimaryStage(Stage primaryStage){
        activeStage = primaryStage;
    }

    // get activeStage
    public Stage getPrimaryStage(){
        return this.activeStage;
    }

    /**
     * Switches to RecipeView scene with the parameters specified
     * @param title title of recipe
     * @param description description of recipe
     * @param mealtype mealtype of recipe
     * @param uuid uuid of recipe
     */
    public void setRecipeViewStage(String title, String description, String mealtype, String uuid){
        recipeController.setRecipe(title, description, mealtype, uuid);
        activeStage.setScene(recipeScene);
    }

    /**
     * Sets scene to recordScene
     */
    public void setRecordStage(){
        activeStage.setScene(recordScene);
    }

    /**
     * Sets scene to RecipeListScene
     */
    public void setRecipeListStage(){
        recipeListController.updateRecipeList();
        activeStage.setScene(recipeListScene);
    }
}



