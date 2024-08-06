package com.animation.chathead;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class ChatHeadService extends Service {
    int X , Y;
    int initialTouchX , initialTouchY;
    View imageView;

    ImageView imageView1;

    boolean isClicked;



    final Rect chatHeadRect = null;
    WindowManager windowManager;
    WindowManager.LayoutParams lp , dp;

    boolean withinBounds;

    ChatHeadListener listener;

    Context context;

    ChatHeadListener chatHeadListener;

    public ChatHeadService() {
    }

    public void setListener(ChatHeadListener chatHeadListener) {

        this.chatHeadListener = chatHeadListener;
        Log.d("klkj", "setListener: ");


    }

    public class LocalBinder extends Binder {

        public ChatHeadService getService() {

            return ChatHeadService.this;
        }
    }

    IBinder binder = new LocalBinder();


    @Override
    public void onCreate() {
        super.onCreate();

        imageView = LayoutInflater.from(this).inflate(R.layout.chat_head_layout , null);
        //imageView.setImageResource(R.drawable.ic_launcher_background);

        //Bitmap bitmap = BitmapFactory.decodeResource(getResources() , R.drawable.transparent_circle);


        imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.transparent_circle);

        int dwidthSpec = View.MeasureSpec.makeMeasureSpec(720 , View.MeasureSpec.AT_MOST);
        int dheightSpec = View.MeasureSpec.makeMeasureSpec(720 , View.MeasureSpec.AT_MOST);

        int cwidthSpec = View.MeasureSpec.makeMeasureSpec(720 , View.MeasureSpec.AT_MOST);
        int cheightSpec = View.MeasureSpec.makeMeasureSpec(720 , View.MeasureSpec.AT_MOST);


        imageView1.measure(dwidthSpec , dheightSpec);
        imageView.measure(cwidthSpec , cheightSpec);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });





        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                , WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                , PixelFormat.TRANSLUCENT);

        dp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                , WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                , PixelFormat.TRANSLUCENT);

        lp.gravity = Gravity.TOP | Gravity.LEFT;
        lp.x = 0;
        lp.y = 100;

        dp.gravity = Gravity.CENTER|Gravity.BOTTOM;


        windowManager.addView(imageView , lp);



        Rect deleteRect = new Rect(dp.x , dp.y , dp.x+imageView1.getMeasuredWidth() , dp.y+imageView1.getMeasuredHeight());
        //Rect chatHeadRect = new Rect(lp.x , lp.y , lp.x+imageView.getWidth() , lp.y+imageView.getHeight());




        imageView.setOnTouchListener((v,event)->{

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    isClicked = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            isClicked = false;


                        }
                    } , 100);


                    X =(int) lp.x;
                    Y =(int) lp.y;

                    initialTouchX =(int) event.getRawX();
                    initialTouchY =(int) event.getRawY();

                    windowManager.addView(imageView1 , dp);

                    return true;

                case MotionEvent.ACTION_MOVE:

                    lp.x = X + ((int) event.getRawX()-initialTouchX);
                    lp.y = Y + ((int) event.getRawY()-initialTouchY);

                    windowManager.updateViewLayout(imageView , lp);

                    checkForDeletion(imageView , imageView1);
                    return true;

                case MotionEvent.ACTION_UP:


                    if (withinBounds) {

                        ObjectAnimator cm = ObjectAnimator.ofFloat(imageView , "translationY" , 0 , 1000);
                        cm.setDuration(500);
                        cm.setAutoCancel(true);

                        ObjectAnimator dm = ObjectAnimator.ofFloat(imageView1 , "translationY" , 0 , 1000);
                        dm.setDuration(500);
                        dm.setAutoCancel(true);
                        cm.start();
                        dm.start();


//                            imageView1.setTranslationY(500);

                        // windowManager.removeView(imageView);
                        // windowManager.removeView(imageView1);
                        stopSelf();
                    } else {

                        if (isClicked) {

                            chatHeadListener.onChatHeadClicked();


                        }

                        windowManager.removeView(imageView1);
                    }



            }

            return false;
        });
    }

    private void checkForDeletion(View chatView, ImageView deleteView) {



        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) chatView.getLayoutParams();
        int[] deleteAreaPosition = new int[2];
        deleteView.getLocationOnScreen(deleteAreaPosition);
        Rect deleteRect = new Rect(deleteAreaPosition[0], deleteAreaPosition[1],
                deleteAreaPosition[0] + deleteView.getMeasuredWidth(), deleteAreaPosition[1] + deleteView.getMeasuredHeight());


        Rect chatHeadRect = new Rect(lp.x, lp.y, lp.x + chatView.getMeasuredWidth(), lp.y + chatView.getMeasuredHeight());

        if (Rect.intersects(chatHeadRect, deleteRect)) {

            dp.width = imageView.getMeasuredWidth() ;
            dp.height = imageView.getMeasuredHeight();
            windowManager.updateViewLayout(deleteView , dp);
            withinBounds = true;
            //Log.d(com.animation.chathead.ChatHeadService.class.getName(), "onCreate: Deleted");
            //stopSelf(); // Stop the service to remove the chat head
        } else {

            dp.width = 96;
            dp.height = 96;
            windowManager.updateViewLayout(deleteView , dp);
            withinBounds = false;




        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return START_STICKY;
    }


}