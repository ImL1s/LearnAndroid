package iml1s.a06_tearshirt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView imageView;
    private Bitmap bitmap;
    private WindowManager windowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.iv);
        imageView.setOnClickListener(this);
        bitmap = Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
        imageView.setImageBitmap(bitmap);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

//        imageView.setBackgroundColor(0x0000ff);
//        findViewById(R.id.btn).setBackgroundColor(getResources().getColor(R.color.blue));


    }

    static int pixelCount = 100;

    public void onClick(View view)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d("debug","***** start *****");

                for (int i = -pixelCount; i <= pixelCount; i++)
                {
                    for (int j = -pixelCount; j <= pixelCount; j++)
                    {
                        int pixcelX = pixelCount + i;
                        int pixcelY = pixelCount + j;

                        if(pixcelX <= 0 || pixcelY <= 0) continue;

//                        SystemClock.sleep(10);
                        // 該點離原點的距離必須小於等於指定的直徑
                        if(Math.sqrt(i * i + j * j) < pixelCount)
                            bitmap.setPixel(pixcelX,pixcelY,getResources().getColor(R.color.blue));
                        else
                            bitmap.setPixel(pixcelX,pixcelY,getResources().getColor(R.color.red));

                        // 在這裡使用runOnUiThread會使子線程卡住,最好全部算完以後再一次顯示
//                        runOnUiThread(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        });
                    }
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        imageView.setImageBitmap(bitmap);
                    }
                });

                Log.d("debug","***** done *****");
            }

        }).start();
    }

    private int getWindowsHeight()
    {
        return windowManager.getDefaultDisplay().getHeight();
    }

    private int getWindowsWidth()
    {
        return windowManager.getDefaultDisplay().getWidth();
    }
}
