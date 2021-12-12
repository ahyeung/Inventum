package com.example.inventum;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ToggleButton;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment implements View.OnClickListener {
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
        return view;
    }

    @Override
    public void onClick(View view) {
        ToggleButton toggle = view.findViewById(R.id.toggleButton);
        switch(view.getId()){
            case R.id.toggleButton:
                String text = (String) toggle.getText();
                if(text.equals("Starred")){
                    Log.d("UserFrag", "Text: " + text);

                }
                if(text.equals("Your Tracks")){
                    Log.d("UserFrag", "Text: " + text);
                }
            default:
                Log.d("message", "Wrong button");
        }
    }
}