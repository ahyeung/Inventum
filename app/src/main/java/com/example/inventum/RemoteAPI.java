package com.example.inventum;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RemoteAPI {

    public static String TOKEN;
    public static String TRACK_ID;

    public static Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("RemoteAPI", "That didn't work!");

            // see error response
            String body = "";
            //get status code here
            String statusCode = String.valueOf(error.networkResponse.statusCode);
            //get response body and parse with appropriate encoding
            if(error.networkResponse.data != null) {
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //do stuff with the body...
            Log.e("RemoteAPI", body);
            //Log.d("Token", Authenticated.AUTH_TOKEN);
        }
    };

    public static StringRequest getTrack(Response.Listener<String> listener, String token, String trackID, String market) {

        String url = "https://api.spotify.com/v1/tracks/" + trackID + "?market=" + market;

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getTracks(Response.Listener<String> listener, String token, String trackIDs, String market) {

        String url = "https://api.spotify.com/v1/tracks?ids=" + trackIDs;

        if (market != null && !market.isEmpty()) {
            url = url + "&market=" + market;
        }

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getTrackAudioFeatures(Response.Listener<String> listener, String token, String trackID) {

        String url = "https://api.spotify.com/v1/audio-features/" + trackID;

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getTracksAudioFeatures(Response.Listener<String> listener, String token, String trackIDs) {

        String url = "https://api.spotify.com/v1/audio-features?ids=" + trackIDs;

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getUserTracks(Response.Listener<String> listener, String token) {

        String url = "https://api.spotify.com/v1/me/top/tracks?time_rante=short_term&limit=20";

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getUser(Response.Listener<String> listener, String token) {

        String url = "https://api.spotify.com/v1/me/";

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getRecommendations(Response.Listener<String> listener, String token, String seed_genres, String seed_artists,
                                                   String seed_tracks, String market, int limit, float target_acousticness, float target_danceability,
                                                   int target_duration_ms, float target_energy, float target_instrumentalness, float target_liveness,
                                                   float target_loudness, int target_popularity, float target_speechiness, int target_tempo,
                                                   float target_valence) {

        String url = "https://api.spotify.com/v1/recommendations?";

        if (seed_genres != null && !seed_genres.isEmpty()) {
            if (url.endsWith("?")) {
                url = url + "seed_genres=" + seed_genres;
            } else {
                url = url + "&seed_genres=" + seed_genres;
            }
        }
        if (seed_artists != null && !seed_artists.isEmpty()) {
            if (url.endsWith("?")) {
                url = url + "seed_artists=" + seed_artists;
            } else {
                url = url + "&seed_artists=" + seed_artists;
            }
        }
        if (seed_tracks != null && !seed_tracks.isEmpty()) {
            if (url.endsWith("?")) {
                url = url + "seed_tracks=" + seed_tracks;
            } else {
                url = url + "&seed_tracks=" + seed_tracks;
            }
        }
        if (market != null && !market.isEmpty()) {
            url = url + "&market=" + market;
        }
        if (limit >= 0 && limit <= 50) {
            url = url + "&limit=" + limit;
        }
        if (target_acousticness >= 0 && target_acousticness <= 1) {
            url = url + "&target_acousticness=" + Float.toString(target_acousticness);
        }
        if (target_danceability >= 0 && target_danceability <= 1) {
            url = url + "&target_danceability=" + Float.toString(target_danceability);
        }
        if (target_energy >= 0 && target_energy <= 1) {
            url = url + "&target_energy=" + Float.toString(target_energy);
        }
        if (target_instrumentalness >= 0 && target_instrumentalness <= 1) {
            url = url + "&target_instrumentalness=" + Float.toString(target_instrumentalness);
        }
        if (target_liveness >= 0 && target_liveness <= 1) {
            url = url + "&target_liveness=" + Float.toString(target_liveness);
        }
        if (target_loudness >= 0 && target_loudness <= 1) {
            url = url + "&target_loudness=" + Float.toString(target_loudness);
        }
        if (target_popularity >= 0 && target_popularity <= 100) {
            url = url + "&target_popularity=" + target_popularity;
        }
        if (target_speechiness >= 0 && target_speechiness <= 1) {
            url = url + "&target_speechiness=" + Float.toString(target_speechiness);
        }
        if (target_valence >= 0 && target_valence <= 1) {
            url = url + "&target_valence=" + Float.toString(target_valence);
        }
        if (target_duration_ms >= 0) {
            url = url + "&target_duration_ms=" + target_duration_ms;
        }
        if (target_tempo >= 0) {
            url = url + "&target_tempo=" + target_tempo;
        }

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest search(Response.Listener<String> listener, String token, String query, String market, String type) {

        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=" + type + "&limit=5";

        if (market != null && !market.isEmpty()) {
            url = url + "&market=" + market;
        }

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getGenres(Response.Listener<String> listener, String token) {

        String url = "https://api.spotify.com/v1/recommendations/available-genre-seeds";

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static StringRequest getGlobalTopSongs(Response.Listener<String> listener, String token, String market) {

        // limit currently set to return 25 tracks
        String url = "https://api.spotify.com/v1/playlists/37i9dQZEVXbNG2KDcFcKOF/tracks?limit=25";

        if (market != null && !market.isEmpty()) {
            url = url + "&market=" + market;
        }

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String auth = "Bearer " + token;
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization:", auth);
                params.put("Host", "api.spotify.com");

                return params;
            }
        };
    }

    public static String parseTopTracksResponse(JSONObject response) {
        String trackIDs = "";

        try {
            JSONArray array = response.getJSONArray("items");
            for (int i = 1; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                trackIDs = trackIDs + object.getJSONObject("track").getString("id") + ",";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!trackIDs.isEmpty()) {
            return trackIDs.substring(0, trackIDs.length() - 1);
        }
        return trackIDs;
    }

    public static String parseUserTracksResponse(JSONObject response) {
        String trackIDs = "";

        try {
            JSONArray array = response.getJSONArray("items");
            for (int i = 1; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                trackIDs = trackIDs + object.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!trackIDs.isEmpty()) {
            return trackIDs.substring(0, trackIDs.length() - 1);
        }
        return trackIDs;
    }

    public static String parseRecommendationsResult(JSONObject response) {
        String trackIDs = "";

        try {
            JSONArray array = response.getJSONArray("tracks");
            for (int i = 1; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                trackIDs = trackIDs + object.getString("id") + ",";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!trackIDs.isEmpty()) {
            return trackIDs.substring(0, trackIDs.length() - 1);
        }
        return trackIDs;
    }

    public static ArrayList<invTrack> getTrackList(String token, String trackIDs, String market, Context applicationContext) throws JSONException, ExecutionException, InterruptedException {
        final JSONObject[] tracks = {null};
        final JSONObject[] tracks_expanded = {null};

        Response.Listener<String> track_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Authenticated", response.substring(0,500));
                try {
                    tracks[0] = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.Listener<String> expanded_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Authenticated", response.substring(0,500));
                try {
                    tracks_expanded[0] = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        StringRequest trackRequest = RemoteAPI.getTracks(track_listener, token, trackIDs, market);
        StringRequest expandedRequest = RemoteAPI.getTracksAudioFeatures(expanded_listener, token, trackIDs);
        queue.add(trackRequest);
        queue.add(expandedRequest);

        ArrayList<invTrack> trackArrayList = new ArrayList<>();

        StringRequest tracks_response = getTracks(track_listener, token, trackIDs, market);

        JSONArray tracksArray = tracks[0].getJSONArray("tracks");
        //JSONArray expandedArray = tracks_expanded[0].getJSONArray("audio_features");
        for (int i = 0; i < tracksArray.length(); i ++) {
            JSONObject trackObject = tracksArray.getJSONObject(i);
            //JSONObject expandedObject = expandedArray.getJSONObject(i);


        }

        return trackArrayList;
    }
}
