package com.example.animation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button perfectBtn,notEnoughBtn,tooMuchBtn;
    TextView desc;
    ImageView raceCar,track,tier,yuanli;
    int clickTime = 0;
    private int time=0;
    ObjectAnimator goStraight;
    ObjectAnimator turningX;
    ObjectAnimator turningStep2;
    AnimatorSet turnningAnimator;
    AnimatorSet backAnimatorSet;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        perfectBtn = findViewById(R.id.normalBtn);
        notEnoughBtn = findViewById(R.id.notEnough);
        tooMuchBtn = findViewById(R.id.tooMuch);
        raceCar = findViewById(R.id.imageView4);
        track = findViewById(R.id.imageView5);
        track.setVisibility(View.VISIBLE);
        desc = findViewById(R.id.description);
        tier = findViewById(R.id.imageView);
        tier.setVisibility(View.INVISIBLE);
        yuanli = findViewById(R.id.imageView2);
        yuanli.setVisibility(View.INVISIBLE);

        final Animation perfectTurning = new RotateAnimation(0,90,1100,488-432);
        perfectTurning.setDuration(2000);

        final Animation notEnough = new RotateAnimation(0,75,1600,488-432);
        notEnough.setDuration(1500);

        goStraight = ObjectAnimator.ofFloat(raceCar,"translationY",-980);
        goStraight.setDuration(1600);

        turningX = ObjectAnimator.ofFloat(raceCar,"translationX",500);
        turningX.setDuration(1200);

        turningStep2 = ObjectAnimator.ofFloat(raceCar,"rotation",0,175);
        turningStep2.setDuration(1300);



        final ObjectAnimator backX = ObjectAnimator.ofFloat(raceCar,"translationX",0);
        final ObjectAnimator backY = ObjectAnimator.ofFloat(raceCar,"translationY",0);
        final ObjectAnimator backRotate = ObjectAnimator.ofFloat(raceCar,"rotation",0,0);
        backAnimatorSet = new AnimatorSet();
        backAnimatorSet.playTogether(backX,backY,backRotate);
        backAnimatorSet.setDuration(0);


        perfectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAnimatorSet.start();
                yuanli.setVisibility(View.INVISIBLE);
                raceCar.startAnimation(perfectTurning);
                desc.setText("正确的入弯时机\n正确的外内外走线\n正确的出弯时机");

            }
        });


        notEnoughBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAnimatorSet.start();
                clickTime++;
                if (clickTime % 2 == 1) {
                    raceCar.setVisibility(View.VISIBLE);
                    yuanli.setVisibility(View.VISIBLE);
                    raceCar.startAnimation(notEnough);
                    desc.setText("车速过快，\n导致前轮胎摩擦力无法提供向心力\n而出现推头现象");
                }

                if (clickTime % 2 == 0) {
                    raceCar.setVisibility(View.INVISIBLE);
                    yuanli.setVisibility(View.VISIBLE);
                    tier.setVisibility(View.VISIBLE);

                    tier.startAnimation(notEnough);
                    tier.setVisibility(View.INVISIBLE);
                    desc.setText("车速过快，\n导致前轮胎摩擦力无法提供向心力\n而出现推头现象");
                }
            }
        });

        tooMuchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAnimatorSet.start();
                time = 0;
                MyTimer timer = new MyTimer();
                timer.start();
                yuanli.setVisibility(View.INVISIBLE);


                desc.setText("入弯过晚->转向过快\n导致后轮胎摩擦力无法提供向心力\n从而出现甩尾现象");
            }
        });
    }

    class MyTimer extends Thread{
        @Override
        public void run() {
            time=0;
                while(true) {
                    time+=10;
                    try {
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {

                    }
                    if (time == 10 ){
                        Message message = handler.obtainMessage();
                        message.what=3;
                        handler.sendMessage(message);
                    }
                    if (time == 700){
                        Message message = handler.obtainMessage();
                        message.what=1;
                        handler.sendMessage(message);
                    }

                    if (time >=2000){
                        break;
                    }
                }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                turningX.start();
                turningStep2.start();
            }
            else if (msg.what==3){
                goStraight.start();
            }

        }
    };
}