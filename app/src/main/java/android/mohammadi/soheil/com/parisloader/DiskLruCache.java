package android.mohammadi.soheil.com.parisloader;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

/**
 * Created by soheilmohammadi on developer/31/18.
 */

public class DiskLruCache {

    private static final String TAG = "DiskLruCache";
    private  static  DiskLruCache instance ;
    private String fileCachePath ;
    private Context context ;
    private SQLiteDatabase sqLiteDatabase ;

    private  String dirNameInternalCached ;
    private  String dirNameExternalCached ;

    public DiskLruCache(String fileCachePath , Context context ) {
        this.fileCachePath = fileCachePath;
        this.context = context ;
        dirNameInternalCached =  context.getFilesDir().getAbsolutePath() ;
        dirNameExternalCached =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "DiskLruCachedParisLoader";
        String databasePath =  new File(fileCachePath).getParent() + "/cacheDataBase.db";
        if(!new File(databasePath).exists()){
            try {
                new FileOutputStream(databasePath);
                sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath , null);
                sqLiteDatabase.execSQL("CREATE TABLE `Cache` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT , `key` TEXT  , `path` TEXT );");
                cleanDiskCache();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else  {
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath , null);
        }

    }

    private void cleanDiskCache() {
        String[] listInternalStorageCached = new File(context.getFilesDir().getAbsolutePath()).list() ;

        if(listInternalStorageCached.length > 0 ) {
            for (String file : listInternalStorageCached  ) {
                if(file.endsWith(".png")) {
                    new File(file).delete();
                }
            }

        }else  {
            if(ContextCompat.checkSelfPermission(context , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(new File(dirNameExternalCached).exists()) {
                    String[] listExternalStorageCached = new File(dirNameExternalCached ).list() ;
                    for (String file : listExternalStorageCached  ) {
                        if(file.endsWith(".png")) {
                            new File(file).delete();
                        }
                    }
                }

            }
        }

    }

    public static DiskLruCache   open(String fileCachePath , Context context) {
        if(instance == null)
            instance = new DiskLruCache(fileCachePath , context);

        return instance ;
    }


    public void put (String key , Bitmap value) {
        Log.e(TAG, "put: " + key );
        String pathCached = saveBitmapFile(value) ;
        if(pathCached != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("key" , key);
            contentValues.put("path" , pathCached);
            sqLiteDatabase.insert("Cache" , null , contentValues);
        }
    }

    private String saveBitmapFile(final Bitmap bitmap) {

        final String pathParentDirCache = manageSpaceDevice(bitmap);
        String pathSaved = null ;

        if(pathParentDirCache != null) {
            try {
                pathSaved = getRandomPath( pathParentDirCache , ".png" );
                FileOutputStream fileOutputStream = new FileOutputStream(new File(pathSaved));
                bitmap.compress(Bitmap.CompressFormat.PNG ,  100 , fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return pathSaved;
    }

    private String manageSpaceDevice(Bitmap bitmap) {

        long bitmapSizeKB =  (bitmap.getRowBytes() * bitmap.getHeight() ) / 1024;
        if(getSpaceKB(dirNameInternalCached) > bitmapSizeKB) {
            return dirNameInternalCached ;
        }else  if(getSpaceKB(Environment.getExternalStorageDirectory().getAbsolutePath()) > bitmapSizeKB) {
            if(ContextCompat.checkSelfPermission(context , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "ParisLoader : WRITE EXTERNAL STORAGE IS DENIED !" );
                return  null;
            }else  {
                new File(dirNameExternalCached).mkdirs();
                return  dirNameExternalCached ;
            }
        }

        return  null ;
    }


    private long getSpaceKB(String path) {
        StatFs statFs = new StatFs(path);
        return  ( (long)statFs.getBlockCount() * (long)statFs.getBlockSize() ) / 1024;
    }


    public Bitmap get (String key) {

        Bitmap cacheBitmap = null ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT `path` FROM `Cache` WHERE `key` =  " +  "'" + key +  "'" ,  null);
        if(cursor.getCount() > 0 ){
            cursor.moveToFirst();
            cacheBitmap = BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndexOrThrow("path")));
        }

        cursor.close();
        return  cacheBitmap ;
    }


    private String getRandomStr(int length) {
        Random random = new Random((new Date()).getTime());
        char[] values = {'a','b','c','d','e','f','g','h','i','j',
                'k','l','m','n','o','p','q','r','s','t',
                'u','v','w','x','y','z','0','1','2','3',
                '4','5','6','7','8','9'};

        String out = "";

        for (int i=0;i<length;i++) {
            int idx=random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }


    private String getRandomPath(String prefixPath, String extention) {
        String path = prefixPath + "/HooHoo-" + getRandomStr(10) + extention;
        File file = new File(path);
        if (file.exists()) {
            return getRandomPath(prefixPath, extention);
        } else {
            return path;
        }
    }



}
