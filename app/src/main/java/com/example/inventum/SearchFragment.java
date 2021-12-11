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

import java.security.acl.Group;

public class SearchFragment extends Fragment implements View.OnClickListener {

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
//        Fragment fragment = new AdvSearchFragment();
//        FragmentManager manager = getParentFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.search, new AdvSearchFragment()).commit();
        switch (view.getId()) {
            case R.id.advancedSearch:
                TextView tV1 = (TextView) getView().findViewById(R.id.genreInc);
                TextView tV2 = (TextView) getView().findViewById(R.id.genreExc);
                EditText eV1 = (EditText) getView().findViewById(R.id.genreIncSearch);
                EditText eV2 = (EditText) getView().findViewById(R.id.genreExcSearch);
                Button b = (Button) getView().findViewById(R.id.advancedSearch);
                tV1.setVisibility(View.VISIBLE);
                tV2.setVisibility(View.VISIBLE);
                eV1.setVisibility(View.VISIBLE);
                eV2.setVisibility(View.VISIBLE);
                b.setVisibility(View.INVISIBLE);
                break;
            case R.id.findResults:
                // FIXME
                break;
            default:
                break;
        }
    }

    public
}