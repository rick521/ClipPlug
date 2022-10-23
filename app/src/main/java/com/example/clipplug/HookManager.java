package com.example.clipplug;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by Dou on 2019/7/27.
 */
public class HookManager {
    private static final HookManager ourInstance = new HookManager();
    private Resources resources;
    private DexClassLoader loader;
    public PackageInfo packageInfo;

    public static HookManager getInstance() {
        return ourInstance;
    }

    private HookManager() {
    }


    //用来加载插件
    public void loadPlugin(Activity activity) {
        // 假如这里是从网络获取的插件 我们直接从sd卡获取 然后读取到我们的cache目录
        String pluginName = "plug-debug.apk";
        File filesDir = activity.getDir("plugin", activity.MODE_PRIVATE);
        String filePath = new File(filesDir, pluginName).getAbsolutePath();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        File plug = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + "/" + pluginName);

        FileInputStream is = null;
        FileOutputStream os = null;
        //读取的目录
        try {
            is = new FileInputStream(plug);
            //要输入的目录
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            File f = new File(filePath);
            if (f.exists()) {
                Toast.makeText(activity, "加载完成", Toast.LENGTH_LONG).show();
            }
            loadPathToPlugin(activity);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    private void loadPathToPlugin(Activity activity) {
        File filesDir = activity.getDir("plugin", activity.MODE_PRIVATE);
        String name = "plug-debug.apk";
        String path = new File(filesDir, name).getAbsolutePath();

        //然后我们开始加载我们的apk 使用DexClassLoader
        File dexOutDir = activity.getDir("dex", activity.MODE_PRIVATE);
        loader = new DexClassLoader(path, dexOutDir.getAbsolutePath(), null, activity.getClassLoader());

        //通过PackAgemanager 来获取插件的第一个activity是哪一个
        PackageManager packageManager = activity.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);


        //然后开始加载我们的资源 肯定要使用Resource 但是它是AssetManager创建出来的 就是AssertManager 有一个addAssertPath 这个方法 但是私有的 所有使用反射
        Class<?> assetManagerClass = AssetManager.class;
        try {
            AssetManager assertManagerObj = (AssetManager) assetManagerClass.newInstance();
            Method addAssetPathMethod = assetManagerClass.getMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(assertManagerObj, path);
            //在创建一个Resource
            resources = new Resources(assertManagerObj, activity.getResources().getDisplayMetrics(), activity.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public ClassLoader getClassLoader() {
        return loader;
    }

    public Resources getResource() {
        return resources;
    }
}
