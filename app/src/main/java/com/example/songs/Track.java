package com.example.songs;

public class Track {
    private String trackId;
    private String trackname;
    private int rating;

    public Track(){

    }

    public Track(String trackId, String trackname, int rating) {
        this.trackId = trackId;
        this.trackname = trackname;
        this.rating = rating;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public void setTrackname(String trackname) {
        this.trackname = trackname;
    }

    public void setrating(int rating) {
        this.rating = rating;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getTrackname() {
        return trackname;
    }

    public int getrating() {
        return rating;
    }
}
