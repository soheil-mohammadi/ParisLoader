package android.mohammadi.soheil.com.parisloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Created by soheilmohammadi on 7/23/18.
 */

public class ResourceLoader extends  Utils  {


    private Bitmap bitmap ;
    private ParisSetter parisSetter ;

    public ResourceLoader(Context context , ParisSetter parisSetter) {
        super(context , parisSetter);
        this.parisSetter = parisSetter ;
    }


    public  void build(){
        new Loader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    class Loader extends AsyncTask<Void, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(parisSetter.isLoaderOne())
                setImage(getFromCache());
            else if(parisSetter.isAddToCache())
                bitmap = getFromCache();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            if (bitmap == null || parisSetter.isLoaderOne()) {
                if(parisSetter.getView() != null) {
                    if(parisSetter.getView().getTag().equals(parisSetter.getKey()))
                        return  getBlurBitmap(BitmapFactory.decodeResource(context.getResources() , parisSetter.getResourceId() ,
                                getBitmapOptions(parisSetter.getResourceId())));

                    return  null ;
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

                if(parisSetter.isAddToCache())
                    addToCache(bitmap);

                ResourceLoader.this.bitmap = null ;
            }else  {
                if(parisSetter.isAddToCache())
                    addToCache(BitmapFactory.decodeResource(context.getResources() , parisSetter.getPlaceHolder() ,
                            getBitmapOptions(parisSetter.getPlaceHolder())));
            }



        }

    }


}
