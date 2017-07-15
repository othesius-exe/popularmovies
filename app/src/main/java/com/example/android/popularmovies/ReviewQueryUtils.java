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

/**
 *
 */

public class ReviewQueryUtils {
    private static final String LOG_TAG = ReviewQueryUtils.class.getSimpleName();

    /**
     * Query the Movies API.
     */
    public static ArrayList<Review> fetchReviewData(String requestUrl) {

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
        ArrayList<Review> reviews = extractReviewFromJson(jsonResponse);

        return reviews;
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

    public static ArrayList<Review> extractReviewFromJson(String movieJson) {
        String author = "";
        String content = "";

        // Make Sure the JSON isn't empty
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }

        // Create an ArrayList to store Reviews in
        ArrayList<Review> reviewsArrayList = new ArrayList<>();

        // Parse the JSON response using key:value pairs to get desired info
        try {
            JSONObject baseJsonResponse = new JSONObject(movieJson);
                // Check for reviews
                if (baseJsonResponse.has("results")) {
                    JSONArray reviewArray = baseJsonResponse.getJSONArray("results");
                    for (int r = 0; r < reviewArray.length(); r++) {
                        JSONObject thisReview = reviewArray.getJSONObject(r);
                        if (thisReview.has("author")) {
                            author = thisReview.getString("author");
                        }
                        if (thisReview.has("content")) {
                            content = thisReview.getString("content");
                        }
                    }
                }
                Review review = new Review(author, content);
                reviewsArrayList.add(review);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Trouble parsing JSON.");
        }
        return reviewsArrayList;

    }
}
