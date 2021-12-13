package com.example.inventum;

import androidx.annotation.BinderThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputBinding;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

public class TrackInfo extends AppCompatActivity implements View.OnClickListener {

    String [] artists = {"NONE"};
    invTrack track = new invTrack("ID", "FAILED TO LOAD", artists, "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA", "NO DATA",
            "NO DATA", "NO DATA", "NO DATA", "NO DATA");

    ImageView trackArt;
    Button likeStatus;
    TextView trackTitle;
    TextView trackArtist;
    TextView trackAlbumInfo;
    TextView popularity;
    TextView danceability;
    TextView tempo;
    TextView liveness;
    TextView acousiticness;
    TextView valence;
    TextView speechiness;
    TextView energy;
    TextView instrumentalness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_info);

        track = Authenticated.trackList.get(getIntent().getIntExtra("trackPosition", 0));

        trackArt = (ImageView) findViewById(R.id.trackArtView);
        likeStatus = (Button) findViewById(R.id.likeStatus);

        trackTitle = (TextView) findViewById(R.id.trackName);
        trackArtist = (TextView) findViewById(R.id.artistName);
        trackAlbumInfo = (TextView) findViewById(R.id.albumName);
        popularity = (TextView) findViewById(R.id.trackPopularity);
        danceability = (TextView) findViewById(R.id.trackDanceScore);
        tempo = (TextView) findViewById(R.id.trackTempoScore);
        liveness = (TextView) findViewById(R.id.trackLiveness);
        acousiticness = (TextView) findViewById(R.id.trackAcousticness);
        valence = (TextView) findViewById(R.id.trackValenceScore);
        energy = (TextView) findViewById(R.id.trackEnergy);
        speechiness = (TextView) findViewById(R.id.trackSpeechiness);
        instrumentalness = (TextView) findViewById(R.id.trackInstrumentalness);



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

            new ImageLoadTask(image, trackArt).execute();
            trackArt.setImageBitmap(getBitmapFromURL(image));

        } catch (Exception e) {
            trackArt.setImageDrawable(Drawable.createFromPath("@android:drawable/btn_star_big_on"));
        }

        String title = track.getTitle();

        if (title.length() > 20) {
            trackTitle.setTextSize(18);
        }

        int size = 20;
        boolean shrink = false;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        Log.e("WIDTH", "" + width);

        if (width <= 1080) {
            shrink = true;
            size = 10;
        } else {
            size = 20;
            shrink = false;
        }

        trackTitle.setText(track.getTitle());
        trackArtist.setText(track.getTrackArtistUI());

        if (shrink) {
            trackTitle.setTextSize(20);
            trackArtist.setTextSize(20);
        }

        trackAlbumInfo.setText(track.getAlbumType() + ": " + track.getAlbum());
        trackAlbumInfo.setTextSize(size);

        if (shrink) {
            trackAlbumInfo.setWidth(400);
        }

        popularity.setText("Popularity: " + track.getPopularityScore());
        popularity.setTextSize(size);
        danceability.setText("Danceability: " + track.getDanceability());
        danceability.setTextSize(size);
        tempo.setText("Tempo: " + track.getTempo());
        tempo.setTextSize(size);
        liveness.setText("Liveness: " + track.getLiveness());
        liveness.setTextSize(size);
        acousiticness.setText("Acousticness: " + track.getAcousticness());
        acousiticness.setTextSize(size);
        valence.setText("Mood: " + track.getValence());
        valence.setTextSize(size);
        speechiness.setText("Speechiness: " + track.getSpeechiness());
        speechiness.setTextSize(size);
        energy.setText("Energy: " + track.getEnergy());
        energy.setTextSize(size);
        instrumentalness.setText("Instrumental: " + track.getInstrumentalness());
        instrumentalness.setTextSize(size);

        //Check whether liked song
        if (checkLikeStatus()) {
            likeStatus.setText("LIKED");
        } else {
            likeStatus.setText("NOT LIKED");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.likeStatus:
                if (!checkLikeStatus()) {
                    likeStatus.setText("LIKED");
                    // Add id to giant string from old preferences and update
                    String track_id = track.getID();
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
                    String likedList = sharedPreferences.getString("likedArray", "");
                    likedList = likedList + track_id + ",";
                    sharedPreferences.edit().putString("likedArray", likedList).apply();
                } else {
                    likeStatus.setText("NOT LIKED");
                    String track_id = track.getID();
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
                    String likedList = sharedPreferences.getString("likedArray", "");
                    String new_list = "";
                    String[] likedIds = likedList.split(",");

                    for(int i = 0; i < likedIds.length; i++){
                        if(!likedIds[i].equals(track_id)){
                            new_list = new_list + likedIds[i] + ",";
                        }
                    }
                    // New list without the unliked song
                    sharedPreferences.edit().putString("likedArray", new_list).apply();
                }
                break;

            case R.id.searchButton:
                Intent intent = new Intent(this, Authenticated.class);
                intent.putExtra("track", track.getID());
                startActivity(intent);
                break;

            case R.id.backButton:
                finish();
        }
    }

    public boolean checkLikeStatus() {

        String track_id = track.getID();
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
        String likedList = sharedPreferences.getString("likedArray", "");
        String new_list = "";
        String[] likedIds = likedList.split(",");

        for(int i = 0; i < likedIds.length; i++){
            if(likedIds[i].equals(track_id)){
                return true;
            }
        }

        return false;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

}