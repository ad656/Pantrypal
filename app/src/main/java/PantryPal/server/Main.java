package PantryPal.server;

import com.sun.net.httpserver.*;

import PantryPal.server.models.GPTManager;
import PantryPal.server.models.RecipeManager;
import PantryPal.server.models.WhisperManager;
import PantryPal.server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        HttpServer server = HttpServer.create(
            new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0
        );

        // create managers
        RecipeManager recipeManager = new RecipeManager("file.json");
        WhisperManager whisperManager = new WhisperManager(args[0]);
        GPTManager gptManager = new GPTManager(args[0]);
        // create the contexts
        server.createContext("/", new RequestHandler(recipeManager));
        server.createContext("/recording", new RecordingHandler(recipeManager, whisperManager, gptManager));
        // set the executor
        server.setExecutor(threadPoolExecutor);
        // start the server
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);
    }
}
