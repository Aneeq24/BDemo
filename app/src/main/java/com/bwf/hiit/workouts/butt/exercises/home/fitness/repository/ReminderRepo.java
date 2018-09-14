package com.bwf.hiit.workouts.butt.exercises.home.fitness.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.hiit.workouts.butt.exercises.home.fitness.dao.ReminderDao;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.database.AppDataBase;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.Reminder;

import java.util.List;

public class ReminderRepo {

    private ReminderDao reminderDao;

    public ReminderRepo() {
        AppDataBase db = AppDataBase.getInstance();
        reminderDao = db.reminderDao();
    }

    public LiveData<Reminder> getReminder(){
        return reminderDao.findById(1);
    }

    public void insert (Reminder reminder) {
        new insertAsyncTask(reminderDao).execute(reminder);
    }

    private static class insertAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao mAsyncTaskDao;

        insertAsyncTask(ReminderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    public void update (Reminder reminder) {
        new updateAsyncTask (reminderDao).execute(reminder);
    }

    private static class updateAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao mAsyncTaskDao;

        updateAsyncTask (ReminderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Reminder... params) {
            mAsyncTaskDao.updateReminder(params[0]);
            return null;
        }
    }
    
}