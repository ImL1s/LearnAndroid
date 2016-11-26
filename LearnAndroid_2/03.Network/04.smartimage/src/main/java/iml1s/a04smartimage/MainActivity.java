package iml1s.a04smartimage;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jit.lib.*;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartImageView s_img = (SmartImageView) findViewById(R.id.s_imageView);

        s_img.setImageUrl("http://vignette1.wikia.nocookie.net/rezero/images/c/c0/Emilia_Anime_2.png/revision/latest?cb=20160408203829", new SmartImageTask.OnCompleteListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Toast.makeText(MainActivity.this,"Succ",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail() {
                Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_LONG).show();
            }
        });


    }
}
