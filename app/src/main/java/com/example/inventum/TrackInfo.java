package com.example.inventum;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TrackInfo extends AppCompatActivity {

    invTrack track;

    ImageView trackArt;
    ImageView likeStatus;

    TextView trackTitle;
    TextView trackArtist;
    TextView trackAlbumInfo;
    TextView popularity;
    TextView danceability;
    TextView tempo;
    TextView liveness;
    TextView acousiticness;
    TextView valence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_info);

        if (getIntent().getExtras() != null) {
            track = (invTrack) getIntent().getSerializableExtra("user");
        }

        trackArt = (ImageView) findViewById(R.id.trackArtView);
        likeStatus = (ImageView) findViewById(R.id.likeStatus);

        trackTitle = (TextView) findViewById(R.id.trackName);
        trackArtist = (TextView) findViewById(R.id.artistName);
        trackAlbumInfo = (TextView) findViewById(R.id.albumName);
        popularity = (TextView) findViewById(R.id.trackPopularity);
        danceability = (TextView) findViewById(R.id.trackDanceScore);
        tempo = (TextView) findViewById(R.id.trackTempoScore);
        liveness = (TextView) findViewById(R.id.trackLiveness);
        acousiticness = (TextView) findViewById(R.id.trackAcousticness);
        valence = (TextView) findViewById(R.id.trackValenceScore);

//        String string = "";
//        String [] sArray = new String[] {""};
//
//        invTrack track = new invTrack(string, string, sArray, string, string, string, string,
//                string, string, string, string, string, string,
//                string, string, string, string);

        //Get String from inventum object
        trackTitle.setText(track.getTitle());
        trackArtist.setText(track.getTrackArtistUI());
        trackAlbumInfo.setText(track.getAlbum());
        popularity.setText(track.getPopularityScore());

    }
}