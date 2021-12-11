package com.example.inventum;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class TrackInfo extends AppCompatActivity {

    String [] artists = {"NONE"};
    invTrack track = new invTrack("ID", "FAILED TO LOAD", artists, "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA");

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

        track = HomeFragment.trackList.get(getIntent().getIntExtra("trackPosition", 0));

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

        //Get meta data from invTrack track
        Drawable trackArtDrawable = LoadTrackURL(track.getImage_url());
        trackArt.setImageDrawable(trackArtDrawable);

        trackTitle.setText(track.getTitle());
        trackArtist.setText(track.getTrackArtistUI());
        trackAlbumInfo.setText(track.getAlbum());
        popularity.setText("Popularity: " + track.getPopularityScore());
        danceability.setText("Danceability: " + track.getDanceability());
        tempo.setText("Tempo: " + track.getTempo());
        liveness.setText("Liveness: " + track.getLiveness());
        acousiticness.setText("Acousticness: " + track.getAcousticness());
        valence.setText("Mood: " + track.getValence());



    }

    public static Drawable LoadTrackURL(String url) {
        try {
            InputStream stream = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(stream, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}