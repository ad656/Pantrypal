package PantryPal.server.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import PantryPal.server.interfaces.IRecipe;

public class RecipeManager {
    private ArrayList<IRecipe> recipes;
    private String filePath;

    /**
     * Constructor
     * @param filePath filepath for saved recipes
     */
    public RecipeManager(String filePath){
        this.filePath = filePath;
        this.recipes = new ArrayList<IRecipe>();
        this.loadRecipes();
    }

    /**
     * Set recipe savefile path
     * @param filePath Recipe savefile path
     */
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    /**
     * Get recipe savefile path
     * @return recipe savefile path
     */
    public String getFilePath(){
        return this.filePath;
    }

    /**
     * Getter for recipe list
     * @return recipe arraylist
     */
    public ArrayList<IRecipe> getRecipes(){
        return this.recipes;
    }

    public IRecipe getRecipe(String uuid){
        for(IRecipe r : this.recipes){
            if(r.getUUID().toString().equals(uuid)){
                return r;
            }
        }
        return null;
    }

    /**
     * Add new recipe to recipe list
     * @param recipe Recipe to add
     */
    public void addRecipe(IRecipe recipe){
        if(this.recipes.contains(recipe)){
            
        } else {
            this.recipes.add(0, recipe);
            try{
                this.saveRecipes();
            } catch (Exception e){

            }
        }
    }

     /**
     * Edit recipe to recipe list
     * @param recipe Recipe to edit
     */
    public void updateRecipe(IRecipe recipe, String newTitle, String newDescription){
        if(this.recipes.contains(recipe)){
            recipe.setTitle(newTitle);
            recipe.setDescription(newDescription);

            try{
                this.saveRecipes();
            } catch (Exception e){

            }
        }
    }
    /**
     * Remove recipe from arraylist
     * @return boolean true if removed successfuly
     *  */
    public boolean deleteRecipe(IRecipe recipe){
        System.out.println("Deleting recipe!");
        boolean deleted = recipes.remove(recipe);
        if(deleted){
            try{
                this.saveRecipes();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return deleted;
    }

    /**
     * Reads file
     */
    private String readFile(){
        // loads da recipes
        // sticks it in this.recipes
        String i = "";

        File f = new File(filePath);
        if (!f.isFile()){
            return "";
        }

        try {
            FileReader filereader = new FileReader(filePath);
            BufferedReader buffreader = new BufferedReader(filereader);
            char[] cbuf = new char[10];
            while (filereader.read(cbuf)>0){
                i += String.valueOf(cbuf);
                cbuf = new char[10];
            }
            buffreader.close();
            filereader.close();
        } catch(Exception e) {
            System.out.println("readFile() did an oopsie!\n"+e);
        }
        return i;
    }

    /**
     * loadrecipes from file into recipes
     */
    public void loadRecipes(){
        recipes.clear();

        String JSONString = readFile();

        if (JSONString == null || JSONString.isEmpty()){
            return;
        }

        try {
            for (Object i: new JSONObject(JSONString).getJSONArray("recipes")) {
                JSONObject j = (JSONObject) i;
                recipes.add(new Recipe(j.getString("title"),
                    j.getString("recipe"), j.getString("mealtype"), j.getString("UUID")));

            }
        } catch (Exception e){
            System.out.println(e);
            //return false;
        }
        //return true;
    }

    /**
     * Given an output string from GPT,
     * split the recipe into its title, description.
     */
    public IRecipe parseRecipe(String recipeString){
        JSONObject recipeJSON = new JSONObject(recipeString);
        return new Recipe(
            recipeJSON.getString("title"),
            recipeJSON.getString("recipe"),
            recipeJSON.getString("mealtype")
        );
    }

    /**
     * Saves recipe to file
     * @throws IOException
     */
    public void saveRecipes() throws IOException{
        String saveString = genSaveString();
        FileWriter filewriter = new FileWriter(this.filePath);
        filewriter.write(saveString);
        filewriter.close();
    }

    /**
     * Will generate a JSON string of the recipe list for saving
     * @return A JSON version of the recipe list
     */
    public String genSaveString(){
        JSONObject exportObject = new JSONObject();
        JSONArray recipeArray = new JSONArray();
        for(IRecipe i: recipes){
            JSONObject j = new JSONObject();
            j.put("title", i.getTitle());
            j.put("recipe", i.getDescription());
            j.put("mealtype", i.getMealType());
            j.put("UUID", i.getUUID());
            recipeArray.put(j);
        }
        exportObject.put("recipes", recipeArray);
        return exportObject.toString();
    }
}

