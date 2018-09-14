package com.bwf.hiit.workouts.butt.exercises.home.fitness.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bwf.hiit.workouts.butt.exercises.home.fitness.Application;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.R;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.dao.ExerciseDao;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.dao.ExerciseDayDao;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.dao.RecordDao;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.dao.ReminderDao;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.dao.UserDao;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.dao.WeightDao;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.Exercise;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.Record;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.Reminder;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.User;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.Weight;


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
