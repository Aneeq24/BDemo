package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.managers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.receiver.AlarmReceiver;
import java.util.Calendar;


public class AlarmManager {

    private static AlarmManager manager;

    public static AlarmManager getInstance() {
        if (manager == null) {
            manager = new AlarmManager();
        }
        return manager;
    }

    public void cancelAlarm(Context context) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }

    public void setAlarm(Context context, Calendar alarmTime) {

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            Calendar currentCalendar = Calendar.getInstance();
            if (currentCalendar.getTimeInMillis() > alarmTime.getTimeInMillis()) {
                alarmTime.add(Calendar.DAY_OF_MONTH, 1);
                alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                alarmManager.setRepeating(android.app.AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

}
