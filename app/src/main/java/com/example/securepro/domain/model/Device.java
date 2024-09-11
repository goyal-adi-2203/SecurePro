package com.example.securepro.domain.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "device_table")
public class Device {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String password;
    private String deviceType;

    public Device(String name, String password, String deviceType) {
        this.name = name;
        this.password = password;
        this.deviceType = deviceType;
    }

    public int getId() {
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

    public void setId(int id) {
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
}
