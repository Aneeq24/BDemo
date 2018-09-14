package com.bwf.hiit.workouts.butt.exercises.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workouts.butt.exercises.home.fitness.models.User;
import com.bwf.hiit.workouts.butt.exercises.home.fitness.repository.UserRepo;


public class UserViewModel extends AndroidViewModel {

    private UserRepo mRepository;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepo();
    }

    public LiveData<User> getUser() {
        return mRepository.getUser();
    }

    public void update(User user) {
        mRepository.update(user);
    }

}