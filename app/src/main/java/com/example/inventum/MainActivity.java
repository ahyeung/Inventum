package com.example.inventum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "fbbdb761618c4c6db1d1be2f596a6560";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";

    // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
    private static final int REQUEST_CODE = 1337;
    static String AUTH_TOKEN = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void authenticateUser(View v) {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming", "user-read-private", "user-top-read"});
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

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
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("token", AUTH_TOKEN).apply();
                    Intent connect = new Intent(MainActivity.this, Authenticated.class);
                    connect.putExtra("token", AUTH_TOKEN);
                    startActivity(connect);
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