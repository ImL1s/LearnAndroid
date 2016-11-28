package iml1s.a1_ipphone;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.set_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("config", MODE_PRIVATE);

        String ipNumber = ((EditText) findViewById(R.id.editText)).getText().toString();
        if (!isNullOrEmpty(ipNumber))
        {
//            sharedPreferences.edit().putString("ipNumber",ipNumber).commit();
            // apply使用後台執行續，不會卡住UI執行續
            sharedPreferences.edit().putString("ipNumber", ipNumber).apply();
            Toast.makeText(MainActivity.this,"儲存成功",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNullOrEmpty(String str)
    {
        if (str == null || str.equals("")) return true;

        return false;
    }
}

