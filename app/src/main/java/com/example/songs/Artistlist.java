package com.example.songs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Artistlist extends ArrayAdapter<Atrist> {
    private Activity context;
    List<Atrist> artists;

    // this will bind the layout arits file
    public Artistlist(Activity context, List<Atrist> artists) {
        super(context, R.layout.artist_list,artists);
        this.context = context;
        this.artists = artists;
    }

    public View getView(int position, View view, ViewGroup parent){
        //referenc to layout file to bind
        LayoutInflater inflater=context.getLayoutInflater();
        View list=inflater.inflate(R.layout.artist_list,null,true);

        //now to access the textview
        TextView textViewartist=list.findViewById(R.id.textViewArtistName);
        TextView textViewartistGenre=list.findViewById(R.id.textViewArtistGenre);

        //bind the data

        Atrist artist=artists.get(position);
        textViewartist.setText(artist.getArtistname());
        textViewartistGenre.setText(artist.getArtistGenre());

        return list;

    }


}
