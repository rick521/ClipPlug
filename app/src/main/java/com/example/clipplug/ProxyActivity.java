package com.example.clipplug;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.baseinterface.ProxyActivityInterface;

import java.lang.reflect.Constructor;

/**
 * 类说明 一个占位的activity
 */
public class ProxyActivity extends AppCompatActivity {

    private ProxyActivityInterface pluginObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在这里拿到了真实跳转的activity 拿出来 再去启动真实的activity

        String className = getIntent().getStringExtra("ClassName");

        //通过反射在去启动一个真实的activity 拿到Class对象
        try {
            Class<?> plugClass = getClassLoader().loadClass(className);
            Constructor<?> pluginConstructor = plugClass.getConstructor(new Class[]{});
            //因为插件的activity实现了我们的标准
            pluginObj = (ProxyActivityInterface) pluginConstructor.newInstance(new Object[]{});
            pluginObj.attach(this);//注入上下文
            pluginObj.onCreate(new Bundle());
        } catch (Exception e) {
            if (e.getClass().getSimpleName().endsWith("ClassCastException")) {
                finish();
                Toast.makeText(this, "不是插件类", Toast.LENGTH_LONG).show();
                return;
            }
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        String className1 = intent.getStringExtra("ClassName");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("ClassName", className1);
        super.startActivity(intent1);
    }

    @Override
    public ClassLoader getClassLoader() {
        return HookManager.getInstance().getClassLoader();
    }

    @Override
    public Resources getResources() {
        return HookManager.getInstance().getResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pluginObj.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pluginObj.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pluginObj.onPause();
    }
}
