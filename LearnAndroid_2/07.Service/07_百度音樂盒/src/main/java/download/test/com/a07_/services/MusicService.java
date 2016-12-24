package download.test.com.a07_.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by iml1s-macpro on 2016/12/12.
 */

public class MusicService extends Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.d("debug","MusicService onBind");
        return new MusicBinder();
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        Log.d("debug","MusicService onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate()
    {
        Log.d("debug","MusicService onCreate");
    }

    @Override
    public void onDestroy()
    {
        Log.d("debug","MusicService onDestroy");
    }

    public void playMusic()
    {
        Log.d("debug","MusicService playMusic");
    }

    public void stopMusic()
    {
        Log.d("debug","MusicService stopMusic");
    }

    public void pauseMusic()
    {
        Log.d("debug","MusicService pauseMusic");
    }

    public class MusicBinder extends Binder implements IMusicBinder
    {
        @Override
        public void callPlayMusic()
        {
            playMusic();
        }

        @Override
        public void callStopMusic()
        {
            stopMusic();
        }

        @Override
        public void callPauseMusic()
        {
            pauseMusic();
        }
    }
}



