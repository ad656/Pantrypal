package PantryPal.client.views;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 *  This Class will Display a list of tasks to the GUI
 */
public class RecipeListView extends VBox {

    private Header header = new Header();

    private Button generateRecipeBtn = header.getGenerateRecipeButton();

    // constructor of the RecipeListView
    public RecipeListView() {
        this.setPrefSize(500, 500);
        this.getChildren().add(header);
        this.getChildren().add(btnList);
    }
    
    ListView<RecipeTileView> btnList = new ListView<RecipeTileView>();
   
    public ListView<RecipeTileView> getBtnList() {
        return this.btnList;
    }

    public void updateBtnList(ListView<RecipeTileView> btnList) {
        this.btnList = btnList;
    }

    public Button getGenerateRecipeButton() {
        return this.generateRecipeBtn;
    }

    public void setGenerateButtonAction(EventHandler<ActionEvent> eventHandler) {
        generateRecipeBtn.setOnAction(eventHandler);
    }

    public Header getHeader(){
        return this.header;
    }
}

class Header extends VBox{

    private Button generateRecipeBtn;

    // Constructor for the header
    Header() {
        this.setPrefSize(500, 50);
        this.setStyle("-fx-background-color: #F0F8FF;");
        
        // Title of the application
        Text titleText = new Text("PantryPal"); 
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.TOP_LEFT); // Align the text to the Center Left

        // Generate Recipe Button
        generateRecipeBtn = new Button("Generate Recipe"); 
        generateRecipeBtn.setAlignment(Pos.TOP_RIGHT);
        this.getChildren().add(generateRecipeBtn);
    }

    public Button getGenerateRecipeButton(){
        return generateRecipeBtn;
    }
}

