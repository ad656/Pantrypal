package mocks;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONObject;

import PantryPal.server.interfaces.IGPTManager;

/**
 * Serves as a MockGPTManager for the purposes of testing 
 */
public class MockGPTManager implements IGPTManager {
    /**
     * Mocks the GPTManager's analyze method for generating text from the prompt and mealtype
     */
    public String analyze(String prompt, String mealType) throws IOException, InterruptedException, URISyntaxException {
        JSONObject j = new JSONObject();
        j.put("title", String.format("title for %s %s", mealType, prompt));
        j.put("recipe", String.format("recipe for %s %s", mealType, prompt));
        j.put("mealtype", mealType);
        return j.toString();
    }
}
