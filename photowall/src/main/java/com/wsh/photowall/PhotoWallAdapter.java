package com.wsh.photowall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wsh-03 on 2016/11/22.
 */

public class PhotoWallAdapter extends ArrayAdapter<String> implements AbsListView.OnScrollListener{
    private static final String TAG = "PhotoWallAdapter";

    private Set<BitmapTask> taskCollection;
    private GridView mPhotoWall;
    private LruCache<String, Bitmap> mMemoryCache;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private boolean firstEnter = true;

    public PhotoWallAdapter(Context context, int resource, String[] objects,
                            GridView photoWall) {
        super(context, resource, objects);
        mPhotoWall = photoWall;
        taskCollection = new HashSet<BitmapTask>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        mPhotoWall.setOnScrollListener(this);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String url = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_layout, null);
        } else {
            view = convertView;
        }
        final ImageView photo = (ImageView) view.findViewById(R.id.photo);
        photo.setTag(url);
        setImageView(url, photo);
        return view;
    }

    public void setImageView(String url, ImageView imageView) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.empty_photy);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmap(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;

        if (firstEnter && visibleItemCount > 0) {
            loadBitmap(firstVisibleItem, visibleItemCount);
            firstEnter = false;
        }
    }

    private void loadBitmap(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i ++) {
                String imageUrl = Images.imageThumbUrls[i];
                Bitmap bitmap = getBitmapFromCache(imageUrl);
                if (bitmap != null) {
                    ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                } else {
                    BitmapTask btask = new BitmapTask();
                    taskCollection.add(btask);
                    btask.execute(imageUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Memory Cache
     */
    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String key) {
        return mMemoryCache.get(key);
    }

    public void cancelAllTask(){
        if (taskCollection != null) {
            for (BitmapTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    /*
     * BitmapTask
     */
    public class BitmapTask extends AsyncTask<String, Integer, Bitmap> {

        private String imageUrl;
        @Override
        protected Bitmap doInBackground(String... params) {

            imageUrl = params[0];
            Bitmap bitmap = downloadBitmap(imageUrl);
            if (bitmap != null) {
                addBitmapToCache(imageUrl, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ImageView imageview = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
            if (imageview != null && bitmap != null) {
                imageview.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }

        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(imageUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5 * 1000);
                httpURLConnection.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return bitmap;
        }
    }

}
