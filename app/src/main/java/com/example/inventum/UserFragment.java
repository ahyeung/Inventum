package com.example.inventum;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {
    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("TRUE", "FRIENDS************");

                } else {
                    Log.d("FALSE", "TRACKS************");
                }
            }
        });
        return view;
    }





}