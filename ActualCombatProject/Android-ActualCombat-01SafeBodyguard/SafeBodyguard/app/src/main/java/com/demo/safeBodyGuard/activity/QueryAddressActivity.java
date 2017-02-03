package com.demo.safeBodyGuard.activity;

import android.app.Service;
import android.os.Vibrator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.AddressDAO;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.utils.LogUtil;
import com.demo.safeBodyGuard.utils.StringUtil;

public class QueryAddressActivity extends BaseActivity implements View.OnClickListener, TextWatcher
{
    private EditText et_query;
    private Button   btn_query;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_query_address);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initUI()
    {
        et_query = (EditText) findViewById(R.id.query_address_activity_et_query);
        btn_query = (Button) findViewById(R.id.query_address_activity_btn_query);

        et_query.addTextChangedListener(this);
        btn_query.setOnClickListener(this);
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.query_address_activity_btn_query:
                onQueryBtnClick();
                break;
        }
    }

    private void onQueryBtnClick()
    {
        String inputTxt = et_query.getText().toString();

        if (StringUtil.isNullOrEmpty(inputTxt))
        {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_query.startAnimation(animation);
            Vibrator vibrate = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            vibrate.vibrate(new long[]{0, 1000, 500, 2000}, -1);
            return;
        }

        String result = AddressDAO.getAddress(inputTxt);
        LogUtil.log(result);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
