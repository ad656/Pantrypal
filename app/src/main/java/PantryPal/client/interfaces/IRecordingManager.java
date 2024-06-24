package PantryPal.client.interfaces;

public interface IRecordingManager {

    /**
     * Begins the recording.
     * Will store wav file at recording.wav
     */
    public void startRecording();

    /**
     * Stops the recording.
     */
    public void stopRecording();
}
