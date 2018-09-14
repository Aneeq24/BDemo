package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.helpers.SharedPrefHelper;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers.AlarmManager;

import java.util.Calendar;


public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPrefHelper.readBoolean(context, context.getString(R.string.alarm)))
            AlarmManager.getInstance().setAlarm(context,  Calendar.getInstance());
    }
}
