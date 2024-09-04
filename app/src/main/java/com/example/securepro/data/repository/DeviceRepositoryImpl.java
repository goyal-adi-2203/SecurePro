package com.example.securepro.data.repository;

import com.example.securepro.data.local.DeviceDao;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.repository.DeviceRepository;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
public class DeviceRepositoryImpl implements DeviceRepository {

    private final DeviceDao deviceDao;

    public DeviceRepositoryImpl(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    @Override
    public void insertDevice(Device device) {
        deviceDao.insert(device);
    }

    @Override
    public void updateDevice(Device device) {
        deviceDao.update(device);
    }

    @Override
    public void deleteDevice(Device device) {
        deviceDao.delete(device);
    }

    @Override
    public void deleteAll() {
        deviceDao.deleteAll();
    }

    @Override
    public Device getDevice(int id) {
        return deviceDao.getDevice(id);
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceDao.getAllDevice();
    }
}
