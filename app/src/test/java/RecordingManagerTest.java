import PantryPal.client.models.RecordingManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecordingManagerTest {
    
    /**
     * This tests if the recording manager creates the .wav file as expected (start and stop recording.)
     * Note that an empty file will be created if the audio cannot be recorded on that device.
     */
    @Test
    public void recordingFileTest() {
        try {
            RecordingManager manager;
            manager = new RecordingManager();
            manager.startRecording();
            int n = 0;
            while (n < 15){
                try {
                    Thread.sleep(100);

                } catch (Exception e) {
                    
                }
                n++;
            }
            // should return true !!!
            assertTrue(manager.stopRecording());
            
        } catch (Exception e){
            fail("failed recording");
        }
    }
}
