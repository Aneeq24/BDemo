package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.database.AppDataBase;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.helpers.SharedPrefHelper;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AnalyticsManager;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Day;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Exercise;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.ExerciseDay;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.PlanDays;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Reminder;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.User;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class SplashScreeActivity extends AppCompatActivity {

    private final String TAG = SplashScreeActivity.class.getSimpleName();
    boolean backPresed;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        AnalyticsManager.getInstance().sendAnalytics("splash_screen_started", "activity_started");
//        Stetho.initializeWithDefaults(this);
        new AppDbCheckingTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class AppDbCheckingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Gson gson = new Gson();
            AppDataBase appDataBase = AppDataBase.getInstance();

            if (appDataBase != null) {
                if (appDataBase.userdao().getUser() == 0) {
                    User user = new User();
                    Reminder reminder = new Reminder();
                    reminder.setFriday(true);
                    reminder.setSatday(true);
                    reminder.setSunday(true);
                    reminder.setMonday(true);
                    reminder.setTuesday(true);
                    reminder.setWednesday(true);
                    reminder.setThursday(true);
                    appDataBase.userdao().insertAll(user);
                    appDataBase.reminderDao().insertAll(reminder);
                    // insert exercises
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "exercise.json");
                        List<Exercise> exercises = gson.fromJson(json, new TypeToken<List<Exercise>>() {
                        }.getType());
                        if (exercises != null && exercises.size() > 0)
                            appDataBase.exerciseDao().insertAll(exercises);
                    } catch (Exception e) {
                        Log.e(TAG, "exercises: " + e.getLocalizedMessage());
                    }
                    // insert exercises days
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "days.json");
                        List<PlanDays> planDays = gson.fromJson(json, new TypeToken<List<PlanDays>>() {
                        }.getType());
                        if (planDays != null && planDays.size() > 0) {
                            for (PlanDays days : planDays) {
                                for (Day day : days.getDays()) {
                                    List<ExerciseDay> mList = new ArrayList<>();
                                    for (ExerciseDay exerciseDay : day.getExercisesOfDay()) {
                                        exerciseDay.setPlanId(days.getPlanId());
                                        exerciseDay.setDayId(day.getDayId());
                                        exerciseDay.setRoundCompleted(day.getRoundCompleted());
                                        exerciseDay.setTotalExercise(day.getTotalExercise());
                                        exerciseDay.setTotalExercise(day.getExercisesOfDay().size() * exerciseDay.getRounds());
                                        exerciseDay.setTotalKcal(0);
                                        mList.add(exerciseDay);
                                    }
                                    appDataBase.exerciseDayDao().insertAll(mList);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "days: " + e.getLocalizedMessage());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled())
                return;
            new Handler().postDelayed(() -> {
                if (backPresed)
                    return;
                if (!SharedPrefHelper.readBoolean(context, getString(R.string.is_first_run))) {
                    setDefaultPreferences();
                } else {
                    startActivity(new Intent(context, HomeActivity.class));
                    finish();
                }
            }, 5000);
        }
    }

    private void setDefaultPreferences() {
        SharedPrefHelper.writeInteger(context, getString(R.string.hour), 19);
        SharedPrefHelper.writeInteger(context, getString(R.string.minute), 0);
        SharedPrefHelper.writeBoolean(context, "rate", false);
        SharedPrefHelper.writeInteger(context, "sound", 0);
        SharedPrefHelper.writeBoolean(context, "reminder", true);
        SharedPrefHelper.writeBoolean(context, getString(R.string.alarm), true);
        SharedPrefHelper.writeBoolean(context, getString(R.string.is_first_run), true);
        startActivity(new Intent(context, SelectGender.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPresed = true;
    }

}
