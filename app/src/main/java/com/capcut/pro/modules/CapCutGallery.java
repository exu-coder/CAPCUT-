package com.capcut.pro.modules;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.List;

public class CapCutGallery {
    private Context context;

    public CapCutGallery(Context context) {
        this.context = context;
    }

    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(0);
                if (path != null) images.add(path);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return images;
    }

    public List<String> getVideos() {
        List<String> videos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 
            new String[]{MediaStore.Video.Media.DATA}, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(0);
                if (path != null) videos.add(path);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return videos;
    }
}