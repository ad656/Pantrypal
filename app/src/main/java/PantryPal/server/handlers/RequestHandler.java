package PantryPal.server.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import PantryPal.server.interfaces.IRecipe;
import PantryPal.server.models.Recipe;
import PantryPal.server.models.RecipeManager;

public class RequestHandler implements HttpHandler {

    private RecipeManager recipeManager;

    public RequestHandler(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }
    
    /**
     * Method for handling the various HTTP requests, such as GET, POST, PUT, and DELETE 
     * @param exchange HTTP exchange containing the request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Receiving a request!");
        String response = "Response Received";
        String method = exchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                response = handleGet(exchange);
            } else if (method.equals("POST")) {
                response = handlePost(exchange);
            } else if (method.equals("PUT")) {
                response = handlePut(exchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(exchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }
        //Sending back response to the client
        exchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = exchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    /**
     * Method for handling the GET request from the HTTP exchange
     * @param exchange HTTP exchange containing the GET request
     * @return If there is no query, the entire recipe list is returned.
     * If there is a query, then the recipe specified by UUID is returned.
     */
    public String handleGet(HttpExchange exchange) {
        // Can get a RecipeList
        String response = "Invalid GET request";
        URI uri = exchange.getRequestURI();
        String query = uri.getRawQuery();
        String recipeListJson = recipeManager.genSaveString();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            // Retrieves the recipe list as a JSON string
            
            if (recipeListJson != null) {
                IRecipe recipe = recipeManager.getRecipe(value);
                if(recipe != null){
                    response = recipe.getJSONString();
                }else{
                    // Error handling
                }
                
            } else {
                response = "No data found for " + value;
            }
        } else {
            response = recipeListJson;
        }
        return response;
    }

    /**
     * Method for handling the POST request from the HTTP exchange 
     * @param exchange HTTP exchange containing the request
     * @return The response of the POST request, which contains the recipe information
     */
    public String handlePost(HttpExchange exchange) {
        // Creates a recipe
        InputStream inStream = exchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        // POST format `title,mealtype,description`
        int first = postData.indexOf(",");
        int second = postData.indexOf(",", first + 1); 
        String title = URLDecoder.decode(postData.substring(0, first), StandardCharsets.UTF_8),
        mealType = URLDecoder.decode(postData.substring(first + 1, second), StandardCharsets.UTF_8),
        description = URLDecoder.decode(postData.substring(second + 1), StandardCharsets.UTF_8);
        // Add a completely new recipe to manager
        recipeManager.addRecipe(
            new Recipe(title, description, mealType)
        );
        // Print response
        String response = "Posted entry {" + title + ", " + mealType + ", " + description + "}";
        System.out.println(response);
        scanner.close();

        return response;
    }

    /**
     * Method for handling the PUT request from the HTTP exchange 
     * @param exchange HTTP exchange containing the request
     * @return The response of the PUT request, which contains the information of the updated recipe
     */
    public String handlePut(HttpExchange exchange) {
        // Creates/Updates a recipe
        InputStream inStream = exchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        // POST format `uuid,title,mealtype,description`
        int first = postData.indexOf(",");
        int second = postData.indexOf(",", first + 1); 
        int third = postData.indexOf(",", second + 1); 
        String uuid = URLDecoder.decode(postData.substring(0, first), StandardCharsets.UTF_8);
        String title = URLDecoder.decode(postData.substring(first + 1, second), StandardCharsets.UTF_8);
        String mealType = URLDecoder.decode(postData.substring(second + 1, third), StandardCharsets.UTF_8);
        String description = URLDecoder.decode(postData.substring(third + 1), StandardCharsets.UTF_8);

        String response = "";
        // check if exists
        IRecipe recipe = recipeManager.getRecipe(uuid);
        if (recipe == null) {
            // Add recipe
            recipeManager.addRecipe(
                new Recipe(title, description, mealType)
            );
            response = "Added entry {" + title + ", " + mealType + ", " + description + "}";
        }
        else {
            // Update recipe
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setMealType(mealType);
            response = "Updated entry {" + uuid + ", " + title + ", " + mealType + ", " + description + "}";
        }
        System.out.println(response);
        scanner.close();
        return response;
    }

    /**
     * Method for handling the DELETE request from the HTTP exchange 
     * @param exchange HTTP exchange containing the request
     * @return The response of the DELETE request, whether the delete was successful
     */
    public String handleDelete(HttpExchange exchange) {
        System.out.println("I got called!");
        // Deletes a recipe
        String response = "Invalid DELETE request";
        URI uri = exchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String UUID = query.substring(query.indexOf("=") + 1);
            IRecipe recipe = recipeManager.getRecipe(UUID);
            if(recipe != null){
                recipeManager.deleteRecipe(recipe);
                response = "Recipe deleted successfully.";
            }else{
                System.out.println("I tried to get this UUID: "+UUID);
            }
        }
        return response;

    }


}
