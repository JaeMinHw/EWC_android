package com.example.ewc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class User_controller extends AppCompatActivity {

    send_control sc = new send_control();

    private ImageView view1,view2,view3,view4,view5;

    private AnimatorSet animatorSet;
    private Button controlBTN;
    private boolean controlFlag = true;
    private Activity activity = this;

    // go
    private ValueAnimator colorAnimationOuter1;

    // stop
    private ValueAnimator colorAnimationOuter2;

    // back
    private ValueAnimator colorAnimationOuter3;

    // right
    private ValueAnimator colorAnimationOuter4;

    // left
    private ValueAnimator colorAnimationOuter5;

    final int go = 500; // 한번 바뀌는데 걸리는 시간 즉 흰색에서 파란색으로 될 때까지의 시간이 500ms
    final int back = 600;
    final int left = 400;
    final int right = 300;
    final int stop = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_controller);

        view1 = (ImageView) findViewById(R.id.view1);
        view2 = (ImageView) findViewById(R.id.view2);
        view3 = (ImageView) findViewById(R.id.view3);
        view4 = (ImageView) findViewById(R.id.view4);
        view5 = (ImageView) findViewById(R.id.view5);

        Integer colorFrom = Color.BLACK;
        Integer colorTo = Color.WHITE;


        colorAnimationOuter1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
//                view5.setBackgroundColor((Integer) animator.getAnimatedValue());
//                view4.setBackgroundColor((Integer) animator.getAnimatedValue());
//                view3.setBackgroundColor((Integer) animator.getAnimatedValue());
                view1.setBackgroundColor((Integer) animator.getAnimatedValue());
//                Log.e("tttttt",""+Integer.parseInt(animator.getAnimatedValue().toString())) ;
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view1.setColorFilter(R.color.black);
//                    Log.e("color","White   " + animator.getAnimatedValue());
                }
                else {
                    view1.setColorFilter(R.color.white);
//                    Log.e("color","Black    " + animator.getAnimatedValue());
                }

            }

        });

        colorAnimationOuter2 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view2.setBackgroundColor((Integer) animator.getAnimatedValue());
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view2.setColorFilter(R.color.black);
                }
                else {
                    view2.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter3 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view3.setBackgroundColor((Integer) animator.getAnimatedValue());
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view3.setColorFilter(R.color.black);
                }
                else {
                    view3.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter4 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view4.setBackgroundColor((Integer) animator.getAnimatedValue());
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view4.setColorFilter(R.color.black);
                }
                else {
                    view4.setColorFilter(R.color.white);
                }

            }

        });

        colorAnimationOuter5 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimationOuter5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view5.setBackgroundColor((Integer) animator.getAnimatedValue());
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view5.setColorFilter(R.color.black);
                }
                else {
                    view5.setColorFilter(R.color.white);
                }

            }

        });
        colorAnimationOuter1.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter1.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter1.setDuration(go);

        colorAnimationOuter2.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter2.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter2.setDuration(stop);

        colorAnimationOuter3.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter3.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter3.setDuration(back);

        colorAnimationOuter4.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter4.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter4.setDuration(right);

        colorAnimationOuter5.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter5.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter5.setDuration(left);

        // @TODO animation set
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(colorAnimationOuter1, colorAnimationOuter2,colorAnimationOuter3,colorAnimationOuter4,colorAnimationOuter5
                );

        animatorSet.start();
    }

    public void go(View view) {
        new Thread(){public void run() {
            sc.move("go");
        }}.start();
    }
    public void back(View view) {
        new Thread(){public void run() {
            sc.move("back");
        }}.start();
    }
    public void right(View view) {
        new Thread(){public void run() {
            sc.move("right");
        }}.start();
    }
    public void left(View view) {
        new Thread(){public void run() {
            sc.move("left");
        }}.start();
    }
    public void stop(View view) {
        new Thread(){public void run() {
            sc.move("stop");
        }}.start();
    }


}