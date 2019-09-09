package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final Context mainContext = this;
    private static final int m = 3;
    private ImageButton buttonplay, buttonprev, buttonnext;
    private SeekBar seekBar;
    private TextView tV_name, tV_time;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private int[] tracks = new int[m];
    private String[] tracksName = new String[m];
    private int currentTrack = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeXML();
        mediaPlayer = MediaPlayer.create(mainContext, tracks[currentTrack]);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });

        buttonplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    buttonplay.setImageResource(android.R.drawable.ic_media_play);
                }else{
                    try {
                        tV_name.setText(tracksName[currentTrack]);
                        mediaPlayer.start();

                        startProgressBarAction();
                        buttonplay.setImageResource(android.R.drawable.ic_media_pause);
                    }catch (IllegalStateException e){
                        mediaPlayer.pause();
                        System.err.println(e);
                    }
                }
            }
        });
        buttonprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTrack == 0)
                    currentTrack = m-1;
                else
                    currentTrack-=1;
                nextTrack(currentTrack);
            }
        });
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTrack == m-1)
                    currentTrack = 0;
                else
                    currentTrack+=1;
                nextTrack(currentTrack);
            }
        });
    }

    private void nextTrack(int trackID) {
        //mediaPlayer.stop();
        tV_time.setText("00:00");
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(mainContext, tracks[trackID]);
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        tV_name.setText(tracksName[trackID]);
        seekBar.setProgress(0);
        buttonplay.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void startProgressBarAction() {
        int milliseconds = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(milliseconds);
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        String min, sec;
        if (minutes<10)
            min = "0"+minutes;
        else
            min = String.valueOf(minutes);
        if (seconds<10)
            sec = "0"+seconds;
        else
            sec = String.valueOf(seconds);
        tV_time.setText(min+":"+sec);
        //System.out.println(minutes + ":"+seconds);
        if (mediaPlayer.isPlaying()){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    startProgressBarAction();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    private void seekChange(View v) {
        SeekBar seekBar1 = (SeekBar) v;
        mediaPlayer.seekTo(seekBar1.getProgress());
    }

    private void initializeXML() {
        buttonplay = findViewById(R.id.imageButton);
        buttonnext = findViewById(R.id.imageButton_next);
        buttonprev = findViewById(R.id.imageButton_prev);
        seekBar = findViewById(R.id.seekBar);
        tV_name = findViewById(R.id.textView);
        tV_time = findViewById(R.id.textView_time);
        tracks[0] = R.raw.feel_good_inc;
        tracksName[0] = "Gorillaz - Feel Good Inc.";
        tracks[1] = R.raw.dare;
        tracksName[1] = "Gorillaz - DARE";
        tracks[2] = R.raw.el_manan;
        tracksName[2] = "Gorillaz - El Mañana";
    }
}
