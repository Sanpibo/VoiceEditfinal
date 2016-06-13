package com.cefp.camilofarelo.voiceedit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Efectos extends Activity implements OnClickListener {

    //Integer[] freqset = {8000,11025, 16000, 22050,32000, 44100,48000};
    private ArrayAdapter<Integer> adapter;


    Button startRec, stopRec, playBack;
    ImageButton anonimus,darthvader,eructo,deedee,no_se,alvin;
    LinearLayout ll;
    int sampleFreq;

    Boolean recording;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_efectos);
        startRec = (Button)findViewById(R.id.startrec);
        stopRec = (Button)findViewById(R.id.stoprec);
        playBack = (Button)findViewById(R.id.playback);
        anonimus=(ImageButton) findViewById(R.id.anonimus_btn);
        darthvader=(ImageButton)  findViewById(R.id.darth_vader_btn);
        eructo=(ImageButton) findViewById(R.id.eructo_btn);
        deedee=(ImageButton)findViewById(R.id.deedee_btn);
        no_se=(ImageButton)findViewById(R.id.no_se);
        alvin=(ImageButton)findViewById(R.id.alvin_btn);
        ll=(LinearLayout) findViewById(R.id.linear);

        stopRec.setEnabled(false);
        playBack.setEnabled(false);

        startRec.setOnClickListener(startRecOnClickListener);
        stopRec.setOnClickListener(stopRecOnClickListener);
        playBack.setOnClickListener(playBackOnClickListener);
        anonimus.setOnClickListener(this);
        darthvader.setOnClickListener(this);
        eructo.setOnClickListener(this);
        deedee.setOnClickListener(this);
        no_se.setOnClickListener(this);
        alvin.setOnClickListener(this);

    }

    OnClickListener startRecOnClickListener
            = new OnClickListener(){

        @Override
        public void onClick(View arg0) {

            Thread recordThread = new Thread(new Runnable(){

                @Override
                public void run() {
                    recording = true;
                    startRecord();
                }

            });


            recordThread.start();
            startRec.setEnabled(false);
            stopRec.setEnabled(true);
            playBack.setEnabled(true);
            ll.setBackgroundColor(Color.RED);
            //Toast.makeText(Efectos.this, "Recording", Toast.LENGTH_SHORT).show();

        }};

    OnClickListener stopRecOnClickListener
            = new OnClickListener(){

        @Override
        public void onClick(View arg0) {
            recording = false;
            ll.setBackgroundColor(Color.WHITE);
        }
    };

    OnClickListener playBackOnClickListener
            = new OnClickListener(){

        @Override
        public void onClick(View v) {
            playRecord();
            startRec.setEnabled(true);
            //Toast.makeText(Efectos.this, "Playing audio!", Toast.LENGTH_SHORT).show();
        }
    };

    private void startRecord(){
        Date createdtime=new Date();

        File file = new File(Environment.getExternalStorageDirectory(),"test.pcm");

        sampleFreq = 22050;

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];

            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();

            while(recording){
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);
                for(int i = 0; i < numberOfShort; i++){
                    dataOutputStream.writeShort(audioData[i]);
                }
            }

            audioRecord.stop();
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void playRecord(){

        File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");
        ll.setBackgroundColor(Color.GRAY);


        int shortSizeInBytes = Short.SIZE/Byte.SIZE;

        int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
        short[] audioData = new short[bufferSizeInBytes];

        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while(dataInputStream.available() > 0){
                audioData[i] = dataInputStream.readShort();
                i++;
            }

            dataInputStream.close();

            AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();
            audioTrack.write(audioData, 0, bufferSizeInBytes);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.anonimus_btn:
                sampleFreq=16000;
                //Toast.makeText(Efectos.this, "Anonymous!", Toast.LENGTH_SHORT).show();
                anonimus.setImageResource(R.drawable.anonimusbw);
                alvin.setImageResource(R.drawable.alvin);
                no_se.setImageResource(R.drawable.porky);
                darthvader.setImageResource(R.drawable.darth_vader);
                eructo.setImageResource(R.drawable.barney_burp);
                deedee.setImageResource(R.drawable.deedee);
                break;
            case R.id.darth_vader_btn:
                sampleFreq=11025;
                //Toast.makeText(Efectos.this, "Darth Vader!", Toast.LENGTH_SHORT).show();
                anonimus.setImageResource(R.drawable.anonimus);
                alvin.setImageResource(R.drawable.alvin);
                no_se.setImageResource(R.drawable.porky);
                darthvader.setImageResource(R.drawable.darth_vaderbw);
                eructo.setImageResource(R.drawable.barney_burp);
                deedee.setImageResource(R.drawable.deedee);
                break;
            case R.id.eructo_btn:
                sampleFreq= 8000;
                anonimus.setImageResource(R.drawable.anonimus);
                alvin.setImageResource(R.drawable.alvin);
                no_se.setImageResource(R.drawable.porky);
                darthvader.setImageResource(R.drawable.darth_vader);
                eructo.setImageResource(R.drawable.barney_burpbw);
                deedee.setImageResource(R.drawable.deedee);
                //Toast.makeText(Efectos.this, "Eructo!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deedee_btn:
                sampleFreq=48000;
                //Toast.makeText(Efectos.this, "Dee Dee!", Toast.LENGTH_SHORT).show();
                anonimus.setImageResource(R.drawable.anonimus);
                alvin.setImageResource(R.drawable.alvin);
                no_se.setImageResource(R.drawable.porky);
                darthvader.setImageResource(R.drawable.darth_vader);
                eructo.setImageResource(R.drawable.barney_burp);
                deedee.setImageResource(R.drawable.deedeebw);
                break;
            case R.id.no_se:
                sampleFreq=32000;
                //Toast.makeText(Efectos.this, "Porky!", Toast.LENGTH_SHORT).show();
                anonimus.setImageResource(R.drawable.anonimus);
                alvin.setImageResource(R.drawable.alvin);
                no_se.setImageResource(R.drawable.porkybw);
                darthvader.setImageResource(R.drawable.darth_vader);
                eructo.setImageResource(R.drawable.barney_burp);
                deedee.setImageResource(R.drawable.deedee);
                break;
            case R.id.alvin_btn:
                sampleFreq=44100;
                alvin.setImageResource(R.drawable.alvin_bw);
                no_se.setImageResource(R.drawable.porky);
                darthvader.setImageResource(R.drawable.darth_vader);
                anonimus.setImageResource(R.drawable.anonimus);
                eructo.setImageResource(R.drawable.barney_burp);
                deedee.setImageResource(R.drawable.deedee);
                //Toast.makeText(Efectos.this, "Alvin!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
