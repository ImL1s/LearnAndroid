package iml1s.a01_scaleimage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    int windowWidth;
    int windowHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_btn).setOnClickListener(this);

        if(Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowWidth = manager.getDefaultDisplay().getWidth();
        windowHeight = manager.getDefaultDisplay().getHeight();
        Log.d("debug","windows width:" + windowWidth + " height:"+windowHeight);

    }

    @Override
    public void onClick(View view)
    {
        String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        Log.d("debug",path);
        BitmapFactory.Options options = new BitmapFactory.Options();

        // 返回一個null 沒有bitmap   不去真正解析位圖 但是能返回圖片的一些信息(寬和高)
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path + "dog2.bmp",options);
//        Log.d("debug",bitmap.toString());
//        ((ImageView)findViewById(R.id.image_view)).setImageBitmap(bitmap);

        // 原圖不能更改,會報錯
//        bitmap.setPixel(100,100, Color.YELLOW);

        int orWidth = options.outWidth;
        int orHeight = options.outHeight;

        int widthScale = orWidth / windowWidth;
        int heightScale = orHeight / windowHeight;
        int finalScale = widthScale > heightScale? widthScale : heightScale;

        // 圖片的縮放比,若為2就等於1/2 ,3就等於1/3
        options.inSampleSize = finalScale;
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(path + "dog2.bmp", options);

        ((ImageView)findViewById(R.id.image_view)).setImageBitmap(bitmap);

    }
}
