package com.example.securepro.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.securepro.domain.model.Device;

import java.util.List;

@Dao
public interface DeviceDao {
    @Insert
    void insert(Device device);

    @Update
    void update(Device device);

    @Delete
    void delete(Device device);

    @Query("DELETE from device_table")
    void deleteAll();

    @Query("SELECT * FROM device_table WHERE id=:id")
    Device getDevice(int id);

    @Query("SELECT * FROM device_table")
    List<Device> getAllDevice();

    @Query("SELECT * FROM device_table")
    LiveData<List<Device>> getAllDevicesLive() ;
}
