package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.adapter.DailyExerciseAdapter;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.database.AppDataBase;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AdsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AnalyticsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Exercise;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.ExerciseDay;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.utils.Utils;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyExerciseInfo extends AppCompatActivity {

    Context context;
    DailyExerciseAdapter mAdapter;
    List<Exercise> mEXList;
    int plan;
    int planDay;
    int completeRounds = 0;
    int completeExercise = 0;
    int totaTimeSpend = 0;
    float kcal = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_scroll)
    RecyclerView rvDayExercise;
    @BindView(R.id.tv_exercise)
    TextView tvExercise;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_kcal)
    TextView tvKcal;
    @BindView(R.id.tv_Title)
    TextView tvTitle;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_exercise_info);
        ButterKnife.bind(this);

        context = this;
        mEXList = new ArrayList<>();
        Intent intent = getIntent();
        plan = intent.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        planDay = intent.getIntExtra(getApplicationContext().getString(R.string.day_selected), 0);
        tvTitle.setText("DAY " + planDay);

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        AdsManager.getInstance().showFacebookInterstitial(getString(R.string.FB_Int_Exercise_List),true);

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_list_activity");

        rvDayExercise.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DailyExerciseAdapter(this);
        rvDayExercise.setNestedScrollingEnabled(false);
        rvDayExercise.setAdapter(mAdapter);
        mAdapter.setDayPlan(planDay, plan);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new getExerciseTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class getExerciseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected final Void doInBackground(Void... params) {

            List<ExerciseDay> mList = AppDataBase.getInstance().exerciseDayDao().getExerciseDays(plan, planDay);
            if (mList.size() > 0) {
                completeExercise = mList.get(0).getExerciseComplete();
                completeRounds = mList.get(0).getRoundCompleted();
                for (ExerciseDay day : mList) {
                    if (day.isStatus())
                        day.setStatus(false);
                    totaTimeSpend = totaTimeSpend + day.getReps();
                    Exercise exercise = AppDataBase.getInstance().exerciseDao().findByIdbg(day.getId());
                    if (exercise != null) {
                        mEXList.add(new Exercise(day.getReps(), day.getDelay(), exercise.getName(), exercise.getDisplay_name()));
                        kcal = kcal + exercise.getCalories();
                    }
                }
                totaTimeSpend = totaTimeSpend * mList.get(0).getRounds();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int totalExercisePerRound = mEXList.size();
            tvExercise.setText(String.valueOf(totalExercisePerRound) + " Exercise");
            int minutes = (totaTimeSpend % 3600) / 60;
            tvTime.setText(String.valueOf(minutes) + " Min");
            tvKcal.setText(String.valueOf((int) kcal) + " Kcal");
            mAdapter.setList(mEXList);
            mAdapter.setData(completeRounds, completeExercise);
        }
    }

    @OnClick(R.id.startButton)
    public void onViewClicked() {
        if (completeRounds > 0 || completeExercise > 0)
            Utils.setCheckBox(context, planDay, plan);
        else Utils.setScreen(context, planDay, plan);
    }
}
