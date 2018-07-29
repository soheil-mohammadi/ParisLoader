package android.mohammadi.soheil.com.parisloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;

import Listeners.OnCustomBitmapModelParis;
import Listeners.OnCustomByteModelParis;

/**
 * Created by soheilmohammadi on developer/29/18.
 */

public class ParisLoader {

    private static final String TAG = "ParisLoader";

    private static ParisLoader parisInstance = null;
    private   LruCache<String, Bitmap> cacheList;

    private Context context;

    private static final  int LOADER_CUSTOM_MODEL = 1 ;
    private  static final  int LOADER_FILE_PATH = 2 ;
    private static final  int LOADER_URL = 3 ;
    private static final  int LOADER_RESOURCE = 4 ;
    private static final  int LOADER_BYTES = 5 ;
    private static final  int LOADER_FILE_PATH_ONE = 6 ;
    private static final  int LOADER_URL_ONE = 7 ;
    private static final  int LOADER_BYTES_ONE = 8 ;
    private static final  int LOADER_CUSTOM_MODEL_ONE = 9 ;


    public ParisLoader(Context context) {
        this.context = context;

        cacheList = new LruCache<String, Bitmap>((int) ((Runtime.getRuntime().maxMemory() / 1024) / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }

        };

    }


    public static ParisLoader with(Context context) {
        if(parisInstance == null)
            parisInstance = new ParisLoader(context);
        return  parisInstance ;
    }



        /*One  Loader */

    public Target loadOne(@NonNull  String filePath) {
        if(filePath == null || filePath.trim().equals(""))
            throw  new IllegalArgumentException("ParisLoader : filePath Can not be null or empty !");

        return new Target().initLoader(LOADER_FILE_PATH_ONE).setPath(filePath);
    }


    public Target loadOne(URL url){
        if(url == null || url.toString().trim().equals(""))
            throw  new IllegalArgumentException("ParisLoader : Url Can not be null or empty !");

        return new Target().initLoader(LOADER_URL_ONE).setPath(url.toString());
    }

    public Target loadOne(@NonNull byte[] bytes) {
        if(bytes == null || bytes.length == 0)
            throw  new IllegalArgumentException("ParisLoader : Buffer Byte Data  Can not be null or empty !");

        return new Target().initLoader(LOADER_BYTES_ONE).setByteBuffer(bytes).setKey(bytes.toString().substring(0 , bytes.length - (bytes.length / 2 )));
    }


    public Target loadOne(@NonNull OnCustomByteModelParis onCustomByteModelParis, @NonNull String key) {
        if(onCustomByteModelParis == null )
            throw  new IllegalArgumentException("ParisLoader : Custom Model Data  Can not be null  !");

        if(key == null || key.trim().equals(""))
            throw  new IllegalArgumentException("ParisLoader : Key of Custom Model Data  Can not be null or empty !");

        return new Target().initLoader(LOADER_CUSTOM_MODEL_ONE).setCustomByteModel(onCustomByteModelParis).setKey(key);
    }




        /*Multiple Loader */

    public Target load(@NonNull String filePath) {
        if(filePath == null || filePath.trim().equals(""))
            throw  new IllegalArgumentException("ParisLoader : filePath Can not be null or empty !");

        return new Target().initLoader(LOADER_FILE_PATH).setPath(filePath);
    }


    public Target loadUrl(@NonNull URL url) {
        if(url == null || url.toString().trim().equals(""))
            throw  new IllegalArgumentException("ParisLoader : Url Can not be null or empty !");

        return new Target().initLoader(LOADER_URL).setPath(url.toString());
    }


    public Target load(@NonNull byte[] bytes) {
        if(bytes == null || bytes.length == 0)
            throw  new IllegalArgumentException("ParisLoader : Buffer Byte Data  Can not be null or empty !");

        return new Target().initLoader(LOADER_BYTES).setByteBuffer(bytes).setKey(bytes.toString().substring(0 , bytes.length - (bytes.length / 2 )));
    }

    public Target load(@NonNull int resource) {
        if(resource == 0 )
            throw  new IllegalArgumentException("ParisLoader : Invalid Resource Id  !");

        return new Target().initLoader(LOADER_RESOURCE).setResourceId(resource).setKey(context.getResources().getResourceName(resource));
    }

    public Target load(@NonNull OnCustomByteModelParis onCustomByteModelParis, @NonNull String key) {
        if(onCustomByteModelParis == null )
            throw  new IllegalArgumentException("ParisLoader : Custom Model Data  Can not be null  !");

        if(key == null || key.trim().equals(""))
            throw  new IllegalArgumentException("ParisLoader : Key of Custom Model Data  Can not be null or empty !");

        return new Target().initLoader(LOADER_CUSTOM_MODEL).setCustomByteModel(onCustomByteModelParis).setKey(key);
    }



