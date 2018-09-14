package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Reminder;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.repository.ReminderRepo;

public class ReminderViewModel extends AndroidViewModel {

    private ReminderRepo mRepository;

    public ReminderViewModel(Application application) {
        super(application);
        mRepository = new ReminderRepo();
    }

    public LiveData<Reminder> getReminder() {
        return mRepository.getReminder();
    }

    public void insert(Reminder reminder) {
        mRepository.insert(reminder);
    }

    public void update(Reminder reminder) {
        mRepository.update(reminder);
    }

}