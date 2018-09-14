package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.Application;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.ExerciseDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.ExerciseDayDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.RecordDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.ReminderDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.UserDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.WeightDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Exercise;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.ExerciseDay;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Record;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Reminder;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.User;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Weight;


@Database(entities = {ExerciseDay.class , Exercise.class ,User.class, Record.class, Reminder.class, Weight.class}, version = 3 ,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase appDataBase;

    public abstract ExerciseDao exerciseDao();

    public abstract ExerciseDayDao exerciseDayDao();

    public  abstract UserDao userdao();

    public  abstract RecordDao recorddao();

    public abstract ReminderDao reminderDao();

    public abstract WeightDao weightdao();

    public static AppDataBase getInstance() {
        if (appDataBase == null) {
            Context context = Application.getContext();
            appDataBase = Room.databaseBuilder(context, AppDataBase.class, context.getString(R.string.database))
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDataBase;
    }
}
