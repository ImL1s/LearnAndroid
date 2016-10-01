package com.learn.aa223.a02sendbundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,RecvActivity.class);
//                Bundle b = new Bundle();
//                Bundle bb = new Bundle();
//                b.putString("name","sam");
//                b.putInt("age",4);
//                bb.putString("undefined","unun");
//                i.putExtras(b);
//                i.putExtras(new Bundle(b));
//                i.putExtras(bb);
//                i.putExtra("bundle1",b);
//                i.putExtra("bundle2",bb);

//                i.putExtra("user1",new User("Sam",24));
                i.putExtra("userP",new UserParcelable("SamP",23));
                startActivity(i);
            }
        });
    }
}
