package com.example.inventum;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener {

    JSONObject TOP_TRACKS;
    ArrayList<invTrack> trackList;

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
                }
                break;
            default:
                break;
        }
    }

    public void initialSearch(String s) {
        ArrayList<String> displayNotes = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, displayNotes);
        ListView listView = (ListView) getView().findViewById(R.id.tracksListView);
        listView.setAdapter(adapter);

        // Add onItemClickListener


    }
}