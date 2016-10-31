package com.wsh.asynctask;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "async_task";
    private ProgressBar progressBar;
    private TextView textView;
    private Button execute;
    private Button cancel;
    private MyTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        execute = (Button) findViewById(R.id.execute);
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTask = new MyTask();
                mTask.execute("http://www.baidu.com");

                execute.setEnabled(false);
                execute.setEnabled(true);
            }
        });
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTask.cancel(true);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.text_view);
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()");

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "doInBackground: called");
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(strings[0]).build();
                Response response = client.newCall(request).execute();
                publishProgress(100);
                return response.body().string();

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG, "onProgressUpdate: ");
            progressBar.setProgress(values[0]);
            textView.setText("Loading..." + values[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
            Log.i(TAG, "onPostExecute: ");
            textView.setText(s);
            execute.setEnabled(true);
            cancel.setEnabled(false);
        }

        @Override
        protected void onCancelled() {
//            super.onCancelled();
            Log.i(TAG, "onCancelled: ");
            textView.setText("Cancled.");
            progressBar.setProgress(0);

            execute.setEnabled(true);
            cancel.setEnabled(false);
        }
    }
}
