package mocks;

import java.io.*;
import java.net.*;
import PantryPal.server.interfaces.IWhisperManager;

/**
Serves as a mock whisper manager for the purposes of testing
 */
public class MockWhisperManager implements IWhisperManager {

    private String returnString;

    public void setMockedTranscript(String returnString){
        this.returnString = returnString;
    }

    /**
     * Generates FAKE transcript from recording.wav 
     */
    public String generateTranscript() throws IOException, URISyntaxException {
        return returnString;
    }

	@Override
	public void setFilePath(String string) {
		
	}
}