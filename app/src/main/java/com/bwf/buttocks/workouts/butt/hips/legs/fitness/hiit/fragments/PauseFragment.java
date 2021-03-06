package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AdsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.view.PlayingExercise;
import com.google.android.gms.ads.AdView;


public class PauseFragment extends Fragment {

    View rootView;
    TextView tvExName;
    TextView tvExercise;
    ImageView btnResume;
    ImageView imgAnimate;
    PlayingExercise playingExercise;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pause, container, false);

        AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        findReferences();
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void findReferences() {
        playingExercise = (PlayingExercise) getActivity();
        btnResume = rootView.findViewById(R.id.pauseResume);
        tvExName = rootView.findViewById(R.id.tv_pauseHeadline);
        imgAnimate = rootView.findViewById(R.id.pf_exerciseImage);
        tvExercise = rootView.findViewById(R.id.pf_exerciseText);

        tvExName.setText(playingExercise.nextExerciseName);

        String str = playingExercise.nextExerciseImage;
        int id = getResources().getIdentifier(str, "drawable", rootView.getContext().getPackageName());
        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + id;

        Glide.with(this).load(path).into(imgAnimate);
        btnResume.setOnClickListener(view -> onResumeExercise());

        if (playingExercise.currentEx <= (playingExercise.totalExercises - 1))
            tvExercise.setText("Exercise " + (playingExercise.currentEx + 1) + " of " + playingExercise.totalExercisePerRound);
        else
            tvExercise.setText("Exercise " + playingExercise.totalExercisePerRound + " of " + playingExercise.totalExercisePerRound);
    }

    private void onResumeExercise() {
        playingExercise.onResumeFragment();
    }

}
