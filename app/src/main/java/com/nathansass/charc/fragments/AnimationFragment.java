package com.nathansass.charc.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                animateAnt(binding.ivAnt);
            }
        });
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
