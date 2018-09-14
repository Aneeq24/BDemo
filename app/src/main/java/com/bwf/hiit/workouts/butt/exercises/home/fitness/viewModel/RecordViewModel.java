package com.bwf.hiit.workouts.butt.exercises.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.Record;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.repository.RecordRepo;

import java.util.List;

public class RecordViewModel extends AndroidViewModel {

    private RecordRepo mRepository;
    private LiveData<List<Record>> mAllRecords;

    public RecordViewModel(Application application) {
        super(application);
        mRepository = new RecordRepo();
        mAllRecords = mRepository.getAllRecords();
    }

    public LiveData<List<Record>> getAllRecords() {
        return mAllRecords;
    }

    public void insert(Record record) {
        mRepository.insert(record);
    }

}