    public Target load(@NonNull OnCustomBitmapModelParis onCustomBitmapModelParis, @NonNull String key) {
        if(onCustomBitmapModelParis == null )
            throw  new IllegalArgumentException("ParisLoader : Custom Model Data  Can not be null  !");

        if(key == null || key.trim().equals(""))
            throw  new IllegalArgumentException("ParisLoader : Key of Custom Model Data  Can not be null or empty !");

        return new Target().initLoader(LOADER_CUSTOM_MODEL).setCustomByteModel(onCustomBitmapModelParis).setKey(key);

    }



    public  class Target {

        private byte[] byteBuffer;

        private int resourceId;

        private String key;


        private float blur = 0f;

        private OnCustomByteModelParis onCustomByteModelParis;
        private OnCustomBitmapModelParis onCustomBitmapModelParis;

        private int resPlaceHolder = 0;

        private int typeLoader;

        private DiskLruCache diskLruCache = null;

        private Boolean isAddToCache = true;

        private Boolean isLoaderOne = false;

        private View view;

        private Target initLoader(int typeLoader) {
            this.typeLoader = typeLoader;
            return this;

        }

        private class Goal extends   ParisSetter {

            @Override
            View getView() {
                return view;
            }

            @Override
            Boolean isAddToCache() {
                return isAddToCache;
            }

            @Override
            String getKey() {
                return key;
            }

            @Override
            float getBlur() {
                return blur;
            }

            @Override
            OnCustomByteModelParis getByteModel() {
                return onCustomByteModelParis;
            }

            @Override
            OnCustomBitmapModelParis getBitmapModel() {
                return onCustomBitmapModelParis;
            }

            @Override
            int getResourceId() {
                return resourceId;
            }

            @Override
            byte[] getByte() {
                return byteBuffer;
            }

            @Override
            DiskLruCache getDiskLruCache() {
                return diskLruCache;
            }

            @Override
            LruCache<String, Bitmap> getCacheList() {
                return cacheList;
            }

            @Override
            Boolean isLoaderOne() {
                return isLoaderOne;
            }

            @Override
            int getPlaceHolder() {
                return resPlaceHolder;
            }

        }




        private Target setResourceId(int resourceId) {
            this.resourceId = resourceId;
            return this;
        }


        private Target setByteBuffer(byte[] byteBuffer) {
            this.byteBuffer = byteBuffer;
            return this;
        }

        public Target blur(float blur) {
            this.blur = blur;
            return this;
        }


        private Target setCustomByteModel(OnCustomByteModelParis onCustomByteModelParis) {
            this.onCustomByteModelParis = onCustomByteModelParis;
            return this;
        }


        private Target setCustomByteModel(OnCustomBitmapModelParis onCustomBitmapModelParis) {
            this.onCustomBitmapModelParis = onCustomBitmapModelParis;
            return this;
        }




        public Target placeHolder(int resPlaceHolder) {
            this.resPlaceHolder = resPlaceHolder;
            return this;
        }

        public Target addToCache(Boolean isAddToCache) {
            this.isAddToCache = isAddToCache;
            return this;
        }


        public Target setDiskCache(String cachePath) {
            diskLruCache = DiskLruCache.open(cachePath, context);
            return this;
        }


        private Target setPath(String path) {
            this.key = path;
            return this;
        }


        private Target setKey(String key) {
            this.key = key;
            return this;
        }


        public void into(View view) {

            this.view = view;

            if (resPlaceHolder != 0) {
                if (view instanceof ImageView)
                    ((ImageView) view).setImageResource(resPlaceHolder);
                else
                    view.setBackgroundResource(resPlaceHolder);

            }


            switch (typeLoader) {
                case LOADER_FILE_PATH:
                    new FilePathLoader(context, new Goal()).build();
                    break;


                case LOADER_FILE_PATH_ONE:
                    this.isLoaderOne = true;
                    new FilePathLoader(context, new Goal()).build();
                    break;

                case LOADER_URL:
                    new NetworkLoader(context, new Goal()).build();
                    break;


                case LOADER_URL_ONE:
                    this.isLoaderOne = true;
                    new NetworkLoader(context, new Goal()).build();
                    break;


                case LOADER_CUSTOM_MODEL:
                    new ModelLoader(context,  new Goal()).build();
                    break;


                case LOADER_CUSTOM_MODEL_ONE:
                    this.isLoaderOne = true;
                    new ModelLoader(context,  new Goal()).build();
                    break;


                case LOADER_RESOURCE:
                    new ResourceLoader(context,  new Goal()).build();
                    break;


                case LOADER_BYTES:
                    new ByteLoader(context,  new Goal()).build();
                    break;


                case LOADER_BYTES_ONE:
                    this.isLoaderOne = true;
                    new ByteLoader(context,  new Goal()).build();
                    break;

            }

        }

    }


}

