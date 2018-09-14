package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.view;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AdsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AnalyticsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.User;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.viewModel.UserViewModel;
import com.google.android.gms.ads.AdView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectGender extends AppCompatActivity {

    UserViewModel mViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        ButterKnife.bind(this);
        AnalyticsManager.getInstance().sendAnalytics("gender_screen_started", "activity_started");

        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mViewModel.getUser().observe(this,user ->
        this.user = user);

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);
    }


    @OnClick({R.id.rb_male, R.id.rb_female, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_male:
                setGender("MALE", 1);
                break;
            case R.id.rb_female:
                setGender("FEMALE", 0);
                break;
            case R.id.btn_skip:
                AnalyticsManager.getInstance().sendAnalytics("skip_gender", "skip_gender");
                startNewActivity();
                break;
        }
    }

    private void setGender(String gender, int sex) {
        user.setGender(sex);
        AnalyticsManager.getInstance().sendAnalytics("gender_selected", gender);
        mViewModel.update(user);
        startNewActivity();
    }

    private void startNewActivity() {
        startActivity(new Intent(getApplicationContext(), AgeWeightHeightActivity.class));
        finish();
    }
}
