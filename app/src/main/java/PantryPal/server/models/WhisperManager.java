package PantryPal.server.models;

import java.io.*;
import java.net.*;
import org.json.*;
import PantryPal.server.interfaces.IWhisperManager;

public class WhisperManager implements IWhisperManager {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private String token = "";
    private static final String MODEL = "whisper-1";
    private String filename = "recording.wav";

    public WhisperManager(String token){
        this.token = token;
    }

    public void setFilePath(String filename){
        this.filename = filename;
    }

    /**
     * Helper method to write a parameter to the output stream in multipart form data format
     * @param outputStream
     * @param parameterName
     * @param parameterValue
     * @param boundary
     * @throws IOException
     */
    private static void writeParameterToOutputStream (
        OutputStream outputStream,
        String parameterName,
        String parameterValue,
        String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            ("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }
    
    /**
     * Helper method to write a file to the output stream in multipart form data format
     * @param outputStream
     * @param file
     * @param boundary
     * @throws IOException
     */
    private static void writeFileToOutputStream (
        OutputStream outputStream,
        File file,
        String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
        (
        "Content-Disposition: form-data; name=\"file\"; filename=\"" +
        file.getName() +
        "\"\r\n"
        ).getBytes()
            );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }
    
    /**
     * Helper method to handle a successful response
     * @param connection
     * @return Transcript text from the HTTP response
     * @throws IOException
     * @throws JSONException
     */
    private static String handleSuccessResponse(HttpURLConnection connection)
    throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject responseJson = new JSONObject(response.toString());
        String generatedText = responseJson.getString("text");
        // sucessful transcript text
        return generatedText;
    }

    /**
     * Helper method to handle an error response
     * @param connection
     * @return The error response from the HTTP connection
     * @throws IOException
     * @throws JSONException
     */
    private static String handleErrorResponse(HttpURLConnection connection)
    throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        // the resulting error
        return "Error Result: " + errorResult;
    }

    /**
     * Method for calling the Whisper API to generate and return the audio transcript.
     * @return String containing audio transcript
     * @throws IOException
     * @throws URISyntaxException
     */
    public String generateTranscript() throws IOException, URISyntaxException {
        // Create file object from file path
        File file = new File(this.filename);
        
        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        
        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=" + boundary
        );
        connection.setRequestProperty("Authorization", "Bearer " + this.token);
        
        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();
        
        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
        
        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);
        
        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
        
        // Flush and close output stream
        outputStream.flush();
        outputStream.close();
        
        // Get response code
        int responseCode = connection.getResponseCode();
        
        // String to hold the transcript if successful
        String transcript = null;

        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            transcript = handleSuccessResponse(connection);
        } else {
            transcript = handleErrorResponse(connection);
        }
        
        // Disconnect connection
        connection.disconnect();

        return transcript;
    }
}