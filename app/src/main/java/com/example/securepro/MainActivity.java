package com.example.securepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

import com.example.securepro.data.database.AppRoomDatabase;
import com.example.securepro.data.local.DeviceDao;
import com.example.securepro.data.repository.DeviceRepositoryImpl;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.repository.DeviceRepository;
import com.example.securepro.presentation.home.DeviceListActivity;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivity(intent);
        finish();
    }
}