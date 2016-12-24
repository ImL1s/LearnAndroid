package download.test.com.a01_;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import download.test.com.a01_.service.PhoneService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.startBtn).setOnClickListener(this);
        findViewById(R.id.startRecord_btn).setOnClickListener(this);
        findViewById(R.id.stopRecord_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.startBtn:

                 if(checkAndRequestPermissions())
                 {
                     Intent intent = new Intent(this, PhoneService.class);
                     startService(intent);
                     Toast.makeText(this, "啟動竊聽服務", Toast.LENGTH_SHORT).show();
                 }
                else
                 {
                     Toast.makeText(this, "請允許權限", Toast.LENGTH_SHORT).show();
                 }
                break;

            case R.id.startRecord_btn:

//                try
//                {
//                    validateMicAvailability();
//                }
//                catch (Exception e)
//                {
//                    Log.d("debug",e.toString());
//                }

                Toast.makeText(this,"開始錄音",Toast.LENGTH_LONG).show();
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                //                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                //                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                //                    recorder.setOutputFile(Environment.getExternalStorageDirectory() + "/recorder.3gp");
                //                    recorder.setOutputFile(Environment.getDownloadCacheDirectory() + "/recorder.3gp");
                recorder.setOutputFile("mnt/sdcard/recorder.3gp");

                try
                {
                    recorder.prepare();
                    recorder.start();

                }
                catch (Exception e)
                {
                    Log.d("debug",e.toString());
                }

                break;

            case R.id.stopRecord_btn:

                recorder.stop();
                recorder.reset();
                recorder.release();
                Toast.makeText(this,"錄音完成",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private boolean checkAndRequestPermissions()
    {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void validateMicAvailability() throws Exception {
        AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);
        try{
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED ){
                Log.d("debug","Mic didn't successfully initialized");
                throw new Exception("Mic didn't successfully initialized");
            }

            recorder.startRecording();
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING){
                recorder.stop();
                Log.d("debug","Mic is in use and can't be accessed");
                throw new Exception("Mic is in use and can't be accessed");
            }
            recorder.stop();
        } finally{
            recorder.release();
            recorder = null;
        }
    }

}
