package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao.ExerciseDayDao;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.database.AppDataBase;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.ExerciseDay;

import java.util.List;

public class ExerciseDayRepo {

    private ExerciseDayDao exerciseDayDao;

    public ExerciseDayRepo() {
        AppDataBase db = AppDataBase.getInstance();
        exerciseDayDao = db.exerciseDayDao();
    }

    public LiveData<List<ExerciseDay>> getLiveExerciseDays(int planId, int dayId){
        return exerciseDayDao.getLiveExerciseDays(planId,dayId);
    }

    public void insert(ExerciseDay  exerciseDay) {
        new insertAsyncTask(exerciseDayDao).execute(exerciseDay);
    }

    private static class insertAsyncTask extends AsyncTask<ExerciseDay, Void, Void> {

        private ExerciseDayDao mAsyncTaskDao;

        insertAsyncTask(ExerciseDayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ExerciseDay... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}