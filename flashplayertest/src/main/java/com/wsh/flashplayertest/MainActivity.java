package com.wsh.flashplayertest;

import android.os.Environment;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.webkit.WebView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebview = (WebView) findViewById(R.id.webview);

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
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
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
                break;
            default:
                break;
        }
    }
}
