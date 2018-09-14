package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Record;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    int getUser();

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> findById(int id);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Update
    void updateUser(User user);

}
