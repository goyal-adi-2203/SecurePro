package com.example.securepro.domain.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "device_table")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Device {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String password;
    private String deviceType;
}
