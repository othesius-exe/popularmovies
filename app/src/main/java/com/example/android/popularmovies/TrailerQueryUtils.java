package com.example.android.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class TrailerQueryUtils {
    private static final String LOG_TAG = TrailerQueryUtils.class.getSimpleName();

    /**
     * Query the Movies API.
     */
    public static List<Trailer> fetchTrailerData(String requestUrl) {

        // Create a Url Object.
        URL url = createUrl(requestUrl);

        // Perform Http request and receive JSON response.
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error closing input stream.", e);
        }

        // Extract relevant fields from json and create a new Movie object
        List<Trailer> trailers = extractTrailersFromJson(jsonResponse);

        return trailers;
    }

    /**
     * Returns a URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Bad Url", e);
        }
        return url;
    }

    /**
     * Make an Http Request to url.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If URL is null, return early.
        if (url == null) {
            return jsonResponse;
        }

        // Try to open a connection.
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the connection is successful (Response code == 200)
            // Read the stream and parse the response

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Failed to connect: Response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem retrieving JSON from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert InputStream into a string containing the JSON response
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Build a Movie Object from the JSON response
     */

    public static List<Trailer> extractTrailersFromJson(String movieJson) {
        String trailerKey = "";
        String trailerId = "";

        // Make Sure the JSON isn't empty
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }

        // Create an ArrayList to store trailers in
        ArrayList<Trailer> trailerArrayList = new ArrayList<>();

        // Parse the JSON response using key:value pairs to get desired info
        try {
            JSONObject baseJsonResponse = new JSONObject(movieJson);
            for (int i = 0; i < baseJsonResponse.length(); i ++) {
                if (baseJsonResponse.has("videos")) {
                    JSONObject trailerObject = baseJsonResponse.getJSONObject("videos");
                    if (trailerObject.has("results")) {
                        JSONArray trailerArray = trailerObject.getJSONArray("results");
                        for (int v = 0; v < trailerArray.length(); v++) {
                            JSONObject thisVideo = trailerArray.getJSONObject(v);
                            if (thisVideo.has("key")) {
                                trailerKey = thisVideo.getString("key");
                            }
                            if (thisVideo.has("id")) {
                                trailerId = thisVideo.getString("id");
                            }
                        }
                    }
                }
                Trailer trailer = new Trailer(trailerKey, trailerId);
                trailerArrayList.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Trouble parsing JSON.");
        }
        return trailerArrayList;

    }
}
