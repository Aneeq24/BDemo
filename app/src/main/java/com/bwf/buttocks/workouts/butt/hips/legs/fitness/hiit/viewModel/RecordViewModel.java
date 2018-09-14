package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Record;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.repository.RecordRepo;

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
