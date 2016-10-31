package com.nathansass.charc.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.nathansass.charc.R;
import com.nathansass.charc.databinding.FragmentAnimationBinding;

/**
 * Created by nathansass on 10/30/16.
 */

public class AnimationFragment extends Fragment {
    FragmentAnimationBinding binding;

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
    }

    private void setUpUI() {
        binding.btnAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkUpScreen();
            }
        });

    }

    private void antWobble() {
        int speed = 200;
        int rotation = 8;
        ObjectAnimator wobbleLeft =
                ObjectAnimator.ofFloat(binding.ivAnt, View.ROTATION, -rotation);
        wobbleLeft.setDuration(speed);
        wobbleLeft.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator wobbleRight =
                ObjectAnimator.ofFloat(binding.ivAnt, View.ROTATION, rotation);
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

    private void antMoveSideToSide() {
        int speed = 300;
        int dist = 5;
        ObjectAnimator leftShuffle = ObjectAnimator.ofFloat(binding.ivAnt, View.TRANSLATION_X, -dist);
        leftShuffle.setDuration(speed);
        leftShuffle.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator rightShuffle = ObjectAnimator.ofFloat(binding.ivAnt, View.TRANSLATION_X, dist);
        rightShuffle.setDuration(speed);
        rightShuffle.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(leftShuffle, rightShuffle);
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

    private void walkUpScreen() {
        int time = 3000;

        ObjectAnimator moveSide =
                ObjectAnimator.ofFloat(binding.ivAnt, View.TRANSLATION_X, 0); //Change to 400 later
        moveSide.setDuration(time);

        ObjectAnimator moveAhead =
                ObjectAnimator.ofFloat(binding.ivAnt, View.TRANSLATION_Y, -getScreenHeight() + 400);
        moveAhead.setDuration(time);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                antWobble();
                antMoveSideToSide();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.cancel(); //stops the ant for wobbling
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.playTogether(moveSide, moveAhead);
        animatorSet.start();

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

    private int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return  height;
    }
}
