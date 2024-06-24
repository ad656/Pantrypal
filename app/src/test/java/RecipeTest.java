import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PantryPal.server.interfaces.IRecipe;
import PantryPal.server.models.Recipe;

public class RecipeTest {

    private IRecipe firstRecipe;
    private IRecipe secondRecipe;

    /**
     * Sets up the tests by assigning recipes with information, 
     * and one recipe is given
     */
    @BeforeEach
    public void setup(){
        String title = "masala";
        String description = "stir masala";
        String mealType = "breakfast";
        String uuid = UUID.randomUUID().toString();

        firstRecipe = new Recipe(title, description, mealType);
        secondRecipe = new Recipe(title, description, mealType, uuid);
    }

    /**
     * Assert that when a recipe is created, values aren't changed.
     */
    @Test
    public void testJsonString(){
        JSONObject expected = new JSONObject();
            expected.put("title", firstRecipe.getTitle());
            expected.put("recipe", firstRecipe.getDescription());
            expected.put("mealtype", firstRecipe.getMealType());
            expected.put("UUID", firstRecipe.getUUID()); 

        assertEquals(expected.toString(), firstRecipe.getJSONString());

        JSONObject expected2 = new JSONObject();
            expected2.put("title", secondRecipe.getTitle());
            expected2.put("recipe", secondRecipe.getDescription());
            expected2.put("mealtype", secondRecipe.getMealType());
            expected2.put("UUID", secondRecipe.getUUID()); 

        assertEquals(expected2.toString(), secondRecipe.getJSONString());
    }
}