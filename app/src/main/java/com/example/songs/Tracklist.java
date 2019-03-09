package com.example.songs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Tracklist extends ArrayAdapter<Track> {

    private Activity context;
    List<Track> track;

    // this will bind the layout arits file
    public Tracklist(Activity context, List<Track> track) {
        super(context, R.layout.layout_track,track);

        this.context = context;
        this.track = track;
    }

    public View getView(int position, View view, ViewGroup parent){
        //referenc to layout file to bind
        LayoutInflater inflater=context.getLayoutInflater();
        View list=inflater.inflate(R.layout.layout_track,null,true);

        //now to access the textview
        TextView textViewartist=list.findViewById(R.id.textViewArtistName);
        TextView textViewrating=list.findViewById(R.id.textViewRating);

        //bind the data

        Track artist=track.get(position);
        textViewartist.setText(artist.getTrackname());
        textViewrating.setText(String.valueOf(artist.getrating()));

        return list;

    }



}
