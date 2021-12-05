package com.example.inventum;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RemoteAPI {

    public static StringRequest getTrack(String token, String trackID, String market) {

        String url = "https://api.spotify.com/v1/tracks/" + trackID + "?market=" + market;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Authenticated", response.substring(0,500));
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("Authenticated", jsonObject.getJSONObject("album").getString("album_type"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Authenticated", "That didn't work!");

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
                Log.e("Authenticated", body);
            }
        }
        ) {
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

        // Add the request to the RequestQueue.
        return stringRequest;
    }

    public void getTracks() {
    //TODO: implement
    }

    public static StringRequest getTrackAudioFeatures(String token, String trackID) {

        String url = "https://api.spotify.com/v1/audio-features/" + trackID;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Authenticated", response.substring(0,500));
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("Authenticated", jsonObject.getString("danceability"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Authenticated", "That didn't work!");

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
                Log.e("Authenticated", body);
            }
        }
        ) {
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

        // Add the request to the RequestQueue.
        return stringRequest;
    }

    public void getTracksAudioFeatures() {
    //TODO: implement
    }

    public static StringRequest getUser(String token) {

        String url = "https://api.spotify.com/v1/me/";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Authenticated", response.substring(0,500));
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("Authenticated", jsonObject.getString("danceability"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Authenticated", "That didn't work!");

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
                Log.e("Authenticated", body);
            }
        }
        ) {
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

        // Add the request to the RequestQueue.
        return stringRequest;
    }
}
