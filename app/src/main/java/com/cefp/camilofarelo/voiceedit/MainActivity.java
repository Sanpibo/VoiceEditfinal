package com.cefp.camilofarelo.voiceedit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    private static final String LOG_TAG = "AudioRecordTest";
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    String mFileName;
    Button record_btn;
    Button stop_btn;
    Button play_btn;
    Button sgte_btn;
    ImageView imagen;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record_btn = (Button) findViewById(R.id.record_btn);
        stop_btn = (Button) findViewById(R.id.stop_btn);
        imagen = (ImageView) findViewById(R.id.logo);
        play_btn = (Button) findViewById(R.id.play_btn);
        sgte_btn=(Button) findViewById(R.id.sgte_btn);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        stop_btn.setEnabled(false);
        play_btn.setEnabled(false);
        sgte_btn.setEnabled(false);


        /*mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFileName);*/

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile(mFileName);
                /*for (int i = 1; i <= 100; i++) {
                    mFileName = mFileName + " " + String.valueOf(i);
                    mRecorder.setOutputFile(mFileName);
                }*/
                imagen.setImageResource(R.drawable.logo2);
                try {
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                record_btn.setEnabled(false);
                stop_btn.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording Audio", Toast.LENGTH_SHORT).show();
            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Stop Recording", Toast.LENGTH_SHORT).show();
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;

                stop_btn.setEnabled(false);
                play_btn.setEnabled(true);
                sgte_btn.setEnabled(true);
            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record_btn.setEnabled(true);
                Uri data= Uri.parse(mFileName);
                mPlayer= MediaPlayer.create(getApplicationContext(),data);
                mPlayer.start();
                Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
                mRecorder=null;
            }
        });


        sgte_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(MainActivity.this,Efectos.class);
                startActivity(intent);
            }
        });

    }
}
