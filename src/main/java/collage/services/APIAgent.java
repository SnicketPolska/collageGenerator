package collage.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;


public class APIAgent extends DataAgent{

    private static String user;
    private static String period;

    public BufferedImage[] getData(int size) {

        JSONArray dataArray = getTopAlbums(); //retrieve data from API
        if(dataArray == null) return null;

        BufferedImage[] images = new BufferedImage[size * size];
        for (int i = 0; i < size * size; i++) {
            String artist = null,album = null;
            try {
                artist = dataArray.getJSONObject(i).getJSONObject("artist").getString("name");
                album = dataArray.getJSONObject(i).getString("name");
                URL url = new URL(dataArray.getJSONObject(i).getJSONArray("image").getJSONObject(3).getString("#text"));
                images[i] = ImageIO.read(url);
                images[i] = Draw.drawInfo(images[i],artist,album);
            } catch (IllegalArgumentException exception) {
                // If image is corrupted replace it with a black rectangle that preserves the album data
                exception.printStackTrace();
                images[i] = Draw.getBlackRect(images);
                images[i] = Draw.drawInfo(images[i],artist,album);
            } catch (IOException | JSONException exception){
                exception.printStackTrace();
            }
        }
        return images;
    }


    public static JSONArray getTopAlbums() {
            String response = callHTTP(1);

            if(response != null) {
                JSONObject json = new JSONObject(response);
                JSONArray dataArray = json.getJSONObject("topalbums").getJSONArray("album");

                response = callHTTP(2);
                if (response != null) {
                    json = new JSONObject(response);
                    JSONArray newArray = json.getJSONObject("topalbums").getJSONArray("album");
                    for (Object o : newArray) {
                        dataArray.put(o);
                    }
                }
                return dataArray;
            }else{
                return null;
            }
        }

    private static String callHTTP(int page){
        String uri = "http://ws.audioscrobbler.com/2.0/?method=user.gettopalbums&user="
                + user + "&api_key=" + System.getenv("LAST_FM_API_KEY")
                + "&period=" + period + "&format=json" + "&page=" + page;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(uri))
                .header("accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e ) {
            e.printStackTrace();
            return null;
        }

        if(response.statusCode() == 200){
            return response.body();
        }else {
            return null;
        }
    }
}
