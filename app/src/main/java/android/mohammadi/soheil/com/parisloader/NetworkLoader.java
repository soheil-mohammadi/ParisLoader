package android.mohammadi.soheil.com.parisloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by soheilmohammadi on 7/27/18.
 */

public class NetworkLoader extends Utils {

    private Bitmap bitmap ;
    private ParisSetter parisSetter ;

    private static final String TAG = "NetworkLoader";

    NetworkLoader(Context context, ParisSetter parisSetter) {
        super(context, parisSetter);
        this.parisSetter = parisSetter ;
    }

    public  void build(){
        new Loader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    private class  Loader extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(parisSetter.isLoaderOne()) {
                if(getFromCache() != null)
                    setImage(getFromCache());
            }else if(parisSetter.isAddToCache())
                bitmap = getFromCache();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            if (bitmap == null || parisSetter.isLoaderOne()) {
                if(parisSetter.getView() != null) {
                    if(parisSetter.getView().getTag().equals(parisSetter.getKey())) {
                        return  getBlurBitmap(BitmapFactory.decodeStream(
                                readNetworkData(parisSetter.getKey()) , null,
                                getBitmapOptions(parisSetter.getKey())));

                    }else  {
                        return  null ;
                    }
                }

                return null ;

            } else {
                return bitmap;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null) {
                if(parisSetter.getView() != null) {
                    if(parisSetter.getView().getTag().toString().equals(parisSetter.getKey())) {
                        setImage(bitmap);
                    }
                }

                if(parisSetter.isAddToCache() & !parisSetter.isLoaderOne())
                    addToCache(bitmap);

                NetworkLoader.this.bitmap = null ;

            }

        }


        private InputStream  readNetworkData(String pathUrl) {
            try {
                URL url = new URL(pathUrl);
                URLConnection connection = url.openConnection();
                connection.setDoInput(true);
                return  connection.getInputStream();

            } catch (IOException e) {
                Log.e(TAG, "ParisNetworkData : " + e );
            }

            return null ;

        }

    }


}
