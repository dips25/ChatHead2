package com.chat.mychathead;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.animation.chathead.ChatHead;
import com.animation.chathead.ChatHeadListener;

public class MainActivity extends AppCompatActivity implements ChatHeadListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startChatHead();



        //ChatHead chatHead = new ChatHead(this);

    }

    private void startChatHead() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())
                );
                startActivityForResult(intent, 1001);
            } else {
                // Permission already granted
                startChatHeadService();
            }
        }
    }

    private void startChatHeadService() {

        ChatHead chatHead = new ChatHead(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // Permission granted
                    startChatHeadService();
                } else {
                    Toast.makeText(this,
                            "Overlay permission denied",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onChatHeadClicked() {

        Log.d(MainActivity.class.getName(), "onChatHeadClicked");
    }
}