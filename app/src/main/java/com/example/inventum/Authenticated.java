package com.example.inventum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationBarView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Authenticated extends AppCompatActivity {

    private static final String CLIENT_ID = "fbbdb761618c4c6db1d1be2f596a6560";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private NavigationBarView bottomNavigationView;

    static String AUTH_TOKEN = "";
    private static final int REQUEST_CODE = 1337;
    static String MARKET = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticated);

        bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setOnItemSelectedListener(bottomnavFunction);

        Intent intent = getIntent();
        AUTH_TOKEN = intent.getStringExtra("token");
        Log.w("Authenticated", "///////////TOKEN///////// " + AUTH_TOKEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });

        // Get user info
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Authenticated", response.substring(0,50));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    MARKET = jsonObject.getString("country");
                    Log.d("Authenticated", "TESTING " + MARKET);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest = RemoteAPI.getUser(listener, AUTH_TOKEN);
        queue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });

        // Refresh token after every 3500 seconds
        Runnable helloRunnable = new Runnable() {
            public void run() {
                AuthorizationRequest.Builder builder =
                        new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

                builder.setScopes(new String[]{"streaming", "user-read-private"});
                AuthorizationRequest request = builder.build();

                AuthorizationClient.openLoginActivity(Authenticated.this, REQUEST_CODE, request);

                Log.d("Authenticated","---------------------- Hello world");
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3500, TimeUnit.SECONDS);

        // Instantiate RequestQueue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Get needed track info for the request
        String trackID = "11dFghVXANMlKmJXsNCbNl";
        String market = MARKET;

        final String[] testString = {"test"};

        // Determine what to do with the response
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Authenticated", response.substring(0,500));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    testString[0] = jsonObject.getJSONObject("album").getString("album_type");
                    Log.d("Authenticated", "TESTING " + testString[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.Listener<String> listener1 = new Response.Listener<String>() {
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
        };

        // Get the requested track
        StringRequest stringRequest = RemoteAPI.getTrack(listener, AUTH_TOKEN, trackID, market);

        // Get track features
        StringRequest stringRequest1 = RemoteAPI.getTrackAudioFeatures(listener1, AUTH_TOKEN, trackID);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.add(stringRequest1);
    }

    private NavigationBarView.OnItemSelectedListener bottomnavFunction = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.search:
                    fragment = new SearchFragment();
                    break;
                case R.id.user:
                    fragment = new UserFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.d("MyActivity", "Connected");
                    AUTH_TOKEN = response.getAccessToken();
                    Log.d("MyActivity", "Token: " + AUTH_TOKEN);
                    Log.w("MyActivity", "-------------- Expires in: " + response.getExpiresIn());
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.e("MyActivity", "Connection error");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    Log.e("MyActivity", "Could not connect");
            }
        }
    }
}