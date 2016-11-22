package com.wsh.flashplayertest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.webkit.WebView;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "flashtest";

    private final String mPath = "/mnt/sdcard/test.swf";
    private WebView mWebview;
    private Button playBtn;
    private Button stopBtn;
    private Button forExecp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebview = (WebView) findViewById(R.id.webview);
        ((AppApplication)getApplication()).setContext(this);


        FlashPlayer.getInstance(this).setWebView(mWebview)
                .setFlashPlayerCallBack(new FlashPlayer.FlashPlayerCallBack() {
                    @Override
                    public void onCallBack(FlashPlayer.CallBackState state, Object... args) {
                        if (state == FlashPlayer.CallBackState.GET_DATA_FROM_JS) {
                            String data = (String) args[0];
                            Log.v(FlashPlayer.TAG, data);
                        }
                    }
                });
        playBtn = (Button) findViewById(R.id.play);
        stopBtn = (Button) findViewById(R.id.stop);
        File file = new File(mPath);
        if (file.exists()) {
            FlashPlayer.getInstance(this).playSwf(mPath);

        } else {
            Toast.makeText(this, mPath + " is not exist", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        Log.v(TAG, "isTbsCoreInited(): " + QbSdk.isTbsCoreInited() + '\n' + "getTBSInstalling()" + QbSdk.getTBSInstalling());
        FlashPlayer.getInstance(this).onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FlashPlayer.getInstance(this).onPause();
    }

    public void onClickListener(View v) {

        int id  = v.getId();
        switch (id) {
            case R.id.play:
                File file = new File(mPath);
                if (file.exists()) {
                    FlashPlayer.getInstance(this).playSwf(mPath);

                } else {
                    Toast.makeText(this, mPath + " is not exist", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.stop:
                FlashPlayer.getInstance(this).onPause();
                break;
            case R.id.restart:
                ((AppApplication)getApplication()).restartApp();
//                forExecp.getText();
//                restartApplication();
//                mWebview.loadUrl("http://baidu.com");
                break;
            default:
                break;
        }
    }

    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

//    // 创建服务用于捕获崩溃异常
//    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
//        public void uncaughtException(Thread thread, Throwable ex) {
//            restartApp();//发生崩溃异常时,重启应用
//        }
//    };
//    public void restartApp(){
//        Intent intent = new Intent(mAppContext,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mAppContext.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
//    }
}
