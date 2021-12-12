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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TrackInfo extends AppCompatActivity implements View.OnClickListener {

    String [] artists = {"NONE"};
    invTrack track = new invTrack("ID", "FAILED TO LOAD", artists, "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA");

    ImageView trackArt;
    ImageButton likeStatus;
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
        likeStatus = (ImageButton) findViewById(R.id.likeStatus);

        trackTitle = (TextView) findViewById(R.id.trackName);
        trackArtist = (TextView) findViewById(R.id.artistName);
        trackAlbumInfo = (TextView) findViewById(R.id.albumName);
        popularity = (TextView) findViewById(R.id.trackPopularity);
        danceability = (TextView) findViewById(R.id.trackDanceScore);
        tempo = (TextView) findViewById(R.id.trackTempoScore);
        liveness = (TextView) findViewById(R.id.trackLiveness);
        acousiticness = (TextView) findViewById(R.id.trackAcousticness);
        valence = (TextView) findViewById(R.id.trackValenceScore);

        // Testing an added click listener
        likeStatus.setOnClickListener(this);


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
            loadImage.execute(image);

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
        if (track.getLiked()) {
            likeStatus.setImageDrawable(Drawable.createFromPath("@android:drawable/btn_star_big_on"));
        } else {
            likeStatus.setImageDrawable(Drawable.createFromPath("@android:drawable/btn_star_big_off"));
        }
    }

//    public void searchClicked(View view) {
//        Intent intent = new Intent(this, Authenticated.class);
//        intent.putExtra("track", track.getID());
//        startActivity(intent);
//    }
//
//    public void likedClicked(View view) {
//        if (track.getLiked() == false) {
//            track.setLiked(true);
//            likeStatus.setImageDrawable(Drawable.createFromPath("@android:drawable/btn_star_big_on"));
//        } else {
//            track.setLiked(false);
//            likeStatus.setImageDrawable(Drawable.createFromPath("@android:drawable/btn_star_big_off"));
//        }
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.likeStatus:
                if (track.getLiked() == false) {
                    track.setLiked(true);
                    //likeStatus.setBackground(Drawable.createFromPath("@android:drawable/btn_star_big_on"));
                } else {
                    track.setLiked(false);
                    //likeStatus.setBackground(Drawable.createFromPath("@android:drawable/btn_star_big_off"));
                }
                break;

            case R.id.searchButton:
                Intent intent = new Intent(this, Authenticated.class);
                intent.putExtra("track", track.getID());
                startActivity(intent);
                break;
        }
    }

    private class LoadImage extends AsyncTask <String, Void, Bitmap> {
        ImageView art;

        public LoadImage(ImageView trackArt) {
            this.art = trackArt;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String link = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream stream = new URL(link).openStream();
                bitmap = BitmapFactory.decodeStream(stream);
                Log.e("bitmap", "FAILED TO BUILD BITMAP FROM INPUT STREAM");
            } catch (IOException e) {
                Log.e("bitmap", "FAILED TO BUILD BITMAP FROM INPUT STREAM");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("bitmap", "entered on execute, bitmap: " + bitmap.toString());
            trackArt.setImageBitmap(bitmap);
        }
    }
}