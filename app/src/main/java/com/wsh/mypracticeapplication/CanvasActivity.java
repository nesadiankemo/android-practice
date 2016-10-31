package com.wsh.mypracticeapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class CanvasActivity extends AppCompatActivity {
    private static final String TAG = "CanvasActivity";
    private MyView myView;
    Bitmap bitmap;
    Paint mPaint;
    RectF mRectF;
    BitmapShader mBitmapShader;
    BroadcastReceiver broadcastReceiver;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_list);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        Toast.makeText(getApplicationContext(), "item 1 is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item2:
                        Toast.makeText(getApplicationContext(), "item 2 is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        //设置View 调用onDraw() 时的回调函数， 用于重绘画面
        /*myView = (MyView) findViewById(R.id.my_view);
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("test.png"));
        } catch (IOException e) {
            Log.v(TAG, "getAssets().open error");
            e.printStackTrace();
        }
        mPaint = new Paint();
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        mRectF = new RectF(0, 0, bitmap.getWidth() * 2, bitmap.getHeight() * 2);
        mPaint.setAntiAlias(true);

        myView.setOnDrawCallback(new MyView.OnDrawCallback() {
            @Override
            public void onDrawCallback(Canvas canvas) {
                int width = canvas.getWidth();
                int height = canvas.getHeight();
                float centerX = width / 2f;
                float centerY = height / 2f;
                float radius = width / 4f;
//                Path path = new Path();
//                path.moveTo(0, 0);
//                path.lineTo(150, 50);
//                path.lineTo(80, 170);
//                path.lineTo(0, 0);
                LinearGradient linearGradient = new LinearGradient(0, 0, 400, 400, Color.YELLOW, Color.BLUE, Shader.TileMode.MIRROR);
                RadialGradient radialGradient = new RadialGradient(centerX, centerY, radius, Color.RED, Color.BLACK, Shader.TileMode.REPEAT);
                mPaint.setShader(radialGradient);
                mPaint.setAlpha(200);
                if (mPaint != null) {
//                    canvas.drawRect(mRectF, mPaint);
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
                }

            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
