package com.bwf.hiit.workouts.butt.exercises.home.fitness.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bwf.hiit.workouts.butt.exercises.home.fitness.R;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.managers.AlarmManager;

import java.util.Calendar;


public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPrefHelper.readBoolean(context, context.getString(R.string.alarm)))
            AlarmManager.getInstance().setAlarm(context,  Calendar.getInstance());
    }
}
