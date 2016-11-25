package com.project.lottowebview.a04login_network;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.project.lottowebview.a04login_network.utils.LoginHttpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText ed_account;
    private EditText ed_password;
    private Button btn_login;
    private CheckBox cb_rem;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_account = (EditText) findViewById(R.id.ed_accountTxt);
        ed_password = (EditText) findViewById(R.id.ed_passwordTxt);
        btn_login = (Button) findViewById(R.id.btn_login);
        cb_rem = (CheckBox) findViewById(R.id.cb_remember);

        btn_login.setOnClickListener(this);
    }

    private  void login()
    {
        LoginHttpUtil.loginForNetworkByGet(handler,this.ed_account.getText().toString(),this.ed_password.getText().toString(),"");
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_login:
                login();
                break;
        }
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    handleGetLoginResult(msg.obj.toString());
                    break;

                case 2:
                    handlePostLoginResult(msg.obj.toString());
                    break;
            }
        }
    };

    // 處理Get登入結果
    private void  handleGetLoginResult(String result)
    {
        if(result.contains("succ"))
        {
            Toast.makeText(this,"Get登入成功",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Post登入失敗",Toast.LENGTH_LONG).show();
        }
    }

    // 處理Post登入結果
    private void handlePostLoginResult(String result)
    {
        if(result.contains("succ"))
        {
            Toast.makeText(this,"Post登入成功",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Post登入失敗",Toast.LENGTH_LONG).show();
        }
    }

}
