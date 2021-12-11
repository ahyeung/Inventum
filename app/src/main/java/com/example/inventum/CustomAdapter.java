package com.example.inventum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private Context context;

    public CustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
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
        Button chooseButton = (Button)view.findViewById(R.id.chooseButton);
        chooseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.advancedSearch:

                        break;
                }
            }
        });

        return view;
    }
}
