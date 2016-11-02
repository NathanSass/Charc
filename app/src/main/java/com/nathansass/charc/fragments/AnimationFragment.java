package com.nathansass.charc.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.nathansass.charc.R;
import com.nathansass.charc.databinding.FragmentAnimationBinding;

import java.util.ArrayList;
import java.util.Random;


public class AnimationFragment extends Fragment {
    FragmentAnimationBinding binding;
    float x,y;

    ArrayList<View> antList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_animation, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpUI();
        antList = new ArrayList<>();
    }

    private void setUpUI() {

        animateBread();
        removeOffScreenAnts();

        binding.rlAnimation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View antImage = addAnNewAntImage(x, y);
                antList.add(antImage);
                walkUpScreen(antImage);
                return false;
            }
        });

        binding.rlAnimation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent ev) {
                if(ev.getAction() == MotionEvent.ACTION_DOWN){
                    x = ev.getX();
                    y = ev.getY();
                }
                return false;
            }
        });

        binding.ivBread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void animateBread() {
        int dist = getScreenWidth();
        int speed = 3000;
        View view = binding.ivBread;

        ObjectAnimator leftShuffle = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0);
        leftShuffle.setDuration(speed);
        leftShuffle.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator rightShuffle = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, dist - 225);
        rightShuffle.setDuration(speed);
        rightShuffle.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(rightShuffle, leftShuffle);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private int getYValue(View view){
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int x = locations[0];
        int y = locations[1];

        return y;
    }

    private void removeOffScreenAnts() {
        final android.os.Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{
                    for(View antView : antList) {
                        int antY = getYValue(antView);
                        if(antY < 0) { //BUGBUG: Not the best
                            antList.remove(antView);
                            binding.rlAnimation.removeView(antView);
                        }

                        int viewY = getYValue(binding.rlAnimation);



                        Log.v("DEBUG", "antY: " + antY + "    viewY: " + viewY);
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally{
                    handler.postDelayed(this, 500);
                }
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    private void antWobble(View view) {
        int speed = getRandom(120, 225);
        int rotation = getRandom(5,9);
        ObjectAnimator wobbleLeft =
                ObjectAnimator.ofFloat(view, View.ROTATION, -rotation);
        wobbleLeft.setDuration(speed);
        wobbleLeft.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator wobbleRight =
                ObjectAnimator.ofFloat(view, View.ROTATION, rotation);
        wobbleRight.setDuration(speed);
        wobbleRight.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(wobbleLeft, wobbleRight);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private boolean isViewOverlapping(View firstView, View secondView) {

        final int[] location = new int[2];

        firstView.getLocationInWindow(location);
        Rect rect1 = new Rect(location[0], location[1],location[0] + firstView.getWidth(), location[1] + firstView.getHeight());

        secondView.getLocationInWindow(location);
        Rect rect2 = new Rect(location[0], location[1],location[0] + secondView.getWidth(), location[1] + secondView.getHeight());

        return rect1.intersect(rect2);
    }

    private void walkUpScreen(final View view) {
        int time = getRandom(7000, 20000);

        ObjectAnimator moveAhead =
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -getScreenHeight() + 400);
        moveAhead.setDuration(time);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                antWobble(view);
//                antMoveSideToSide(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.removeAllListeners();
                animation.cancel(); //stops the ant for wobbling
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
//        animatorSet.playTogether(moveSide, moveAhead);
        animatorSet.playTogether(moveAhead);
        animatorSet.start();
    }

    private View addAnNewAntImage(float x, float y) {
        int antSize = getRandom(60, 120);
        ImageView antImageView = new ImageView(getContext());
        antImageView.setVisibility(View.VISIBLE);
        int antImage = getResources().getIdentifier("@drawable/ant", "drawable", getActivity().getPackageName());
        antImageView.setImageResource(antImage);

        binding.rlAnimation.addView(antImageView);
        antImageView.getLayoutParams().width = antSize;
        antImageView.getLayoutParams().height = antSize;

        antImageView.setY(y);
        antImageView.setX(x);

        return antImageView;
    }


    private int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return  height;
    }

    private int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        return width;
    }

    private int getRandom(int min, int max) {
        Random rn = new Random();
        return rn.nextInt(max - min + 1) + min;
    }

    private void animateAnt(ImageView imageView) {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 400.0f,
                0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(5);  // animation repeat count
        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
        //animation.setFillAfter(true);

        imageView.startAnimation(animation);  // start animation
    }
}
