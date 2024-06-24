import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.UUID;
import java.io.File;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PantryPal.server.interfaces.IRecipe;
import PantryPal.server.models.Recipe;
import PantryPal.server.models.RecipeManager;

public class RecipeManagerTest {

    private final String RECIPE_FORMAT = "{title:\"%s\",\nmealtype:\"%s\",\nrecipe:\"%s\"}";
    RecipeManager blankManager;

    @BeforeEach
    public void setup(){
        blankManager = new RecipeManager("");
        blankManager.getRecipes().clear();
    }

    /**
     * Tests the recipe manager's JSON parsing functionality
     */
    @Test
    public void testParseRecipe() {
        // strings
        String title = "Eggs and Ham";
        String mealType = "Breakfast";
        String description = "Melt butter in 12-inch nonstick skillet over medium-high heat until "
        + "sizzling; add onion and green pepper. Cook, stirring occasionally, 3-4 minutes or until "
        + "vegetables are crisply tender. Add ham; continue cooking 2-3 minutes or until heated "
        + "through. Reduce heat to medium.";
        String recipeString = String.format(RECIPE_FORMAT, title, mealType, description);
        // object to test
        RecipeManager manager = new RecipeManager("file.txt");
        IRecipe parsedRecipe = manager.parseRecipe(recipeString);
        // asserts
        assertEquals(title, parsedRecipe.getTitle());
        assertEquals(mealType, parsedRecipe.getMealType());
        assertEquals(description, parsedRecipe.getDescription());
    }

    /**
     * Test for writing an empty recipe to file.
     */
    @Test
    public void writeEmptyRecipe() {
        String fp = "test_file.json";
        long fileLen = 0;
        
        File f = new File(fp);
        try {
            f.delete();
        } catch (Exception e){

        }

        blankManager.setFilePath(fp);

        try{
            blankManager.saveRecipes();
            File j = new File(fp);
            fileLen = j.length();
        } catch (Exception e) {
            fail("Failed to write recipe.");
        }
        assertTrue(fileLen > 0);
    }

    /**
     * Tests the loading of a recipe for any exceptions
     */
    @Test
    public void readRecipe() {
        RecipeManager manager = blankManager;
        try{
            manager.loadRecipes();
        } catch (Exception e) {
            fail("failed test");
        }
    }
    
    /**
     * Tests the manager's ability to save a recipe, and then the ability
     * to read that recipe's information back.
     */
    @Test
    public void saveThenReadRecipe() {
        RecipeManager manager = new RecipeManager("test_file1.json");
        
        ArrayList<IRecipe> recipeList = new ArrayList<IRecipe>(); 
        
        recipeList.add(new Recipe("beans toast", "beanies toast", "breakfast")); 
        recipeList.add(new Recipe("beans toast new", "beanies toast newer", "breakfast"));
        
        manager.getRecipes().clear();
        manager.getRecipes().addAll(recipeList);
        
        try{
            manager.saveRecipes();
            manager.loadRecipes();
        } catch (Exception e) {
            fail();
        }

        assertEquals(manager.getRecipes().size(), recipeList.size());

        for(int i = 0; i < recipeList.size(); i++){
            IRecipe cRecipe = recipeList.get(i);
            IRecipe lRecipe = manager.getRecipes().get(i);

            assertEquals(cRecipe.getTitle(), lRecipe.getTitle());

            assertEquals(cRecipe.getDescription(), lRecipe.getDescription());

            assertEquals(cRecipe.getMealType(), lRecipe.getMealType());

            assertEquals(cRecipe.getUUID(), lRecipe.getUUID());
        }
    }

    /**
     * Test for genSaveString base case with no recipes
     */
    @Test
    public void testGenSaveStringEmpty(){
        String correctOutStr = "{\"recipes\":[]}";
        String generatedOutStr = blankManager.genSaveString();
        assertEquals(correctOutStr, generatedOutStr);
    }

    /**
     * Test for genSaveString with multiple recipes.
     */
    @Test
    public void testGenSaveStringMultiple(){

        ArrayList<IRecipe> recipeList = new ArrayList<IRecipe>(); 
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        recipeList.add(new Recipe("beans toast", "beanies toast", "breakfast", uuid1)); 
        recipeList.add(new Recipe("beans toast new", "beanies toast newer", "breakfast", uuid2));
        
        blankManager.getRecipes().clear();
        blankManager.getRecipes().addAll(recipeList);

        String correctOutStr = String.format(
            "{\"recipes\":[{\"recipe\":\"beanies toast\",\"mealtype\":\"breakfast\",\"title\":\"beans toast\",\"UUID\":\"%s\"}"+
            ",{\"recipe\":\"beanies toast newer\",\"mealtype\":\"breakfast\",\"title\":\"beans toast new\",\"UUID\":\"%s\"}]}",
            uuid1, uuid2);
        String generatedOutStr = blankManager.genSaveString();
        assertEquals(correctOutStr, generatedOutStr);
    }

    /**
     * Test for getRecipes searching by UUID.
     */
    @Test
    public void testGetRecipes(){

        ArrayList<IRecipe> recipeList = new ArrayList<IRecipe>(); 
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();
        IRecipe rec1 = new Recipe("beans toast", "beanies toast", "breakfast", uuid1);
        IRecipe rec2 = new Recipe("beans toast new", "beanies toast newer", "breakfast", uuid2);

        recipeList.add(rec1); 
        recipeList.add(rec2);
        
        blankManager.getRecipes().clear();
        blankManager.getRecipes().addAll(recipeList);

        IRecipe correctRecipe = rec1;
        IRecipe generatedRecipe = blankManager.getRecipe(uuid1);
        assertEquals(correctRecipe, generatedRecipe);
    }

    /**
     * Test for deleteRecipe where the recipe doesn't exist.
     */
    @Test
    public void testDeleteRecipe(){
        IRecipe rec1 = new Recipe("beans toast", "beanies toast", "breakfast");
        assertFalse(blankManager.deleteRecipe(rec1));
    }
}