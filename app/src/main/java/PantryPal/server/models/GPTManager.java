package PantryPal.server.models;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import PantryPal.server.interfaces.IGPTManager;

public class GPTManager implements IGPTManager {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private String token = "";
    private static final String MODEL = "gpt-3.5-turbo";
    private static final int maxTokens = 400;
    private static final String SYSTEM_PROMPT = "You are a Recipe Suggester. "+
    "Create a step-by-step recipe containing ingredient measurements when given "+
    "a mealtype and ingredients. Ensure the recipe only contains the ingredients "+
    "listed. Format the recipe as a JSON with three fields:"+
    "\n```{title:\"TITLE STRING\",\nmealtype:\"MEALTYPE\",\nrecipe:\"INGREDIENTS STRING RECIPE STRING\"}```"+
    "\nEnsure that your title is a brief yet informative description of the recipe.";

    public GPTManager(String token){
        this.token = token;
    }

    /**
     * Setter for api key
     * @param token
     */
    public void setToken(String token){
        this.token = token;
    }

    /**
     * Getter for token? Is this needed?
     * @return token
     */
    public String getToken(){
        return this.token;
    }
    
    /**
     * Formats prompt to feed into GPT
     * @param ingredients available ingredients
     * @param mealType meal type
     * @return JSONObject of GPT api call
     */
    private JSONObject formatPrompt(String ingredients, String mealType) {
        String mainPrompt = "My mealtype is %s. Use the following ingredients: %s";
        String formattedPrompt = String.format(mainPrompt, mealType, ingredients);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("max_tokens", maxTokens);

        JSONArray messages = new JSONArray();
        JSONObject system = new JSONObject();
        JSONObject user = new JSONObject();
        requestBody.put("messages", messages);
        messages.put(system);
        messages.put(user);
        system.put("role", "system");
        system.put("content", SYSTEM_PROMPT);
        user.put("role", "user");
        user.put("content", formattedPrompt);

        return requestBody;
    }

    /**
     * Creates a JSON string of GPT's response from our prompt
     * @return Stringified JSON of GPT response in the format of
     * {"title" "mealtype" "recipe"}
     */
    public String analyze(String prompt, String mealType) 
        throws IOException, InterruptedException, URISyntaxException {
        JSONObject formattedPrompt = formatPrompt(prompt, mealType);
        String generatedText = sendReq(formattedPrompt);
        return generatedText;
    }    

    /**
     * Send HTTP request to GPT.
     * @param requestBody formatted request body for the API call
     * @return JSON string of GPT's response
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    private String sendReq(JSONObject requestBody) 
        throws IOException, InterruptedException, URISyntaxException {

        // Create a request body which you will pass into request object

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(new URI(API_ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", token))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();


        // Send the request and receive the response
        HttpResponse<String> response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = ((JSONObject)choices.getJSONObject(0)).getJSONObject("message").getString("content");
        return generatedText;
    }

    public String[] parseGPTResponse(String out) {
        return null;
    }
}
