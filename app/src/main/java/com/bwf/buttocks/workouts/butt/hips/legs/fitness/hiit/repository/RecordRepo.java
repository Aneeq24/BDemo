package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.RecordDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.database.AppDataBase;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Record;

import java.util.List;

public class RecordRepo {

    private RecordDao recorddao;

    public RecordRepo() {
        AppDataBase db = AppDataBase.getInstance();
        recorddao = db.recorddao();
    }

    public LiveData<List<Record>> getAllRecords() {
        return recorddao.getAllRecords();
    }

    public void insert(Record record) {
        new insertAsyncTask(recorddao).execute(record);
    }

    private static class insertAsyncTask extends AsyncTask<Record, Void, Void> {

        private RecordDao mAsyncTaskDao;

        insertAsyncTask(RecordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

}