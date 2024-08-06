package com.animation.chathead;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ChatHead {

    ChatHeadListener chatHeadListener;
    private ServiceConnection connection;

    ChatHeadService chatHeadService;

    boolean isBound;

    public ChatHead(Context context) {

        this.chatHeadListener = (ChatHeadListener) context;

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                ChatHeadService.LocalBinder binder = (ChatHeadService.LocalBinder) service;
                chatHeadService = binder.getService();
                isBound = true;
                chatHeadService.setListener(chatHeadListener);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent1 = new Intent(context , ChatHeadService.class);
        context.bindService(intent1 , connection , Context.BIND_AUTO_CREATE);

    }
}
