package com.tarapogancev.denoise.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "PLAY": {
                    MediaPlayerService.getInstance().resume();
                    Intent in = new Intent("RefreshPlayPause");
                    context.sendBroadcast(in);
                    break;
                }
                case "PAUSE": {
                    MediaPlayerService.getInstance().pause();
                    Intent in = new Intent("RefreshPlayPause");
                    context.sendBroadcast(in);
                    break;
                }
                case "NEXT": {
                    MediaPlayerService.getInstance().next(context);
                    break;
                }
                case "PREVIOUS": {
                    MediaPlayerService.getInstance().previous(context);
                    break;
                }
                default: {
                    MediaPlayerService.getInstance().close();
                    Intent serviceIntent = new Intent(context, MediaPlayerService.class);
                    context.stopService(serviceIntent);
                    Intent in = new Intent("RefreshPlayPause");
                    context.sendBroadcast(in);
                    break;
                }
            }
        }
    }

}
