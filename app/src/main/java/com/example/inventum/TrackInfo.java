package com.example.inventum;

import androidx.annotation.BinderThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputBinding;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TrackInfo extends AppCompatActivity {

    String [] artists = {"NONE"};
    invTrack track = new invTrack("ID", "FAILED TO LOAD", artists, "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA");

    ScriptGroup.Binding binding;
    Handler trackHandler;
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

        track = Authenticated.trackList.get(getIntent().getIntExtra("trackPosition", 0));

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
        //Glide.with(this).load(track.getImage_url()).into(trackArt);
        try {
            String image = track.getImage_url();

            if (image.equals(null)) {
                Log.e("message", "STRING URL RETURNS NULL");
            } else {
                Log.e("message", "STRING" + track.getImage_url());
            }

            InputStream stream = new URL(image).openStream();
            LoadImage loadImage = new LoadImage(trackArt);
            loadImage.execute(track.getImage_url());

        } catch (Exception e) {
            trackArt.setImageDrawable(Drawable.createFromPath("@android:drawable/btn_star_big_on"));
        }

        trackTitle.setText(track.getTitle());
        trackArtist.setText(track.getTrackArtistUI());
        trackAlbumInfo.setText(track.getAlbumType() + ", " + track.getAlbum());
        popularity.setText("Popularity: " + track.getPopularityScore());
        danceability.setText("Danceability: " + track.getDanceability());
        tempo.setText("Tempo: " + track.getTempo());
        liveness.setText("Liveness: " + track.getLiveness());
        acousiticness.setText("Acousticness: " + track.getAcousticness());
        valence.setText("Mood: " + track.getValence());

        //Check whether liked song
        boolean liked = true;

        if (liked) {
            likeStatus.setImageDrawable(Drawable.createFromPath("@android:drawable/btn_star_big_on"));
        }

    }

    public void searchClicked(View view) {
        Intent intent = new Intent(this, Authenticated.class);
        intent.putExtra("track", track.getID());
        startActivity(intent);
    }

    private class LoadImage extends AsyncTask <String, Void, Bitmap> {
        ImageView art;

        public LoadImage(ImageView trackArt) {
            art = trackArt;

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String link = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream stream = new URL(link).openStream();
                bitmap = BitmapFactory.decodeStream(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            art.setImageBitmap(bitmap);
        }
    }
}