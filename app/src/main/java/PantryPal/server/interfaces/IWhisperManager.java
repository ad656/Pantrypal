package PantryPal.server.interfaces;
import java.net.*;
import java.io.*;

public interface IWhisperManager {
    public String generateTranscript() throws IOException, URISyntaxException ;

	public void setFilePath(String string);
}