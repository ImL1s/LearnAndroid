package download.test.com.a07_;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import download.test.com.a07_.services.IMusicBinder;
import download.test.com.a07_.services.MusicService;

public class MainActivity extends AppCompatActivity
{
    ServiceConnection musicServerConnection;
    IMusicBinder binder;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MusicService.class);
        // 1. 如果使用混合方式開啟服務,必須先startService() 開啟服務
        // 2. 如果沒有stopService(),那麼重新bind()一次並不會觸發Service的onBind()方法
        // 3. unBindService()時如果沒有startService()開啟該服務,那麼就會調用Service的onDestroy()方法
        startService(intent);
        musicServerConnection = new MusicConnection();
        bindService(intent,musicServerConnection,BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy()
    {
        unbindService(musicServerConnection);
        super.onDestroy();
    }

    public void playMusic(View view)
    {
        int i = 1;
        binder.callPlayMusic();
    }

    public void stopMusic(View view)
    {
        binder.callStopMusic();
    }

    public void pauseMusic(View view)
    {
        binder.callPauseMusic();
    }

    public void bindService(View view)
    {
        bindService(intent,musicServerConnection,BIND_AUTO_CREATE);
    }

    public void unBindService(View view)
    {
        unbindService(musicServerConnection);
    }

    public void test(View view)
    {
        Log.d("debug","test");
    }
    // Services狀態監聽者
    public class MusicConnection implements ServiceConnection
    {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            Log.d("debug","onServiceConnected");
            binder = (IMusicBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            Log.d("debug","onServiceDisconnected");
        }
    }
}
