package com.example.inventum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SearchFragment extends Fragment implements View.OnClickListener {

    static String MARKET = "";
    public static ArrayList<String> idList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        Button findR = (Button) v.findViewById(R.id.findResults);
        findR.setOnClickListener(this);
        ToggleButton toggle = (ToggleButton) v.findViewById(R.id.toggleButton);
        toggle.setChecked(true);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("TRUE", "SONGS/ARTISTS************");
                    TextView searchTitle = (TextView) getActivity().findViewById(R.id.basicSearch);
                    searchTitle.setText("Songs/Artists");
                    EditText keywords = (EditText) getActivity().findViewById(R.id.searchBar);
                    EditText genres = (EditText) getActivity().findViewById(R.id.genreIncSearch);
                    genres.setVisibility(View.INVISIBLE);
                    keywords.setVisibility(View.VISIBLE);

                } else {
                    Log.d("FALSE", "GENRES************");
                    TextView searchTitle = (TextView) getActivity().findViewById(R.id.basicSearch);
                    searchTitle.setText("Genres");
                    EditText keywords = (EditText) getActivity().findViewById(R.id.searchBar);
                    EditText genres = (EditText) getActivity().findViewById(R.id.genreIncSearch);
                    keywords.setVisibility(View.INVISIBLE);
                    genres.setVisibility(View.VISIBLE);
                }
            }
        });
        AutoCompleteTextView autocomplete = (AutoCompleteTextView)
                getActivity().findViewById(R.id.genreIncSearch);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity().getApplicationContext(),android.R.layout.select_dialog_item, Authenticated.genres);

        //autocomplete.setThreshold(2);
        //autocomplete.setAdapter(adapter);

        return v;
    }



    public void onClick(View view) {

        TextView tV1 = (TextView) getView().findViewById(R.id.genreInc);
        EditText eV1 = (EditText) getView().findViewById(R.id.searchBar);
        EditText eV2 = (EditText) getView().findViewById(R.id.genreIncSearch);

        switch (view.getId()) {
            case R.id.findResults:
                String searchStr = eV1.getText().toString();
                initialSearch(searchStr);
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
                    //MARKET = jsonObject.getString("country");
                    Log.d("Search", response.substring(0, 500));

                    JSONArray artists = jsonObject.getJSONObject("artists").getJSONArray("items");
                    JSONArray tracks = jsonObject.getJSONObject("tracks").getJSONArray("items");

                    ArrayList<String> itemsList = new ArrayList<>();
                    //ArrayList<String> idList = new ArrayList<>();

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
                    ListView listView = (ListView) getView().findViewById(R.id.tracksListView);
                    listView.setAdapter(new CustomAdapter(itemsList, getActivity().getApplicationContext()));

                    getView().findViewById(R.id.tracksListView).setVisibility(View.VISIBLE);

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