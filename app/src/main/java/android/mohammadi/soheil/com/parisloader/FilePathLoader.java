package android.mohammadi.soheil.com.parisloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;

/**
 * Created by soheilmohammadi on 7/23/18.
 */


public  class FilePathLoader extends Utils  {

    private   Bitmap bitmap ;
    private ParisSetter parisSetter ;


    public FilePathLoader(Context context , ParisSetter parisSetter) {
        super(context , parisSetter);
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
                setImage(getFromCache());
            }else if(parisSetter.isAddToCache())
                bitmap = getFromCache();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            if (bitmap == null || parisSetter.isLoaderOne()) {
                if(parisSetter.getView() != null) {
                    if(parisSetter.getView().getTag().equals(parisSetter.getKey())) {
                        if(new File(parisSetter.getKey()).exists())
                            return  getBlurBitmap(BitmapFactory.decodeFile(parisSetter.getKey() ,
                                    getBitmapOptions(parisSetter.getKey())));
                        else
                            return getBlurBitmap(BitmapFactory.decodeResource(context.getResources() , parisSetter.getPlaceHolder() ,
                                    getBitmapOptions(parisSetter.getPlaceHolder())));

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

                FilePathLoader.this.bitmap = null ;

            }

        }

    }

}


