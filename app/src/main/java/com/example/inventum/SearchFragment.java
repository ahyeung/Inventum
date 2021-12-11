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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener {

    TextView tV1;
    TextView tV2;
    EditText eV1;
    EditText eV2;
    EditText eV3;
    Button advSB;
    String searchStr;
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

        tV1 = (TextView) getView().findViewById(R.id.genreInc);
        tV2 = (TextView) getView().findViewById(R.id.genreExc);
        eV1 = (EditText) getView().findViewById(R.id.keywordsInput);
        eV2 = (EditText) getView().findViewById(R.id.genreIncSearch);
        eV3 = (EditText) getView().findViewById(R.id.genreExcSearch);
        advSB = (Button) getView().findViewById(R.id.advancedSearch);

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
                    searchStr = eV1.getText().toString();
                    initialSearch(searchStr);
                }
                break;
            default:
                break;
        }
    }

    public void initialSearch(String s) {

    }
}