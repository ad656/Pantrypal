package PantryPal.client.views;

import javafx.scene.control.Button;

public class DefaultButton extends Button{

    /**
     * Constructor for DefaultButton settings
     * @param text string to set text
     */
    public DefaultButton(String text){
        this.setText(text);
        this.setPrefSize(250, 50);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 2px; -fx-font-size: 20; -fx-text-fill:#000");
    }
}