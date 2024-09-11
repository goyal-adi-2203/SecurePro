package com.example.securepro.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.securepro.data.database.AppRoomDatabase;
import com.example.securepro.data.local.DeviceDao;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.repository.DeviceRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DeviceRepositoryImpl implements DeviceRepository {

    private final DeviceDao deviceDao;
    private final LiveData<List<Device>> liveData;
    private ExecutorService executorService;

    public DeviceRepositoryImpl(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getInstance(application);
        this.deviceDao = db.deviceDao();
        liveData = deviceDao.getAllDevicesLive();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void insertDevice(Device device) {
        executorService.execute(() -> deviceDao.insert(device));
//        deviceDao.insert(device);
    }

    @Override
    public void updateDevice(Device device) {
        executorService.execute(() -> deviceDao.update(device));
//        deviceDao.update(device);
    }

    @Override
    public void deleteDevice(Device device) {
        executorService.execute(() -> deviceDao.delete(device));
//        deviceDao.delete(device);
    }

    @Override
    public void deleteAll() {
        executorService.execute(deviceDao::deleteAll);
//        deviceDao.deleteAll();
    }

    @Override
    public Device getDevice(int id) {
        Future<Device> future = executorService.submit(() -> deviceDao.getDevice(id));
        try{
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Device> getAllDevices() {
        Future<List<Device>> future = executorService.submit(deviceDao::getAllDevice);
        try{
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LiveData<List<Device>> getAllDevicesLive() {
        return liveData;
    }
}
