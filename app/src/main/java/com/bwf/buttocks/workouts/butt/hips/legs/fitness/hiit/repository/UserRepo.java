package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.UserDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.database.AppDataBase;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.User;

public class UserRepo {

    private UserDao userdao;

    public UserRepo() {
        AppDataBase db = AppDataBase.getInstance();
        userdao = db.userdao();
    }

    public LiveData<User> getUser() {
        return userdao.findById(1);
    }

    public void update (User user) {
        new updateAsyncTask(userdao).execute(user);
    }

    private static class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        updateAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.updateUser(params[0]);
            return null;
        }
    }
}