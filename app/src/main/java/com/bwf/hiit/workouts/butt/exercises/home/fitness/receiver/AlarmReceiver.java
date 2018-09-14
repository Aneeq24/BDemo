package com.bwf.hiit.workouts.butt.exercises.home.fitness.receiver;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.bwf.hiit.workouts.butt.exercises.home.fitness.Application;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.R;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.database.AppDataBase;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.managers.NotificationManager;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.User;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.repository.UserRepo;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.repository.WeightRepo;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.viewModel.UserViewModel;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPrefHelper.readBoolean(context, context.getString(R.string.alarm))) {
            String title = context.getString(R.string.app_name);
            String text = context.getString(R.string.notification_text);
            NotificationManager.getInstance().generateNotification(context, title, text);
        }
    }
}