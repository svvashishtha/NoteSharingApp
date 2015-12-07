package com.app.notes.utilities;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;


public class FileUtils {
    public static  File docFolder = new File(Environment.getExternalStorageDirectory(), "zimply/documents/");
    public static File productFolder = new File(Environment.getExternalStorageDirectory(), "zimply/products/");
    //public static File productCacheFolder = new File(ZiApplication.getContext().getCacheDir(), "product");
    public static boolean saveFile(Bitmap bitmap, File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static String getFileNameURL(String url){
        return url.substring(url.lastIndexOf("/")+1);
    }
}
