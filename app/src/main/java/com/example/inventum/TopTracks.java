package com.example.inventum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.LinkedList;

public class TopTracks extends AppCompatActivity {

    ArrayList <invTrack> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        RecyclerView tracksNav = (RecyclerView) findViewById(R.id.tracksView);

        initTracks(20);

        TracksAdapter adapter = new TracksAdapter(tracks);

        tracksNav.setAdapter(adapter);
        tracksNav.setLayoutManager(new LinearLayoutManager(this));

    }

    public void initTracks(int numTracks) {

        //might be doubling up but who cares
        String title = "SAMPLE TITLE";
        String artist = "SAMPLE ARTIST";

        for (int i = 0; i < numTracks; i++) {
            tracks.add(new invTrack(title, artist));
        }
    }

}