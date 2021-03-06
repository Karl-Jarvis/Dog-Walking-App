package com.example.signin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView){
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... strings){
        return downloadBitmap(strings[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()){
            bitmap = null;
        }

        ImageView imageView = imageViewReference.get();
        if(imageView != null){
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
        }

    }

    private Bitmap downloadBitmap(String url){
        HttpsURLConnection urlConnection = null;
        try{
            URL uri = new URL(url);
            urlConnection = (HttpsURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if(statusCode != HttpsURLConnection.HTTP_OK){
                return null;
            }
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream!= null){
                return BitmapFactory.decodeStream(inputStream);
            }
        }catch (Exception e) {
            urlConnection.disconnect();
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return null;
    }

}
