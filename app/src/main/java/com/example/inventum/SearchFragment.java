package com.example.inventum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener {

    JSONObject TOP_TRACKS;
    ArrayList<invTrack> trackList;
    static String MARKET = "";
    final JSONObject[] tracks = {null};
    final JSONObject[] tracks_expanded = {null};

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        Button aSearch = (Button) v.findViewById(R.id.advancedSearch);
        aSearch.setOnClickListener(this);
        Button findR = (Button) v.findViewById(R.id.findResults);
        findR.setOnClickListener(this);
        return v;
    }



    public void onClick(View view) {

        TextView tV1 = (TextView) getView().findViewById(R.id.genreInc);
        TextView tV2 = (TextView) getView().findViewById(R.id.genreExc);
        EditText eV1 = (EditText) getView().findViewById(R.id.keywordsInput);
        EditText eV2 = (EditText) getView().findViewById(R.id.genreIncSearch);
        EditText eV3 = (EditText) getView().findViewById(R.id.genreExcSearch);
        Button advSB = (Button) getView().findViewById(R.id.advancedSearch);

        switch (view.getId()) {
            case R.id.advancedSearch:
                tV1.setVisibility(View.VISIBLE);
                tV2.setVisibility(View.VISIBLE);
                eV2.setVisibility(View.VISIBLE);
                eV3.setVisibility(View.VISIBLE);
                advSB.setVisibility(View.INVISIBLE);
                break;
            case R.id.findResults:
                if (advSB.getVisibility() == View.INVISIBLE) {
                    String searchStr = eV1.getText().toString();
                    initialSearch(searchStr);
                    advSB.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    public void initialSearch(String s) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // Populate ListView
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    MARKET = jsonObject.getString("country");

                    JSONArray artists = jsonObject.getJSONArray("artists");
                    JSONArray tracks = jsonObject.getJSONArray("tracks");

                    ArrayList<String> itemsList = new ArrayList<>();
                    ArrayList<String> idList = new ArrayList<>();

                    for (int i = 0; i < tracks.length(); i ++) {
                        JSONObject trackObject = tracks.getJSONObject(i);

                        itemsList.add("Song: " + trackObject.getString("name"));
                        idList.add(trackObject.getString("id"));
                    }

                    for (int i = 0; i < artists.length(); i ++) {
                        JSONObject trackObject = artists.getJSONObject(i);

                        itemsList.add("Artist: " + trackObject.getString("name"));
                        idList.add(trackObject.getString("id"));
                    }

                    // use ListView to display results
                    ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(),
                            android.R.layout.simple_list_item_1, itemsList);
                    ListView listView = (ListView) getView().findViewById(R.id.tracksListView);
                    listView.setAdapter(adapter);

                    // Add onItemClickListener

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        StringRequest stringRequest =
                RemoteAPI.search(listener, Authenticated.AUTH_TOKEN, s, MARKET, "track,artist");
        queue.add(stringRequest);
    }
}