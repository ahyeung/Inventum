package com.example.inventum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView trackArtView;
        public TextView titleTextView;
        public TextView artistTextView;

        public ViewHolder (View trackView) {
            super(trackView);

            trackArtView = (ImageView) itemView.findViewById(R.id.trackArt);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            artistTextView = (TextView) itemView.findViewById(R.id.artist);
        }

    }

    private List <invTrack> mTracks;

    public TracksAdapter(List<invTrack> tracks) {
        mTracks = tracks;
    }

    //inflating layout XML and return holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View trackView = inflater.inflate(R.layout.track, parent, false);

        ViewHolder viewHolder = new ViewHolder(trackView);
        return viewHolder;
    }

    //populate track data via holder
    //TODO: These are currently sample tracks with no spotify connection
    //TODO: need to pull spotify image art (if possible), track title, and artist
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        invTrack track = mTracks.get(position);

        //TODO: ImaggeView
        ImageView trackArtView = holder.trackArtView;

        TextView titleView = holder.titleTextView;
        titleView.setText("SAMPLE TITLE");

        TextView artistView = holder.artistTextView;
        artistView.setText("SAMPLE ARTIST");

    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }



}
