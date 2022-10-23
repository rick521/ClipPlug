package com.example.clipplug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mBtnLoadPlugin)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadPlugin();
                    }
                });


        findViewById(R.id.mBtnStartProxy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProxy();
            }

        });
    }

    private void loadPlugin() {
        HookManager.getInstance().loadPlugin(this);

    }

    /**
     * 跳转插件
     */
    private void startProxy() {
        //
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("ClassName", HookManager.getInstance().packageInfo.activities[0].name);
        //intent.putExtra("ClassName", "MainActivity");

        startActivity(intent);
    }
}
