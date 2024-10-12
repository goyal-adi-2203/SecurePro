package com.example.securepro.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "device_table")
public class Device {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String password;
    private String deviceType;

    public Device(String id, String name, String password, String deviceType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.deviceType = deviceType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
