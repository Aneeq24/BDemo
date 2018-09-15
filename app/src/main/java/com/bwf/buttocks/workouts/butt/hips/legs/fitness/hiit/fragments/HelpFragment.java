package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AdsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.view.PlayingExercise;

public class HelpFragment extends Fragment {

    PlayingExercise playingExercise;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_help, container, false);

        playingExercise = (PlayingExercise) getActivity();
        ImageView closeButton  =  rootView.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(view -> closeButtonClick());
        return rootView;
    }

    private void  closeButtonClick() {
        playingExercise.onResumeFragment();
    }

}
