package download.test.com.a06_insertcontacts;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import download.test.com.a06_insertcontacts.model.Contact;
import download.test.com.a06_insertcontacts.utils.ContactsUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button insertBtn;
    private EditText etName;
    private EditText etPhone;
    private EditText etEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        insertBtn.setOnClickListener(this);

    }

    private void findView()
    {
        insertBtn = (Button) findViewById(R.id.btn_insert);
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);
    }

    @Override
    public void onClick(View v)
    {
        Contact contact = new Contact();
        contact.setName(etName.getText().toString());
        contact.setPhone(etPhone.getText().toString());
        contact.setEmail(etEmail.getText().toString());

        ContactsUtil.insertContact(contact,this);
    }
}
