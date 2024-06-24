import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mocks.MockGPTManager;
import mocks.MockWhisperManager;
import PantryPal.server.models.RecipeManager;
import PantryPal.server.handlers.RecordingHandler;

class RecordingHandlerTest {

    private RecordingHandler recordingHandler;
    private RecipeManager recipeManager;
    private MockWhisperManager mockWhisperManager;
    private MockGPTManager mockGPTManager;

    /**
     * Initializes the recording handleer object and the corresponding mock manager objects
     * for testing purposes
     */
    @BeforeEach
    public void setupTests() {
        recipeManager = new RecipeManager("");
        mockWhisperManager = new MockWhisperManager();
        mockGPTManager = new MockGPTManager();
        recordingHandler = new RecordingHandler(recipeManager, mockWhisperManager, mockGPTManager);        
    }

    /**
     * Tests RecordHandler for the mealtype types for breakfast, lunch, and dinner 
     */
    @Test
    public void handleMealTypeTest() {
        mockWhisperManager.setMockedTranscript("I would like to have breakfast");
        assertEquals("breakfast", recordingHandler.handleMealTypeSubmission());        
        
        mockWhisperManager.setMockedTranscript("I would like to have a recipe for lunch instead");
        assertEquals("lunch", recordingHandler.handleMealTypeSubmission()); 
        
        mockWhisperManager.setMockedTranscript("I want something for dinner tonight");
        assertEquals("dinner", recordingHandler.handleMealTypeSubmission()); 
    
        mockWhisperManager.setMockedTranscript("I would like to have brunch");
        assertEquals("", recordingHandler.handleMealTypeSubmission()); 
    }

    /**
     * Test RecordHandler for the ingredientsSubmission printing correct output
     */
    @Test
    public void handleIngredientsTest() throws Exception {
        String prompt = "bacon, eggs, and cheese";
        String mealType = "breakfast";

        mockWhisperManager.setMockedTranscript(prompt);

        String expected = mockGPTManager.analyze(prompt, mealType);
        String result = recordingHandler.handleIngredientsSubmission(mealType);
        
        assertEquals(expected, result);
    }
    
}