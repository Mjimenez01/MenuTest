package com.musicproj.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Song extends AppCompatActivity {
    private long id;
    private String title;
    private String artist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
    }

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public long getID(){return id;}
    public String getsongTitle(){return title;}
    public String getArtist(){return artist;}
}
