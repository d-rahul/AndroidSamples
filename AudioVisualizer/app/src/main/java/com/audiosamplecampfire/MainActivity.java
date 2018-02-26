package com.audiosamplecampfire;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import views.RelativeViewNorm;

public class MainActivity extends AppCompatActivity {


    private RelativeViewNorm mWaveView;

    public static final String DIRECTORY_NAME_TEMP = "AudioTemp";
    //public static final int REPEAT_INTERVAL = 150;
    public static final int REPEAT_INTERVAL = 120;
    private TextView txtRecord;

    private int checkedItem = 0;

    private MediaRecorder recorder = null;

    File audioDirTemp;
    private boolean isRecording = false;


    private Handler handler;// Handler for updating the visualizer
    // private boolean recording; // are we currently recording?

    public float amplitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaveView = (RelativeViewNorm) findViewById(R.id.wave);

        txtRecord = (TextView) findViewById(R.id.txtRecord);
        txtRecord.setOnClickListener(recordClick);

        audioDirTemp = new File(Environment.getExternalStorageDirectory(),
                DIRECTORY_NAME_TEMP);
        if (audioDirTemp.exists()) {
            deleteFilesInDir(audioDirTemp);
        } else {
            audioDirTemp.mkdirs();
        }

        // create the Handler for visualizer update
        handler = new Handler();
    }

    View.OnClickListener recordClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!isRecording) {
                // isRecording = true;

                txtRecord.setText("Stop Recording");

                recorder = new MediaRecorder();

                 recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(audioDirTemp + "/audio_file"
                        + ".mp3");

                MediaRecorder.OnErrorListener errorListener = null;
                recorder.setOnErrorListener(errorListener);
                MediaRecorder.OnInfoListener infoListener = null;
                recorder.setOnInfoListener(infoListener);

                try {
                    recorder.prepare();
                    recorder.start();
                    isRecording = true; // we are currently recording
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(updateVisualizer);

            } else {

                txtRecord.setText("Start Recording");

                releaseRecorder();
            }

        }
    };

    private void releaseRecorder() {
        if (recorder != null) {
            isRecording = false; // stop recording
            handler.removeCallbacks(updateVisualizer);
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    public static boolean deleteFilesInDir(File path) {

        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {

                if (files[i].isDirectory()) {

                } else {
                    files[i].delete();
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRecorder();
    }

    // updates the visualizer every 50 milliseconds
    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isRecording) // if we are already recording
            {
                // get the current amplitude
                int x = recorder.getMaxAmplitude();
                String cast = x + "";

                //mWaveView.setAmplitudeRatio(Float.parseFloat(cast));
                mWaveView.addAmplitude(Float.parseFloat(cast));
                //amplitude = getAmplitudeInRane100(x);
                //mWaveView.setAmplitudeRatio(amplitude);// refresh the VisualizerView
                //initAnim();


                handler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };


}