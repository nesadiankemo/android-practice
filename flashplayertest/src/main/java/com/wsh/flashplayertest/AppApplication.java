package com.wsh.flashplayertest;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by wsh-03 on 2016/11/14.
 */
public class AppApplication extends Application {
    private static final String TAG = "jting";

    private final String mPath = "app_tbs/share/core_info";
    private File rootFolder;
    private static Context mAppContext;
    private Context mContext;

    private final String[] X5CoreFileList = {
            "app_tbs/core_private/debug.conf",
            "app_tbs/core_private/tbs_extension.conf",
            "app_tbs/core_private/tbs_preloadx5_check_and_report.conf",
            "app_tbs/core_private/tbs_rename_lock.txt",
            "app_tbs/core_private/tbscoreinstall.txt",
            "app_tbs/core_private/tbslock.txt",
            "app_tbs/core_share/1",
            "app_tbs/core_share/TencentLocationSDK.dex",
            "app_tbs/core_share/TencentLocationSDK.jar",
            "app_tbs/core_share/ZIPReader.dex",
            "app_tbs/core_share/ZIPReader.jar",
            "app_tbs/core_share/game_impl_dex.dex",
            "app_tbs/core_share/game_impl_dex.jar",
            "app_tbs/core_share/libAndro7za.so",
            "app_tbs/core_share/libBugly.so",
            "app_tbs/core_share/libSharpPDecoder.so",
            "app_tbs/core_share/libTbsPatch.so",
            "app_tbs/core_share/libWxHevcDecoder.so",
            "app_tbs/core_share/libcmdsh.so",
            "app_tbs/core_share/libcommon_basemodule_jni.so",
            "app_tbs/core_share/libfreakfixer.so",
            "app_tbs/core_share/libgamehook.so",
            "app_tbs/core_share/libgamejavahook.so",
            "app_tbs/core_share/liblinuxtools_jni.so",
            "app_tbs/core_share/liblinuxtoolsfortbssdk_jni.so",
            "app_tbs/core_share/libmttgpu_info.so",
            "app_tbs/core_share/libmttport_shared.so",
            "app_tbs/core_share/libmttwebview.so",
            "app_tbs/core_share/libmttwebview_plat_support.so",
            "app_tbs/core_share/libqb_keystore.so",
            "app_tbs/core_share/libtbs_crash_handler.so",
            "app_tbs/core_share/libtencentpos.so",
            "app_tbs/core_share/libwebhook.so",
            "app_tbs/core_share/libwebp_base.so",
            "app_tbs/core_share/miniqb.conf",
            "app_tbs/core_share/miniqb_dex.dex",
            "app_tbs/core_share/miniqb_dex.jar",
            "app_tbs/core_share/miniqbres.apk",
            "app_tbs/core_share/qziper_dex.dex",
            "app_tbs/core_share/qziper_dex.jar",
            "app_tbs/core_share/res.apk",
            "app_tbs/core_share/tbs.conf",
            "app_tbs/core_share/tbs_game_sandbox_dex.dex",
            "app_tbs/core_share/tbs_game_sandbox_dex.jar",
            "app_tbs/core_share/tbs_jars_fusion_dex.dex",
            "app_tbs/core_share/tbs_jars_fusion_dex.jar",
            "app_tbs/core_share/tbs_sdk_extension_dex.dex",
            "app_tbs/core_share/tbs_sdk_extension_dex.jar",
            "app_tbs/core_share/video_impl_dex.dex",
            "app_tbs/core_share/video_impl_dex.jar",
            "app_tbs/core_share/webview_dex.dex",
            "app_tbs/core_share/webview_dex.jar",
            "app_tbs/share/core_info"
    };
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //TbsDownloader.needDownload(getApplicationContext(), false);
        mAppContext = getApplicationContext();

        Log.v("jting", "AppApplication onCreate");
        rootFolder = new File("/data/data/" + getPackageName());
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.v(TAG, " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.v(TAG, " onCoreInitFinished ");
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d(TAG,"onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d(TAG,"onInstallFinish");
//                if (mContext == null) return;
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setMessage("初始应用配置，应用即将重启...");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        restartApp();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();

            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d(TAG,"onDownloadProgress:"+i);
            }

        });

        if (! checkEnv()) {
            Log.v(TAG, "copy X5 core file...");
            insertX5CoreFile();
        }
        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }


    /*
     * checkEnv()
     * 检测x5内核是否已经下载安装
     */
    private boolean checkEnv() {
        File file = new File(rootFolder, mPath);
//        Log.v(TAG, file.getAbsolutePath());
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 将assets 里的x5 内核复制到引用目录中，避免下载耗时
     */
    private void insertX5CoreFile() {
        for (int i = 0; i < X5CoreFileList.length; i++) {
            Log.v(TAG, "copy file: " + X5CoreFileList[i]);
            copyFromAssets(X5CoreFileList[i]);
        }
    }

    /*
     * 将文件写入到rootFolder 目录下，文件名和结构保持不变
     */
    private void copyFromAssets(String assets) {

        InputStream source = null;
        try {
            source = getAssets().open(assets);
            File DestFile = new File(rootFolder, assets);
            if (!DestFile.getParentFile().exists()) {
                Log.v(TAG, "Parent File do not exists. mkdir...");
                DestFile.getParentFile().mkdirs();
            }
            OutputStream dest = new FileOutputStream(DestFile);
            byte[] buffer = new byte[4096];
            int nread;
            while ((nread = source.read(buffer)) != -1) {
                if (nread == 0) {
                    nread = source.read();
                    if (nread < 0)
                        break;
                    dest.write(nread);
                    continue;
                }
                dest.write(buffer, 0, nread);
            }
            dest.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 重新启动应用。
     * 如果应用为single task 启动模式，不能实现重启动
     */
    public void restartApp(){
        Intent intent = new Intent(mAppContext,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mAppContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void setContext(Context context) {
        mContext = context;
    }

}
