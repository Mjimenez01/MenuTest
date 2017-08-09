package com.musicproj.menu;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;
public class MainActivity extends AppCompatActivity implements MediaPlayerControl {
    private ArrayList<Song> songList;
    private MusicController controller;
    private ListView songView;
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    public void start() {

        musicSrv.go();
    }

    private void setController(){
    // sets our controller up!!

        controller = new MusicController(this);

        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    //play next
    private void playNext(){
        musicSrv.playNext();
        controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        controller.show(0);
    }

    @Override
    public void pause() {

        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {

        if(musicSrv!=null &amp;&amp; musicBound &amp;&amp; musicSrv.isPng())
        return musicSrv.getDur();

         return 0; // deleted else before the return
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv!=null &amp;&amp; musicBound &amp;&amp; musicSrv.isPng())
            return musicSrv.getPosn();

             return 0;  // deleted else before the return

    }

    @Override
    public void seekTo(int i) {

        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null &amp;&amp; musicBound)
        return musicSrv.isPng();

        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {

        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    public void getSongList() {



        //retrieve song info

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mT= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mT);
    getSongList();
        Collections.sort(songList, new Comparator<Song>(){

            public int compare(Song a, Song b){
              return a.getsongTitle().compareTo(b.getsongTitle());

            }
        });
        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);

        setController();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creates icons
        MenuInflater mM= getMenuInflater();
        mM.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_setting){
             startActivity(new Intent(this,SettingsActivity.class));

        }
        if(item.getItemId() == R.id.action_import_music){
            Toast.makeText(MainActivity.this, "Importing Selected Music Libraries...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
