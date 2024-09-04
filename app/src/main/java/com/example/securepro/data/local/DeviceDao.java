package com.example.securepro.data.local;

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
    public void insert(Device device);

    @Update
    public void update(Device device);

    @Delete
    public void delete(Device device);

    @Query("DELETE from device_table")
    public void deleteAll();

    @Query("SELECT * FROM device_table WHERE id=:id")
    public Device getDevice(int id);

    @Query("SELECT * FROM device_table")
    public List<Device> getAllDevice();
}
