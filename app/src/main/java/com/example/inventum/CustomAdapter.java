package com.example.inventum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SeekBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private Context context;
    private Activity activity;
    SharedPreferences sharedPreferences;

    private String id;
    private int tag;

    public CustomAdapter(ArrayList<String> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("com.example.inventum", Context.MODE_PRIVATE);

    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.results_layout, null);
        }

        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.trackInfo);
        tvContact.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button simpleSearch = (Button)view.findViewById(R.id.simpleSearch);
        simpleSearch.setTag(position);

        simpleSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Search", SearchFragment.idList.get((Integer)v.getTag()));

                //SearchFragment.simpleSearch(v);
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

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
                                                    ArrayAdapter adapter = new ArrayAdapter(context.getApplicationContext(), android.R.layout.simple_list_item_1, displayTracks);
                                                    ListView listView = (ListView) activity.findViewById(R.id.resultsList);
                                                    listView.setAdapter(adapter);

                                                    // Add onItemClickListener
                                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            Log.d("ItemClick", Integer.toString(position));
                                                            Log.d("ItemClick", Integer.toString(((int)id)));
                                                            invTrack trackExtra = Authenticated.trackList.get(position);
                                                            Intent intent = new Intent(context.getApplicationContext(), TrackInfo.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.putExtra("trackPosition", position);
                                                            context.startActivity(intent);
                                                        }
                                                    });

                                                    activity.findViewById(R.id.resultsList).setVisibility(View.VISIBLE);
                                                    activity.findViewById(R.id.tracksListView).setVisibility((View.INVISIBLE));
                                                    activity.findViewById(R.id.searchBar).setVisibility((View.INVISIBLE));
                                                    activity.findViewById(R.id.toggleButton).setVisibility((View.INVISIBLE));
                                                    activity.findViewById(R.id.basicSearch).setVisibility((View.INVISIBLE));
                                                    activity.findViewById(R.id.findResults).setVisibility(View.INVISIBLE);
                                                    activity.findViewById(R.id.findGenreResults).setVisibility(View.INVISIBLE);
                                                    activity.findViewById(R.id.findAdvGenreResults).setVisibility(View.INVISIBLE);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                                        StringRequest expandedRequest = RemoteAPI.getTracksAudioFeatures(expanded_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs);
                                        queue.add(expandedRequest);



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                            StringRequest trackRequest = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
                            StringRequest tracks_response = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
                            queue.add(trackRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                String artist = "";
                String track = "";
                if ((Integer)v.getTag() < 5) {
                    track = SearchFragment.idList.get((Integer)v.getTag());
                } else {
                    artist = SearchFragment.idList.get((Integer)v.getTag());
                }
                StringRequest searchRequest = RemoteAPI.getRecommendations(search_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), "", artist, track,
                        Authenticated.MARKET, 10, -1, -1, -1, -1, -1,
                        -1, -1, -1, -1, -1, -1);
                queue.add(searchRequest);

                String info = SearchFragment.itemsList.get((Integer)v.getTag());
                TextView name = (TextView) activity.findViewById(R.id.advSearchResult);
                name.setText("Based on " + info);
                activity.findViewById(R.id.advSearchResult).setVisibility(View.VISIBLE);
            }
        });

        Button advancedSearch = (Button) view.findViewById(R.id.advancedSearch);
        advancedSearch.setTag(position);

        advancedSearch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("Search", SearchFragment.itemsList.get((Integer)v.getTag()));
                String info = SearchFragment.itemsList.get((Integer)v.getTag());

                TextView name = (TextView) activity.findViewById(R.id.advSearchResult);
                name.setText("Based on: " + info);
                activity.findViewById(R.id.advSearchResult).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.popularityTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.popularityBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.danceabilityTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.danceabilityBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.tempoTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.tempoBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.livenessTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.livenessBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.energyTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.energyBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.valenceTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.valenceBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.acousticnessTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.acousticnessBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.speechinessTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.speechinessBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.instrumentalnessTitle).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.instrumentalnessBar).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.findAdvResults).setVisibility(View.VISIBLE);

                activity.findViewById(R.id.tracksListView).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.searchBar).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.toggleButton).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.basicSearch).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.findResults).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.findGenreResults).setVisibility(View.INVISIBLE);
                activity.findViewById(R.id.findAdvGenreResults).setVisibility(View.INVISIBLE);

                Button findAdvResults = (Button) activity.findViewById(R.id.findAdvResults);
                id = SearchFragment.idList.get((Integer)v.getTag());
                tag = (Integer)v.getTag();
                findAdvResults.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        SeekBar popularityBar = (SeekBar) activity.findViewById(R.id.popularityBar);
                        SeekBar danceabilityBar = (SeekBar) activity.findViewById(R.id.danceabilityBar);
                        SeekBar tempoBar = (SeekBar) activity.findViewById(R.id.tempoBar);
                        SeekBar livenessBar = (SeekBar) activity.findViewById(R.id.livenessBar);
                        SeekBar energyBar = (SeekBar) activity.findViewById(R.id.energyBar);
                        SeekBar valenceBar = (SeekBar) activity.findViewById(R.id.valenceBar);
                        SeekBar acousticnessBar = (SeekBar) activity.findViewById(R.id.acousticnessBar);
                        SeekBar speechinessBar = (SeekBar) activity.findViewById(R.id.speechinessBar);
                        SeekBar instrumentalnessBar = (SeekBar) activity.findViewById(R.id.instrumentalnessBar);

                        int popularityBarInt = popularityBar.getProgress();
                        int danceabilityBarInt = danceabilityBar.getProgress();
                        int tempoBarInt = tempoBar.getProgress();
                        int livenessBarInt = livenessBar.getProgress();
                        int energyBarInt = energyBar.getProgress();
                        int valenceBarInt = valenceBar.getProgress();
                        int acousticnessBarInt = acousticnessBar.getProgress();
                        int speechinessBarInt = speechinessBar.getProgress();
                        int instrumentalnessBarInt = instrumentalnessBar.getProgress();

                        float danceabilityBarFloat = danceabilityBarInt/100;
                        float livenessBarFloat = livenessBarInt/100;
                        float energyBarFloat = energyBarInt/100;
                        float valenceBarFloat = valenceBarInt/100;
                        float speechinessBarFloat = speechinessBarInt/100;
                        float instrumentalnessBarFloat = instrumentalnessBarInt/100;
                        float acousticnessBarFloat = acousticnessBarInt/100;

                        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

                        Response.Listener<String> search_listener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d("Advanced", response.substring(0,100));
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
                                                            ArrayAdapter adapter = new ArrayAdapter(context.getApplicationContext(), android.R.layout.simple_list_item_1, displayTracks);
                                                            ListView listView = (ListView) activity.findViewById(R.id.resultsList);
                                                            listView.setAdapter(adapter);

                                                            // Add onItemClickListener
                                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    Log.d("ItemClick", Integer.toString(position));
                                                                    Log.d("ItemClick", Integer.toString(((int)id)));
                                                                    invTrack trackExtra = Authenticated.trackList.get(position);
                                                                    Intent intent = new Intent(context.getApplicationContext(), TrackInfo.class);
                                                                    intent.putExtra("trackPosition", position);
                                                                    context.startActivity(intent);
                                                                }
                                                            });

                                                            activity.findViewById(R.id.resultsList).setVisibility(View.VISIBLE);
                                                            activity.findViewById(R.id.tracksListView).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.searchBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.toggleButton).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.basicSearch).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.findResults).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.findAdvResults).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.findGenreResults).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.findAdvGenreResults).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.genreIncSearch).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.genreButton1).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.genreButton2).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.genreButton3).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.genreButton4).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.genreButton5).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.popularityTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.popularityBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.danceabilityTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.danceabilityBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.tempoTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.tempoBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.livenessTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.livenessBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.energyTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.energyBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.valenceTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.valenceBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.acousticnessTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.acousticnessBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.speechinessTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.speechinessBar).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.instrumentalnessTitle).setVisibility(View.INVISIBLE);
                                                            activity.findViewById(R.id.instrumentalnessBar).setVisibility(View.INVISIBLE);


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };

                                                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                                                StringRequest expandedRequest = RemoteAPI.getTracksAudioFeatures(expanded_listener, sharedPreferences.getString("token", RemoteAPI.TOKEN), trackIDs);
                                                queue.add(expandedRequest);



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                                    StringRequest trackRequest = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", RemoteAPI.TOKEN), trackIDs, Authenticated.MARKET);
                                    StringRequest tracks_response = RemoteAPI.getTracks(track_listener, sharedPreferences.getString("token", Authenticated.AUTH_TOKEN), trackIDs, Authenticated.MARKET);
                                    queue.add(trackRequest);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        String artist = "";
                        String track = "";
                        if (tag < 5) {
                            track = id;
                        } else {
                            artist = id;
                        }

                        StringRequest searchRequest = RemoteAPI.getRecommendations(search_listener, sharedPreferences.getString("token", RemoteAPI.TOKEN), "", artist, track,
                                Authenticated.MARKET, 20, acousticnessBarFloat, danceabilityBarFloat, -1, energyBarFloat, instrumentalnessBarFloat,
                                livenessBarFloat, -1, popularityBarInt, speechinessBarFloat, tempoBarInt, valenceBarFloat);
                        queue.add(searchRequest);
                    }
                });
            }
        });

        return view;
    }
}
