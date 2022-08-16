package com.tarapogancev.denoise.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "PLAY": {
                    MediaPlayerService.getInstance().resume();
                    break;
                }
                case "PAUSE": {
                    MediaPlayerService.getInstance().pause();
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
                default:    {
                    MediaPlayerService.getInstance().close();
                    Intent serviceIntent = new Intent(context, MediaPlayerService.class);
                    context.stopService(serviceIntent);
                    break;
                }
            }
        }
    }

}
