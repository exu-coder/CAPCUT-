package com.capcut.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.capcut.pro.core.CapCutService;

public class CapCutMain extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = new Intent(this, CapCutService.class);
        startForegroundService(intent);
        
        handler.postDelayed(() -> finish(), 100);
    }
}