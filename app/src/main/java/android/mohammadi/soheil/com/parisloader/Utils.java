package android.mohammadi.soheil.com.parisloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by soheilmohammadi on 7/23/18.
 */

class Utils  {

    protected Context context ;
    private ParisSetter parisSetter;

    private  int destinationSize = 850 ;


    private static final String TAG = "Utils";


    Utils(Context context , ParisSetter parisSetter ) {
        this.context = context ;
        this.parisSetter = parisSetter;
        this.parisSetter.getView().setTag(parisSetter.getKey());
    }

    Bitmap getFromCache() {
        Bitmap bitmap = null ;

        if(parisSetter.isAddToCache()) {
            if(parisSetter.getKey() != null) {
                if(parisSetter.getDiskLruCache() != null)
                    bitmap =  parisSetter.getDiskLruCache().get(parisSetter.getKey());
                else {
                    bitmap = parisSetter.getCacheList().get(parisSetter.getKey());

                }
                return bitmap;

            }
        }


        return  bitmap;

    }


    Bitmap getBlurBitmap(Bitmap image ) {

        if (image == null)
            return null;

        if(parisSetter.getBlur() == 0)
            return image;

        Bitmap outputBitmap = Bitmap.createBitmap(image.getWidth() , image.getHeight() , Bitmap.Config.ARGB_8888);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(parisSetter.getBlur());
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }




    BitmapFactory.Options getBitmapOptions(String pathFile){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathFile, options);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int xDim = size.x;
        int yDim = size.y;

        int inSampleSize = 1;

        while ((options.outHeight / inSampleSize) > (yDim/ (parisSetter.isLoaderOne() ? 1 : 4))
                || (options.outWidth / inSampleSize) > (xDim/(parisSetter.isLoaderOne() ? 1 : 4)))
        {inSampleSize += 1;}


        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return options;
    }



    BitmapFactory.Options getBitmapOptions(byte[] byteBuffer){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteBuffer  , 0 , byteBuffer.length , options);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int xDim = size.x;
        int yDim = size.y;

        int inSampleSize = 1;

        while ((options.outHeight / inSampleSize) > (yDim/(parisSetter.isLoaderOne() ? 1 : 4))
                || (options.outWidth / inSampleSize) > (xDim/(parisSetter.isLoaderOne() ? 1 : 4)))
        {inSampleSize += 1;}


        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return options;
    }


    /**
     * @param bitmap the Bitmap to be scaled
     * @param threshold the maximum dimension (either width or height) of the scaled bitmap
     **/

    Bitmap getScaledDownBitmap(Bitmap bitmap, int threshold){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;

        if(width > height && width > threshold){
            newWidth = threshold;
            newHeight = (int)(height * (float)newWidth/width);
        }

        if(width > height && width <= threshold){
            return bitmap;
        }

        if(width < height && height > threshold){
            newHeight = threshold;
            newWidth = (int)(width * (float)newHeight/height);
        }

        if(width < height && height <= threshold){
            return bitmap;
        }

        if(width == height && width > threshold){
            newWidth = threshold;
            newHeight = newWidth;
        }

        if(width == height && width <= threshold){
            return bitmap;
        }

        return getResizedBitmap(bitmap, newWidth, newHeight);
    }

    private  Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = null ;
        if(bm != null) {
            resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        }

        return resizedBitmap;
    }


    BitmapFactory.Options getBitmapOptions(int resourceId){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources()  , resourceId  , options);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int xDim = size.x;
        int yDim = size.y;

        int inSampleSize = 1;

        while ((options.outHeight / inSampleSize) > (yDim/2)
                || (options.outWidth / inSampleSize) > (xDim/2))
        {inSampleSize += 1;}


        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return options;
    }



    Bitmap getBitmapQuality(Bitmap bitmap){

        if(bitmap != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int xDim = size.x;
            int yDim = size.y;

            int inSampleSize = 1;

            while ((bitmap.getHeight() / inSampleSize) > (yDim/2)
                    || (bitmap.getWidth() / inSampleSize) > (xDim/2))
            {inSampleSize += 1;}


            return Bitmap.createScaledBitmap(bitmap , bitmap.getWidth() / inSampleSize, bitmap.getHeight() / inSampleSize , false);
        }

        return  null ;
    }





    Bitmap manageBitmapCache(Bitmap bitmap) {

        if(bitmap != null) {
            Bitmap finalBitmap = null;
            Bitmap bitmapCached = getFromCache();

            int bitmapSize = bitmap.getByteCount() / 1024 ;

            double destinationSize ;


            if(this.destinationSize >= bitmapSize){
                try {
                    destinationSize = (this.destinationSize / bitmapSize );
                    destinationSize = Math.sqrt(destinationSize) ;
                }catch (ArithmeticException e) {
                    destinationSize = 5;
                }

            }else  {
                destinationSize = (bitmapSize / this.destinationSize) ;
                destinationSize = Math.sqrt(destinationSize) ;
            }


            if (bitmapCached == null) {
                if(bitmap != null) {
                    finalBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / (int)destinationSize  ,
                            bitmap.getHeight() / (int)destinationSize , false);
                    if (parisSetter.isAddToCache())
                        addToCache(finalBitmap);
                }

            } else {
                finalBitmap = bitmapCached;
            }


            return finalBitmap;
        }else  {
            return  null ;
        }

    }


    void addToCache( Bitmap bitmap) {

        if(bitmap != null && parisSetter.getKey() != null) {
            if(parisSetter.getDiskLruCache() != null) {
                parisSetter.getDiskLruCache().put(parisSetter.getKey() , bitmap);
            }else  {
                parisSetter.getCacheList().put(parisSetter.getKey(), bitmap);
            }
        }

    }


    void setImage( Bitmap bitmap) {
        if(parisSetter.getView() instanceof  ImageView)
            ((ImageView) parisSetter.getView()).setImageBitmap(bitmap);
        else
            parisSetter.getView().setBackground(new BitmapDrawable(context.getResources() , bitmap ));

    }


}
