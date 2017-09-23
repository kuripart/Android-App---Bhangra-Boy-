package com.example.kuri.workingwithsprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    Bitmap spriteSheet;
    Sprite sprite;
    FrameLayout frame;
    int frameNumber = 0;
    int frameNumberUpdate;
    boolean jump = false;
    GestureDetector gestures;
    int heightScreen;
    int widthScreen;
    Matrix obstacle;
    boolean moveAhead = true;
    TextView text;
    Bitmap apple;
    int x;
    int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spriteSheet = BitmapFactory.decodeResource(getResources(),R.drawable.imageedited);
        apple = BitmapFactory.decodeResource(getResources(),R.drawable.apple);
        sprite = new Sprite(this,spriteSheet);
        gestures = new GestureDetector(MainActivity.this); //GestureDetector deprecated? -> find why??
        //screen dimensions
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        heightScreen = displaymetrics.heightPixels;
        widthScreen = displaymetrics.widthPixels;
        runImage(sprite);
        randomiseObstacle(sprite);
        frame = (FrameLayout)findViewById(R.id.frame);
        frame.addView(sprite);
        text = (TextView)findViewById(R.id.textIt);
        obstacle = new Matrix();
        a = 25;

    }

    public class Sprite extends View{

        int height;
        int width;
        int appleHeight;
        int appleWidth;
        int y = 500;
        int srcX;
        Bitmap bmp;
        Rect src;
        Rect des;

        public Sprite(Context context, Bitmap bmp) {
            super(context);
            this.bmp = bmp;
            height = bmp.getHeight()/2;
            width = bmp.getWidth()/2;
        }
        public void update(){

            frameNumber++;
            //modulo 6 will give number between 0 to 5
            frameNumberUpdate = frameNumber%2;
            //srcX = initial x coordinate (from) and srcX + width = final  x coordinate (to)
            //that is, draw the image from this initial x to that final x, keeping the y constant(i.e; 0)
            srcX = frameNumberUpdate*width;

            if(x >= (widthScreen-width*2)) //screen width - total image width
                a = -100;
            if(x < (width)) //width of a single sprite
                a = 100;

            x = x + a;

            if(jump == false){
                text.setVisibility(INVISIBLE);
                src= new Rect(srcX,height,width+srcX,2*height);
                des = new Rect(x,y,width+x,height+y);
            }else if(jump == true) {
                src = new Rect(srcX, 0, width + srcX, height);
                des = new Rect(x, y - 400, width + x, height + (y - 400));
                text.setVisibility(VISIBLE);
                text.setText("BALLE BALLE! I AM FUCKED!");
                jump = false;

            }

            //translates the entire rectangle for this app by scaling
            //scales image from x to width and y to height, thus for des = new Rect(x,y,width,height);
            //as x increases an the width stays the same, the image will become thinner. So add x to width
            //for proportion

        }

        public void move(){
            //
        }
        @Override
        public void onDraw(Canvas canvas){
            //Draw the specified bitmap, scaling/translating automatically to fill the destination rectangle.
            canvas.drawBitmap(bmp,src,des,null);
            invalidate();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestures.onTouchEvent(event);
    }



    public void runImage(final Sprite sprite){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                sprite.update();
                handler.postDelayed(this,500);
            }
        });
    }

    public void randomiseObstacle(final Sprite sprite){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                sprite.move();
            }
        });
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        jump = true; //jump on double tap
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        jump = true;
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


}