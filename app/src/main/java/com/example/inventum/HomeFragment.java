package com.example.inventum;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    JSONObject TOP_TRACKS;
    public static ArrayList<invTrack> trackList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Home", response.substring(0,500));
                try {
                    TOP_TRACKS = new JSONObject(response);
                    Log.d("Home", TOP_TRACKS.toString());

                    String trackIDs = RemoteAPI.parseTopTracksResponse(TOP_TRACKS);
                    Log.d("Home", trackIDs);

                    final JSONObject[] tracks = {null};
                    final JSONObject[] tracks_expanded = {null};

                    Response.Listener<String> track_listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("Authenticated", response.substring(0,500));
                            try {
                                tracks[0] = new JSONObject(response);

                                Response.Listener<String> expanded_listener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        Log.d("Authenticated", response.substring(0,100));
                                        try {
                                            tracks_expanded[0] = new JSONObject(response);

                                            ArrayList<invTrack> trackArrayList = new ArrayList<>();


                                            JSONArray tracksArray = tracks[0].getJSONArray("tracks");
                                            JSONArray expandedArray = tracks_expanded[0].getJSONArray("audio_features");
                                            for (int i = 0; i < tracksArray.length(); i ++) {
                                                JSONObject trackObject = tracksArray.getJSONObject(i);
                                                JSONObject expandedObject = expandedArray.getJSONObject(i);

                                                // Get artists and put into string array
                                                String[] artists = new String[10];
                                                JSONArray artistArray = trackObject.getJSONArray("artists");
                                                for (int j = 0; j < artistArray.length(); j++) {
                                                    artists[j] = artistArray.getJSONObject(j).getString("name");
                                                }

                                                // Parse the two responses to get the desired information
                                                trackArrayList.add(new invTrack(
                                                        trackObject.getString("id"),
                                                        trackObject.getString("name"),
                                                        artists,
                                                        trackObject.getJSONObject("album").getString("name"),
                                                        trackObject.getJSONObject("album").getString("type"),
                                                        trackObject.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"),
                                                        expandedObject.getString("energy"),
                                                        expandedObject.getString("danceability"),
                                                        expandedObject.getString("liveness"),
                                                        expandedObject.getString("acousticness"),
                                                        trackObject.getString("popularity"),
                                                        expandedObject.getString("valence"),
                                                        expandedObject.getString("tempo"),
                                                        expandedObject.getString("speechiness"),
                                                        expandedObject.getString("loudness"),
                                                        expandedObject.getString("instrumentalness"),
                                                        trackObject.getString("duration_ms")
                                               ));
                                            }

                                            trackList = trackArrayList;

                                            ArrayList<String> displayTracks = new ArrayList<>();
                                            for (invTrack track : trackList) {
                                                displayTracks.add(String.format("Title: %s\nArtist: %s Album: %s", track.getTitle(), track.getTrackArtist()[0], track.getAlbum()));
                                            }

                                            // Use ListView to display notes
                                            ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, displayTracks);
                                            ListView listView = (ListView) getActivity().findViewById(R.id.tracksListView);
                                            listView.setAdapter(adapter);

                                            // Add onItemClickListener
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Log.d("ItemClick", Integer.toString(position));
                                                    Log.d("ItemClick", Integer.toString(((int)id)));
                                                    invTrack trackExtra = trackList.get(position);
                                                    Intent intent = new Intent(getActivity().getApplicationContext(), TrackInfo.class);
                                                    intent.putExtra("trackPosition", position);
                                                    startActivity(intent);
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                StringRequest expandedRequest = RemoteAPI.getTracksAudioFeatures(expanded_listener, Authenticated.AUTH_TOKEN, trackIDs);
                                queue.add(expandedRequest);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    StringRequest trackRequest = RemoteAPI.getTracks(track_listener, Authenticated.AUTH_TOKEN, trackIDs, Authenticated.MARKET);
                    StringRequest tracks_response = RemoteAPI.getTracks(track_listener, Authenticated.AUTH_TOKEN, trackIDs, Authenticated.MARKET);
                    queue.add(trackRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest = RemoteAPI.getGlobalTopSongs(listener, Authenticated.AUTH_TOKEN, Authenticated.MARKET);
        queue.add(stringRequest);



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}