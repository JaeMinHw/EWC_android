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

    private ImageView view1,view2,view3,view4,view5;

    private AnimatorSet animatorSet;
    private Button controlBTN;
    private boolean controlFlag = true;
    private Activity activity = this;

    // first
    private ValueAnimator colorAnimationOuter1;
    private ValueAnimator colorAnimationInner1;

    final int go = 500; // 한번 바뀌는데 걸리는 시간 즉 흰색에서 파란색으로 될 때까지의 시간이 500ms
    final int back = 500;
//    final int left;
//    final int right;
//    final int stop;

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
                Log.e("tttttt",""+Integer.parseInt(animator.getAnimatedValue().toString())) ;
                if(Integer.parseInt(animator.getAnimatedValue().toString()) == -1){
                    view1.setColorFilter(R.color.black);
                    Log.e("color","White   " + animator.getAnimatedValue());
                }
                else {
                    view1.setColorFilter(R.color.white);
                    Log.e("color","Black    " + animator.getAnimatedValue());
                }

            }

        });
        colorAnimationInner1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        colorAnimationInner1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view3.setBackgroundColor((Integer) animator.getAnimatedValue());

            }

        });
        colorAnimationOuter1.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationOuter1.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationOuter1.setDuration(go);
        colorAnimationInner1.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimationInner1.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimationInner1.setDuration(back);

        // @TODO animation set
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(colorAnimationOuter1, colorAnimationInner1
                );

        animatorSet.start();
    }
}