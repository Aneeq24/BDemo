package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.WeightDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.database.AppDataBase;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Weight;

import java.util.List;

public class WeightRepo {

    private WeightDao weightdao;

    public WeightRepo() {
        AppDataBase db = AppDataBase.getInstance();
        weightdao = db.weightdao();
    }

    public LiveData<List<Weight>> getAllWeights() {
        return weightdao.getAllWeight();
    }

    public void insert(Weight weight) {
        new insertAsyncTask(weightdao).execute(weight);
    }

    private static class insertAsyncTask extends AsyncTask<Weight, Void, Void> {

        private WeightDao mAsyncTaskDao;

        insertAsyncTask(WeightDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Weight... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

}