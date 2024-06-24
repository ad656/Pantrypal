package PantryPal.server.models;

import java.util.UUID;

import org.json.JSONObject;

import PantryPal.server.interfaces.IRecipe;

public class Recipe implements IRecipe {
    private String title;
    private String description;
    private String mealType;
    private UUID uuid;

    /**
     * This constructor is for creating a new recipe, where a new
     * UUID will be generated for the recipe
     * @param title Recipe's title
     * @param description Recipe's description
     * @param mealtype Recipe's meal type
     */
    public Recipe(String title, String description, String mealType){
        this.title = title;
        this.description = description;
        this.mealType = mealType;
        this.uuid = UUID.randomUUID();
    }

    /**
     * This constructor will create a recipe object using a pre-existing UUID
     * @param title Recipe's title
     * @param description Recipe's description
     * @param mealtype Recipe's meal type
     * @param UUID The pre-existing UUID for a recipe
     */
    public Recipe(String title, String description, String mealType, String uuid){
        this.title = title;
        this.description = description;
        this.mealType = mealType;
        this.uuid = UUID.fromString(uuid);
    }

    /*
     * Getters and Setters 
     */

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getMealType(){
        return this.mealType;
    }

    public void setMealType(String mealType){
        this.mealType = mealType;
    }

    public String getUUID() {
        return this.uuid.toString();
    }

    /**
     * Returns the recipe information as a JSON string
     * @return A JSONified version of the recipe as a string
     */
    public String getJSONString() {
       JSONObject j = new JSONObject();
            j.put("title", this.getTitle());
            j.put("recipe", this.getDescription());
            j.put("mealtype", this.getMealType());
            j.put("UUID", this.getUUID()); 
        return j.toString();
    }
}
