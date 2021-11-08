package com.example.inventum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "fbbdb761618c4c6db1d1be2f596a6560";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private NavigationBarView bottomNavigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void authenticateUser(View v) {
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
                            //connected();
                            Intent intent = new Intent(getApplicationContext(), Authenticated.class);
                            startActivity(intent);

                        }

                        public void onFailure(Throwable throwable) {
                            Log.e("MyActivity", throwable.getMessage(), throwable);

                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });
    }
}