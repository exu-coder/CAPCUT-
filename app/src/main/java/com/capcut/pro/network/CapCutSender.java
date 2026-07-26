package com.capcut.pro.network;

import android.util.Log;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CapCutSender {
    private static final String TAG = "CapCutSender";
    private String sessionToken;
    private String channelId;

    public CapCutSender(String apiId, String apiHash, String channelId, String ownerId, String sessionToken) {
        this.channelId = channelId;
        this.sessionToken = sessionToken;
    }

    public void sendMessage(String text) {
        new Thread(() -> {
            try {
                String url = "https://api.telegram.org/bot" + sessionToken + "/sendMessage";
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject data = new JSONObject();
                data.put("chat_id", channelId);
                data.put("text", text);
                data.put("parse_mode", "HTML");

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(data.toString());
                out.flush();
                out.close();

                int code = conn.getResponseCode();
                Log.d(TAG, "Message sent, code: " + code);
                conn.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error", e);
            }
        }).start();
    }
}