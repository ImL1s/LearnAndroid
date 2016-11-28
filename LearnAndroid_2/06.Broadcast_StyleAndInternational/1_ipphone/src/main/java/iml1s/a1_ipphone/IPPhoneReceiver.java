package iml1s.a1_ipphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by aa223 on 2016/11/29.
 */

public class IPPhoneReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("debug","===============================OnReceive");

        SharedPreferences preferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String ipNumber = preferences.getString("ipNumber","");

        String currentNumber = getResultData();

        // 判斷是否是國際號碼
        if(currentNumber.startsWith("86"))
        {
            setResultData(ipNumber + currentNumber);
        }

        Log.e("debug","===============================更改電話");
        Toast.makeText(context,"更改電話號碼...",Toast.LENGTH_SHORT).show();
    }
}
