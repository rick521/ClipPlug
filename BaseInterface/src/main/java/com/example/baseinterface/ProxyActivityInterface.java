package com.example.baseinterface;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Created by Dou on 2019/7/27.
 */
public interface ProxyActivityInterface {

    //生命周期的activity

    public void attach(Activity proxyActivity);

    public void onCreate(Bundle savedInstanceState);

    public void onStart();

    public void onPause();

    public void onDestroy();

}
