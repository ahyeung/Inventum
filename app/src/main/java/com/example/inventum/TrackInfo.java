package com.example.inventum;

import androidx.annotation.BinderThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

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
                if (track.getLiked() == false) {
                    track.setLiked(true);
                    likeStatus.setText("LIKED");
                    // Add id to giant string from old preferences and update
                    String track_id = track.getID();
                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
                    try{
                        //String likedList = sharedPreferences.getString("likedArray", ).toString();


                        //sharedPreferences.edit().putString("likedArray", likedList).apply();
                    } catch(Exception e){

                    }

                    // sharedPreferences.edit().putString("token", AUTH_TOKEN).apply();
                } else {
                    track.setLiked(false);
                    likeStatus.setText("NOT LIKED");
                }
                break;

            case R.id.searchButton:
                Intent intent = new Intent(this, Authenticated.class);
                intent.putExtra("track", track.getID());
                startActivity(intent);
                break;
        }
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