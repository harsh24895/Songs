package com.example.songs;

public class Atrist {
    private  String artistId;
    private String artistname;
    private String artistGenre;

    public Atrist(){

    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public void setArtistGenre(String artistGenre) {
        this.artistGenre = artistGenre;
    }

    //have an constructor
    public Atrist(String artistId,String artistname, String artistGenre) {
        this.artistId = artistId;
        this.artistname=artistname;
        this.artistGenre=artistGenre;
    }

    /**
     * implement the getter
     * @return
     */
    public String getArtistId() {
        return artistId;
    }

    public String getArtistname() {
        return artistname;
    }

    public String getArtistGenre() {
        return artistGenre;
    }
}
