package com.example.inventum;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RemoteAPI {

    public static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("RemoteAPI", "That didn't work!");

            // see error response
            String body = "";
            //get status code here
            String statusCode = String.valueOf(error.networkResponse.statusCode);
            //get response body and parse with appropriate encoding
            if(error.networkResponse.data!=null) {
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //do stuff with the body...
            Log.e("RemoteSPI", body);
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
        String url = "https://api.spotify.com/v1/tracks?" + trackIDs + "&market=" + market;

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
        String url = "https://api.spotify.com/v1/audio-features?" + trackIDs;

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
                                                   String seed_tracks, String market, String limit, int target_acousticness, int target_danceability,
                                                   int target_duration_ms, int target_energy, int target_instrumentalness, int target_liveness,
                                                   int target_loudness, int target_popularity, int target_speechiness, int target_tempo,
                                                   int target_valence) {
        String url = "https://api.spotify.com/v1/recommendations/?";

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
        if (limit != null && !limit.isEmpty()) {
            url = url + "&limit=" + limit;
        }
        if (target_acousticness >= 0 && target_acousticness <= 1) {
            url = url + "&target_acousticness=" + target_acousticness;
        }
        if (target_danceability >= 0 && target_danceability <= 1) {
            url = url + "&target_danceability=" + target_danceability;
        }
        if (target_energy >= 0 && target_energy <= 1) {
            url = url + "&target_energy=" + target_energy;
        }
        if (target_instrumentalness >= 0 && target_instrumentalness <= 1) {
            url = url + "&target_instrumentalness=" + target_instrumentalness;
        }
        if (target_liveness >= 0 && target_liveness <= 1) {
            url = url + "&target_liveness=" + target_liveness;
        }
        if (target_loudness >= 0 && target_loudness <= 1) {
            url = url + "&target_loudness=" + target_loudness;
        }
        if (target_popularity >= 0 && target_popularity <= 1) {
            url = url + "&target_popularity=" + target_popularity;
        }
        if (target_speechiness >= 0 && target_speechiness <= 1) {
            url = url + "&target_speechiness=" + target_speechiness;
        }
        if (target_valence >= 0 && target_valence <= 1) {
            url = url + "&target_valence=" + target_valence;
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

    public static StringRequest search (Response.Listener<String> listener, String token, String query, String market, String type) {

        String url = "https://api.spotify.com/v1/recommendations/?q=" + query + "&type=" + type + "&market=" + market;

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
}
