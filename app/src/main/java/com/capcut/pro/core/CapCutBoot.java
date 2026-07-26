package com.capcut.pro.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CapCutBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startForegroundService(new Intent(context, CapCutService.class));
        }
    }
}