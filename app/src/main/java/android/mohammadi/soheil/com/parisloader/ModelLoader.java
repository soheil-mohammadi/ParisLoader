package android.mohammadi.soheil.com.parisloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Created by soheilmohammadi on 7/23/18.
 */

public class ModelLoader  extends  Utils{

    private Bitmap bitmap ;

    private ParisSetter parisSetter ;

    public ModelLoader(Context context , ParisSetter parisSetter) {
        super(context , parisSetter);
        this.parisSetter = parisSetter ;
    }


    public  void build(){
        new Loader().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public class Loader extends AsyncTask<Void, Void, Bitmap>  {

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
                    if(parisSetter.getView().getTag().equals(parisSetter.getKey())) {
                        if(parisSetter.getByteModel() != null) {
                            byte[] buffer = parisSetter.getByteModel().onModel();
                            if(buffer != null)
                                return  getBlurBitmap(BitmapFactory.decodeByteArray(buffer , 0 , buffer.length , getBitmapOptions(buffer)));
                            return getBlurBitmap(BitmapFactory.decodeResource(context.getResources() , parisSetter.getPlaceHolder() ,
                                    getBitmapOptions(parisSetter.getPlaceHolder())));
                        }else  if(parisSetter.getBitmapModel() != null) {
                            return  getBlurBitmap(parisSetter.getBitmapModel().onModel());
                        }

                        return null;
                    }
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
                        if(parisSetter.getBitmapModel() != null)
                            setImage(getScaledDownBitmap(bitmap , 85));
                        else if(parisSetter.getByteModel() != null)
                            setImage(bitmap);
                    }
                }

                if(parisSetter.getBitmapModel() == null)
                    if(parisSetter.isAddToCache() && !parisSetter.isLoaderOne())
                        addToCache(bitmap);

                ModelLoader.this.bitmap = null ;

            }

        }


    }

}
