package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Weight;

import java.util.List;

@Dao
public interface WeightDao {

    @Query("SELECT * FROM weight")
    LiveData<List<Weight>> getAllWeight();

    @Query("SELECT * FROM weight WHERE day = :id")
    LiveData<Weight> findById(int id);

    @Query("SELECT * FROM weight WHERE day = :id")
    Weight findByIdbg(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Weight... weights);

    @Delete
    void delete(Weight record);
}
