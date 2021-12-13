package com.example.inventum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.inventum.Authenticated.trackList;

public class UserFragment extends Fragment implements View.OnClickListener {
    JSONObject TOP_USER_TRACKS;
    String trackIDs;
    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ToggleButton toggle = view.findViewById(R.id.toggleButton);
        toggle.setOnClickListener(this);
        populate_top_tracks(view);


        return view;
    }

    @Override
    public void onClick(View view) {
        ToggleButton toggle = view.findViewById(R.id.toggleButton);
        switch(view.getId()){
            case R.id.toggleButton:
                String text = (String) toggle.getText();
                if(text.equals("Starred")){
                    SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
                    String trackIDs = sharedPreferences.getString("likedArray", "");
                    if(trackIDs != null && trackIDs.length() > 0){
                        populate_liked(view);
                    }else{
                        ArrayList <String> empty_memo = new ArrayList<String>();
                        empty_memo.add("Please like a song!");
                        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, empty_memo);
                        ListView listView = (ListView) getActivity().findViewById(R.id.UserListView);
                        listView.setAdapter(adapter);
                    }
                    getView().findViewById(R.id.UserListView).setVisibility(View.INVISIBLE);
                    getView().findViewById(R.id.StarredListView).setVisibility(View.VISIBLE);
                }
                if(text.equals("YOUR TOP TRACKS")){
                    Log.d("UserFrag", "Text: " + text);
                    populate_top_tracks(view);
                    getView().findViewById(R.id.StarredListView).setVisibility(View.INVISIBLE);
                    getView().findViewById(R.id.UserListView).setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void populate_top_tracks(View view) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Home", response.substring(0,500));
                try {
                    TOP_USER_TRACKS = new JSONObject(response);
                    Log.d("userfrag*", TOP_USER_TRACKS.toString());

                    String trackIDs = RemoteAPI.parseUserTracksResponse(TOP_USER_TRACKS);
                    Log.d("userfrag", trackIDs);

                    final JSONObject[] tracks = {null};
                    final JSONObject[] tracks_expanded = {null};

                    Response.Listener<String> track_listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("UserTracks", response.substring(0,500));
                            try {
                                tracks[0] = new JSONObject(response);

                                Response.Listener<String> expanded_listener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        Log.d("UserTracks", response.substring(0,100));
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
                                            ListView listView = (ListView) getActivity().findViewById(R.id.UserListView);
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
                                StringRequest expandedRequest = RemoteAPI.getTracksAudioFeatures(expanded_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs);
                                queue.add(expandedRequest);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    StringRequest trackRequest = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
                    StringRequest tracks_response = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
                    queue.add(trackRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest = RemoteAPI.getUserTracks(listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN));
        queue.add(stringRequest);
    }


    public void populate_liked(View view){
        // Log.d("populate", "entering the liked!");
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
        trackIDs = sharedPreferences.getString("likedArray", "");

        // NOTE: Api error likely here
        if(trackIDs.charAt(trackIDs.length()-1) == ','){
            trackIDs = trackIDs.substring(0, trackIDs.length()-1); // Removes our last char
        }

        Log.d("populate", "Track IDs " + trackIDs);

        final JSONObject[] tracks = {null};
        final JSONObject[] tracks_expanded = {null};
        //String finalTrackIDs = trackIDs;

        Response.Listener<String> track_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("StarredTracks", response.substring(0,500));
                try {
                    tracks[0] = new JSONObject(response);

                    Response.Listener<String> expanded_listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("StarredTracks", response.substring(0,100));
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
                                ListView listView = (ListView) getActivity().findViewById(R.id.StarredListView);
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
                    StringRequest expandedRequest = RemoteAPI.getTracksAudioFeatures(expanded_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs);
                    queue.add(expandedRequest);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest trackRequest = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
        StringRequest tracks_response = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
        queue.add(trackRequest);

    }
}