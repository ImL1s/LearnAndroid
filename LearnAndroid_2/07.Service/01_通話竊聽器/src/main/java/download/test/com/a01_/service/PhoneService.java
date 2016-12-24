package download.test.com.a01_.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iml1s-macpro on 2016/12/7.
 */

public class PhoneService extends Service
{

    private MediaRecorder recorder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        Log.d("debug","onCreate");

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(new YSPhoneListener(),PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("debug","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public class YSPhoneListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            switch (state)
            {
                // 閒置狀態
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("debug","準備電話撥號器!");

                    if(recorder != null)
                    {
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
                    }

                    break;

                // 接起狀態
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("debug","開始錄音!");

                    try
                    {
                        recorder.start();
                    }
                    catch (Exception e)
                    {
                        Log.d("debug",e.toString());
                    }
                    break;

                // 響鈴狀態
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("debug","準備錄音!");

                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

//                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

//                    recorder.setOutputFile(Environment.getExternalStorageDirectory() + "/recorder.3gp");
//                    recorder.setOutputFile(Environment.getDownloadCacheDirectory() + "/recorder.3gp");
                    recorder.setOutputFile("mnt/sdcard/recorder.3gp");

                    try
                    {
                        recorder.prepare();
                    }
                    catch (Exception e)
                    {
                        Log.d("debug",e.toString());
                        e.printStackTrace();
                    }

                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    }


}


