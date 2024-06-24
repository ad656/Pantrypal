package PantryPal.client.models;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.nio.file.*;

public class BasicHTTP {

    private static final String url_root = "http://localhost:8100/";

    /**
     * performs a http request and returns the response.
     * @param method GET, POST, PUT, DELETE
     * @param query
     * @param title
     * @param mealType
     * @param description
     * @param uuid
     * @return
     */
    public static String performRequest(String method, String query, String title, 
        String mealType, String description, String uuid) {

        try {
            String urlString = url_root;
            if (query != null) {
                urlString += "?=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            
            if (method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(
                    
                   URLEncoder.encode(uuid, StandardCharsets.UTF_8) 
                    + "," + URLEncoder.encode(title, StandardCharsets.UTF_8)
                     + "," + URLEncoder.encode(mealType, StandardCharsets.UTF_8) 
                     + "," + URLEncoder.encode(description, StandardCharsets.UTF_8)
                );
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    /**
     * Sends a Recording through POST. 
     * SOURCE: https://stackoverflow.com/a/37885810
     * SOURCE: https://piazza.com/class/lmy9axhgowe53s/post/325
     * @param actionType String for either {ingredients|mealtype} 
     * @return Stringified JSON for a single recipe (on success) or empty string (on fail)
     */
    public static String performRecordingRequest(String actionType) {
        final String POST_URL = url_root+"recording/?="+actionType;
        final File uploadFile = new File("recording.wav");

        String boundary = Long.toHexString(System.currentTimeMillis());     
        String CRLF = "\r\n";
        String charset = "UTF-8";
        
        try {
            // open new http connection
            HttpURLConnection conn = (HttpURLConnection) new URL(POST_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // setting request headers
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            // sending request body
            OutputStream output = conn.getOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(output, charset);
            out.append("--" + boundary).append(CRLF);
            out.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + uploadFile.getName() + "\"").append(CRLF);
            out.append("Content-Length: " + uploadFile.length()).append(CRLF);
            out.append("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile.getName())).append(CRLF);
            out.append("Content-Transfer-Encoding: binary").append(CRLF);
            out.append(CRLF).flush();
            // stick the file into the response body
            Files.copy(uploadFile.toPath(), output);
            output.flush();

            // use bufferedreader + inpustreamreader to get the response.
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String outstr = "";
            char[] cbuf = new char[100];
            while (in.read(cbuf) > 0){
                outstr += String.valueOf(cbuf);
            }
            in.close();

            String response = outstr;
            return response;
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
