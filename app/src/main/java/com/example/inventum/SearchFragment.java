package com.example.inventum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment implements View.OnClickListener {

    static String MARKET = "";
    SharedPreferences sharedPreferences;
    public static ArrayList<String> idList = new ArrayList<>();
    public static ArrayList<String> itemsList = new ArrayList<>();
    public static ArrayList<String> genreList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = this.getActivity().getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        Button findR = (Button) v.findViewById(R.id.findResults);
        Button genreButton1 = (Button) v.findViewById(R.id.genreButton1);
        Button genreButton2 = (Button) v.findViewById(R.id.genreButton2);
        Button genreButton3 = (Button) v.findViewById(R.id.genreButton3);
        Button genreButton4 = (Button) v.findViewById(R.id.genreButton4);
        Button genreButton5 = (Button) v.findViewById(R.id.genreButton5);
        findR.setOnClickListener(this);
        genreButton1.setOnClickListener(this);
        genreButton2.setOnClickListener(this);
        genreButton3.setOnClickListener(this);
        genreButton4.setOnClickListener(this);
        genreButton5.setOnClickListener(this);

        genreList.add("");
        genreList.add("");
        genreList.add("");
        genreList.add("");
        genreList.add("");

        ToggleButton toggle = (ToggleButton) v.findViewById(R.id.toggleButton);
        toggle.setChecked(true);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText keywords = (EditText) getActivity().findViewById(R.id.searchBar);
                EditText genres = (EditText) getActivity().findViewById(R.id.genreIncSearch);
                Button findGenreResults = (Button) getActivity().findViewById(R.id.findGenreResults);
                Button findAdvGenreResults = (Button) getActivity().findViewById(R.id.findAdvGenreResults);
                Button findResults = (Button) getActivity().findViewById(R.id.findResults);

                if (isChecked) {
                    Log.d("TRUE", "SONGS/ARTISTS************");
                    TextView searchTitle = (TextView) getActivity().findViewById(R.id.basicSearch);
                    searchTitle.setText("Songs/Artists");
                    keywords.setVisibility(View.VISIBLE);
                    genres.setVisibility(View.INVISIBLE);
                    findGenreResults.setVisibility(View.INVISIBLE);
                    findAdvGenreResults.setVisibility(View.INVISIBLE);
                    findResults.setVisibility(View.VISIBLE);

                } else {
                    Log.d("FALSE", "GENRES************");
                    TextView searchTitle = (TextView) getActivity().findViewById(R.id.basicSearch);
                    searchTitle.setText("Genres");
                    keywords.setVisibility(View.INVISIBLE);
                    genres.setVisibility(View.VISIBLE);
                    findGenreResults.setVisibility(View.VISIBLE);
                    findAdvGenreResults.setVisibility(View.VISIBLE);
                    findResults.setVisibility(View.INVISIBLE);
                }
            }
        });

        AutoCompleteTextView autocomplete = (AutoCompleteTextView) v.findViewById(R.id.genreIncSearch);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Authenticated.genres);

        Log.d("Genres", Arrays.toString(Authenticated.genres));
        autocomplete.setThreshold(2);
        autocomplete.setAdapter(adapter);
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Generate button for every genre selected, no duplicates
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText searchBar = (EditText) getActivity().findViewById(R.id.genreIncSearch);

                // fill button if empty with selected genre
                if (genreButton1.getText().equals("")) {
                    genreButton1.setText(searchBar.getText());
                    genreButton1.setVisibility(View.VISIBLE);
                    SearchFragment.genreList.set(0, searchBar.getText().toString());
                    searchBar.setText("");
                } else if (genreButton2.getText().equals("")) {
                    genreButton2.setText(searchBar.getText());

                    if (genreButton2.getText().toString().equals(genreButton1.getText().toString())) {
                        genreButton2.setText("");
                    } else {
                        genreButton2.setVisibility(View.VISIBLE);
                        SearchFragment.genreList.set(1, searchBar.getText().toString());
                        searchBar.setText("");
                    }
                } else if (genreButton3.getText().equals("")) {
                    genreButton3.setText(searchBar.getText());

                    if (genreButton3.getText().toString().equals(genreButton1.getText().toString()) ||
                            genreButton3.getText().toString().equals(genreButton2.getText().toString())) {
                        genreButton3.setText("");
                    } else {
                        genreButton3.setVisibility(View.VISIBLE);
                        SearchFragment.genreList.set(2, searchBar.getText().toString());
                        searchBar.setText("");
                    }
                } else if (genreButton4.getText().equals("")) {
                    genreButton4.setText(searchBar.getText());

                    if (genreButton4.getText().toString().equals(genreButton1.getText().toString()) ||
                            genreButton4.getText().toString().equals(genreButton2.getText().toString()) ||
                            genreButton4.getText().toString().equals(genreButton3.getText().toString())) {
                        genreButton4.setText("");
                    } else {
                        genreButton4.setVisibility(View.VISIBLE);
                        SearchFragment.genreList.set(3, searchBar.getText().toString());
                        searchBar.setText("");
                    }
                } else if (genreButton5.getText().equals("")) {
                    genreButton5.setText(searchBar.getText());

                    if (genreButton5.getText().toString().equals(genreButton1.getText().toString()) ||
                            genreButton5.getText().toString().equals(genreButton2.getText().toString()) ||
                            genreButton5.getText().toString().equals(genreButton3.getText().toString()) ||
                            genreButton5.getText().toString().equals(genreButton4.getText().toString())) {
                        genreButton5.setText("");
                    } else {
                        genreButton5.setVisibility(View.VISIBLE);
                        SearchFragment.genreList.set(4, searchBar.getText().toString());
                        searchBar.setText("");
                    }
                }

                for(int i = 0; i < SearchFragment.genreList.size(); i++) {
                    Log.d("genre list", "Genre List: " + i + SearchFragment.genreList.get(i));
                }
            }
        });

        Log.d("SearchToken", ">>>>>>>>>>>>>>>>>TOKEN: " + sharedPreferences.getString("token", RemoteAPI.TOKEN));
        Log.d("TrackSearchFragment", "ID: " + Authenticated.ID_EXTRA);
        if (Authenticated.ID_EXTRA != null && !Authenticated.ID_EXTRA.isEmpty()) {
            trackSearch(Authenticated.ID_EXTRA);
            Authenticated.ID_EXTRA = "";
        }

        return v;
    }

    public void onClick(View view) {
        EditText eV1 = (EditText) getView().findViewById(R.id.searchBar);
        switch (view.getId()) {
            case R.id.findResults:
                String searchStr = eV1.getText().toString();
                initialSearch(searchStr);
                break;
            case R.id.genreButton1:
                Log.d("genreButton1", "REMOVE BUTTON 1");

                Button genreButton1 = (Button) getActivity().findViewById(R.id.genreButton1);
                genreButton1.setText("");
                genreList.set(0, "");
                genreButton1.setVisibility(View.INVISIBLE);

                // test arraylist
                for(int i = 0; i < SearchFragment.genreList.size(); i++) {
                    Log.d("Genre List Array", i + SearchFragment.genreList.get(i));
                }
                break;
            case R.id.genreButton2:
                Log.d("genreButton2", "REMOVE BUTTON 2");

                Button genreButton2 = (Button) getActivity().findViewById(R.id.genreButton2);
                genreButton2.setText("");
                genreList.set(1, "");
                genreButton2.setVisibility(View.INVISIBLE);

                // test arraylist
                for(int i = 0; i < SearchFragment.genreList.size(); i++) {
                    Log.d("Genre List Array", i + SearchFragment.genreList.get(i));
                }
                break;
            case R.id.genreButton3:
                Log.d("genreButton3", "REMOVE BUTTON 3");

                Button genreButton3 = (Button) getActivity().findViewById(R.id.genreButton3);
                genreButton3.setText("");
                genreList.set(2, "");
                genreButton3.setVisibility(View.INVISIBLE);

                // test arraylist
                for(int i = 0; i < SearchFragment.genreList.size(); i++) {
                    Log.d("Genre List Array", i + SearchFragment.genreList.get(i));
                }
                break;
            case R.id.genreButton4:
                Log.d("genreButton4", "REMOVE BUTTON 4");

                Button genreButton4 = (Button) getActivity().findViewById(R.id.genreButton4);
                genreButton4.setText("");
                genreList.set(3, "");
                genreButton4.setVisibility(View.INVISIBLE);

                // test arraylist
                for(int i = 0; i < SearchFragment.genreList.size(); i++) {
                    Log.d("Genre List Array", i + SearchFragment.genreList.get(i));
                }
                break;
            case R.id.genreButton5:
                Log.d("genreButton5", "REMOVE BUTTON 5");

                Button genreButton5 = (Button) getActivity().findViewById(R.id.genreButton5);
                genreButton5.setText("");
                genreList.set(4, "");
                genreButton5.setVisibility(View.INVISIBLE);

                // test arraylist
                for(int i = 0; i < SearchFragment.genreList.size(); i++) {
                    Log.d("Genre List Array", i + SearchFragment.genreList.get(i));
                }
                break;
            default:
                break;
        }
    }

    public void initialSearch(String s) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        itemsList = new ArrayList<>();
        idList = new ArrayList<>();

        // Populate ListView
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Search", response.substring(0, 500));

                    JSONArray artists = jsonObject.getJSONObject("artists").getJSONArray("items");
                    JSONArray tracks = jsonObject.getJSONObject("tracks").getJSONArray("items");

                    for (int i = 0; i < tracks.length(); i ++) {
                        JSONObject trackObject = tracks.getJSONObject(i);

                        itemsList.add("Song: " + trackObject.getString("name") + " by " + trackObject.getJSONArray("artists").getJSONObject(0).getString("name"));
                        idList.add(trackObject.getString("id"));
                    }

                    for (int i = 0; i < artists.length(); i ++) {
                        JSONObject trackObject = artists.getJSONObject(i);

                        itemsList.add("Artist: " + trackObject.getString("name"));
                        idList.add(trackObject.getString("id"));
                    }

                    // use ListView to display results
                    ListView listView = (ListView) getView().findViewById(R.id.tracksListView);
                    listView.setAdapter(new CustomAdapter(itemsList, getActivity().getApplicationContext(), getActivity()));

                    getView().findViewById(R.id.tracksListView).setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest =
                RemoteAPI.search(listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), s, MARKET, "track,artist");
        queue.add(stringRequest);
    }

    public void trackSearch(String id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Response.Listener<String> search_listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("Authenticated", response.substring(0,50));
                try {
                    JSONObject tracksObject = new JSONObject(response);
                    Log.d("Home", tracksObject.toString());

                    String trackIDs = RemoteAPI.parseRecommendationsResult(tracksObject);
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

                                            Authenticated.trackList = trackArrayList;

                                            ArrayList<String> displayTracks = new ArrayList<>();
                                            for (invTrack track : Authenticated.trackList) {
                                                displayTracks.add(String.format("Title: %s\nArtist: %s Album: %s", track.getTitle(), track.getTrackArtist()[0], track.getAlbum()));
                                            }

                                            // Use ListView to display notes
                                            ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, displayTracks);
                                            ListView listView = (ListView) getActivity().findViewById(R.id.resultsList);
                                            listView.setAdapter(adapter);

                                            // Add onItemClickListener
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Log.d("ItemClick", Integer.toString(position));
                                                    Log.d("ItemClick", Integer.toString(((int)id)));
                                                    invTrack trackExtra = Authenticated.trackList.get(position);
                                                    Intent intent = new Intent(getActivity().getApplicationContext(), TrackInfo.class);
                                                    intent.putExtra("trackPosition", position);
                                                    getActivity().startActivity(intent);
                                                }
                                            });

                                            Log.d("Search", "HERE HERE");
                                            getView().findViewById(R.id.resultsList).setVisibility(View.VISIBLE);
                                            getView().findViewById(R.id.tracksListView).setVisibility((View.INVISIBLE));
                                            getView().findViewById(R.id.searchBar).setVisibility((View.INVISIBLE));
                                            getView().findViewById(R.id.toggleButton).setVisibility((View.INVISIBLE));
                                            getView().findViewById(R.id.basicSearch).setVisibility((View.INVISIBLE));
                                            getView().findViewById(R.id.findResults).setVisibility(View.INVISIBLE);
                                            getView().findViewById(R.id.findGenreResults).setVisibility(View.INVISIBLE);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                StringRequest expandedRequest = RemoteAPI.getTracksAudioFeatures(expanded_listener, sharedPreferences.getString("token", RemoteAPI.TOKEN), trackIDs);
                                queue.add(expandedRequest);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    StringRequest trackRequest = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", RemoteAPI.TOKEN), trackIDs, Authenticated.MARKET);
                    StringRequest tracks_response = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
                    queue.add(trackRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest searchRequest = RemoteAPI.getRecommendations(search_listener, sharedPreferences.getString("token", RemoteAPI.TOKEN), "", "", id,
                Authenticated.MARKET, 10, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1);
        queue.add(searchRequest);
    }
}