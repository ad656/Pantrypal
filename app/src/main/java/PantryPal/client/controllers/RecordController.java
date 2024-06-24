package PantryPal.client.controllers;

import PantryPal.client.views.RecordView;

import java.util.UUID;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import PantryPal.client.models.RecordingManager;
import PantryPal.client.models.BasicHTTP;

public class RecordController {

    private RecordingManager recordingManager;
    private RecordView view;
    private SceneController sceneManager;
    
    private String savedMealType;

    /**
     * The RecordController constructor that sets the SceneController and RecordView instances
     * @param sceneManager
     * @param view
     */
    public RecordController(SceneController sceneManager, RecordView view) {

        this.sceneManager = sceneManager;
        this.recordingManager = new RecordingManager();
        this.view = view;

        view.setBackButtonAction(this::handleBackButton);

        view.getChildren().add(view.getPrompt());
        setPromptingMode();
        setMealtypeScreen(false);
    }

    /**
     * Sets handlers for the Mealtype prompting screen.
     * @param err Whether or not to show the error prompt screen.
     */
    public void setMealtypeScreen(Boolean err){
        if(err){
            setPrompt("We didn't get a valid meal type!\nPlease specify breakfast, lunch, dinner.");
        } else {
            setPrompt("what is your mealtype?");
        }

        view.setRecordButtonAction(this::handleRecordingButton);    // record button
        view.setStopButtonAction(this::handleMealTypeStopButton);   // stop button for mealType

    }

    /**
     * Listener's function call, to be bound to the "STOP" button during the MealType recording.
     */
    public void stopMealtypeRecording(){
        try{
            // stop the device from recording
            this.recordingManager.stopRecording();

            // send HTTP request to backend to analyze the current recording.
            String mealType = BasicHTTP.performRecordingRequest("mealtype");
            
            // if we didn't get a valid mealtype, go back to the same screen (but with an error).
            // if we did get a valid mealtype, advance to the next screen (prompting for ingredients).
            if (!mealType.isEmpty()) {
                this.savedMealType = mealType;
                setIngredientsScreen();
                return;
            }

            setMealtypeScreen(true);
            return;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setMealtypeScreen(true);
    }

    /**
     * Listeners + Setup for the Ingredients Prompting screen.
     */
    public void setIngredientsScreen(){
        setPrompt("What ingredients do you have? Please list them out as-is, ex: 'bacon, eggs, ham'");

        view.setRecordButtonAction(this::handleRecordingButton);    // record button
        view.setStopButtonAction(this::handleIngredientStopButton); // stop button for ingredient

    }

    /**
     * Action to take after we press "stop" during the ingredients prompting screen.
     */
    public void stopIngredientsRecording(){
        try{
            this.recordingManager.stopRecording();

            String generatedRecipe = BasicHTTP.performRecordingRequest(String.format("ingredients,%s", this.savedMealType));

            JSONObject recipeJSON = new JSONObject(generatedRecipe);

            // go into the next scene to display the generated recipe.
            this.sceneManager.setRecipeViewStage(
                recipeJSON.getString("title"),
                recipeJSON.getString("recipe"),
                recipeJSON.getString("mealtype"),
                UUID.randomUUID().toString()
            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setMealtypeScreen(false);
    }


    /**
     * Shows stopbutton, hides the rest. 
     */
    public void setRecordingMode(){
        view.getChildren().remove(view.getBackButton());
        view.getChildren().remove(view.getRecordButton());
        view.getChildren().add(view.getStopButton());
    }

    /**
     * Shows startbutton + backbutton, hides the rest. 
     */
    public void setPromptingMode(){
        view.getChildren().add(view.getRecordButton());
        view.getChildren().add(view.getBackButton());
        view.getChildren().remove(view.getStopButton());
    }

    /**
     * Starts recording.
     */
    public void recording(){
        this.recordingManager.startRecording();
    }

    /**
     * Sets the text for the prompt 
     * @param promptString
     */
    public void setPrompt(String promptString) {
        view.getPrompt().setText(promptString);
    }

    /**
     * Action handler for backp button to switch to RecipeList scene
     * @param event
     */
    public void handleBackButton(ActionEvent event) {
        sceneManager.setRecipeListStage();
    }

    /**
     * Action handler for recording button to begin recording
     * @param event
     */
    public void handleRecordingButton(ActionEvent event) {
        recording();
        setRecordingMode();
    }
    
     /**
     * Action handler for mealtype stop button to stop recording
     * @param event
     */
    public void handleMealTypeStopButton(ActionEvent event) {
        stopMealtypeRecording();
        setPromptingMode();
    }

    /**
     * Action handler for ingredient stop button to stop recording
     * @param event
     */
    public void handleIngredientStopButton(ActionEvent event) {
        stopIngredientsRecording();
        setPromptingMode();
    }
}
