package com.example.securepro.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.securepro.domain.model.Device;
import java.util.List;

public interface DeviceRepository {
    public void insertDevice(Device device);
    public void updateDevice(Device device);
    public void deleteDevice(Device device);
    public void deleteAll();
    public Device getDevice(int id);
    public List<Device> getAllDevices();
    public LiveData<List<Device>> getAllDevicesLive();
}

