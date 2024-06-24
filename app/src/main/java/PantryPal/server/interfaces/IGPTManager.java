package PantryPal.server.interfaces;
import java.net.*;
import java.io.*;

public interface IGPTManager {
    /**
     * Takes prompt and mealtype, returns stuff in a Stringified JSON
     */
    public String analyze(String prompt, String mealType) 
        throws IOException, InterruptedException, URISyntaxException;
}