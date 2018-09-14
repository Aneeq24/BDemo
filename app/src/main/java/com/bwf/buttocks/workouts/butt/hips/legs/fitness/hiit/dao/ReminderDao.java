package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Record;
import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.models.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {


    @Query("SELECT * FROM reminder WHERE id = :id")
    LiveData<Reminder> findById(int id);

    @Query("SELECT * FROM reminder WHERE id = :id")
    Reminder findByIdbg(int id);

    @Insert
    void insertAll(Reminder... reminder);

    @Delete
    void delete(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);
}
