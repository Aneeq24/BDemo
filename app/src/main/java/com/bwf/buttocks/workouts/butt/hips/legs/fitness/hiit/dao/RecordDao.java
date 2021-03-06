package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Record;

import java.util.List;


@Dao
public interface RecordDao {

    @Query("SELECT * FROM record")
    LiveData<List<Record>> getAllRecords();

    @Query("SELECT * FROM record WHERE day = :id")
    LiveData<Record> findById(int id);

    @Query("SELECT * FROM record WHERE day = :id")
    Record findByIdbg(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Record... record);

    @Delete
    void delete(Record record);

}
