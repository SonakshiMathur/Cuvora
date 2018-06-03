package com.sonakshi.cuvora;

import java.io.File;
import java.net.URLEncoder;

import android.content.Context;
import android.util.Log;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "Sona");
            Log.i("if","1");}
        else{
            cacheDir=context.getCacheDir();Log.i("else","2");}
        if(!cacheDir.exists()) {
            Boolean f=cacheDir.mkdirs();
            Log.i("ifkl",Boolean.toString(f));
        }
    }

    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}