package android.mohammadi.soheil.com.parisloader;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;

import Listeners.OnCustomBitmapModelParis;
import Listeners.OnCustomByteModelParis;

/**
 * Created by soheilmohammadi on 7/23/18.
 */

 abstract class ParisSetter {


    abstract View getView();

    abstract Boolean isAddToCache();

    abstract String getKey();

    abstract float getBlur();

    abstract OnCustomByteModelParis getByteModel();

    abstract OnCustomBitmapModelParis getBitmapModel();

    abstract int getResourceId();

    abstract byte[] getByte();

    abstract DiskLruCache getDiskLruCache();

    abstract LruCache<String , Bitmap> getCacheList();

    abstract  Boolean isLoaderOne();

    abstract  int getPlaceHolder();


}
