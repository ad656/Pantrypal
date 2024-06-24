package PantryPal.client.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class RecipeTileView extends HBox {

    private Button tileBtn;

    /**
     * Constructor for the RecipeTileView to set tile Button names
     * @param recipe string of recipe
     */
    public RecipeTileView(String recipe){ 
        // Construct the button here
        tileBtn = new Button(recipe);
        tileBtn.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 2px; -fx-font-size: 20; -fx-text-fill:#000");
        
        this.getChildren().add(tileBtn);
    }

    public void setTileButtonAction(EventHandler<ActionEvent> eventHandler) {
        tileBtn.setOnAction(eventHandler);
    }

    public Button getTileButton(){
        return this.tileBtn;
    }
}