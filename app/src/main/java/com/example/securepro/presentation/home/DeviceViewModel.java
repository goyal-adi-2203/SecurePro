package com.example.securepro.presentation.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.securepro.data.database.AppRoomDatabase;
import com.example.securepro.data.local.DeviceDao;
import com.example.securepro.data.repository.DeviceRepositoryImpl;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.repository.DeviceRepository;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {

    private DeviceRepository repository;
    private LiveData<List<Device>> liveData;
    private DeviceDao deviceDao;

    public DeviceViewModel(@NonNull Application application) {
        super(application);

        repository = new DeviceRepositoryImpl(application);
        liveData = repository.getAllDevicesLive();
    }

    public void insert(Device device){
        repository.insertDevice(device);
    }

    public void update(Device device){
        repository.updateDevice(device);
    }

    public void delete(Device device) {
        repository.deleteDevice(device);
    }

    public void deleteAllDevices() {
        repository.deleteAll();
    }

    public LiveData<List<Device>> getAllDevices() {
        return liveData;
    }
}
