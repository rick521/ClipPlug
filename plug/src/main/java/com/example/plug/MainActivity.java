package com.example.plug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toast.makeText(that,"Independent...context",Toast.LENGTH_LONG).show();
        findViewById(R.id.mBtnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(that, SecondActivity.class);
//                startActivity(intent);
            }
        });



    }
}
