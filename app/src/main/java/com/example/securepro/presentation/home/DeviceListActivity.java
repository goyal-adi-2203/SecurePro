package com.example.securepro.presentation.home;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securepro.R;
import com.example.securepro.domain.model.Device;
import com.example.securepro.utils.DeviceListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DeviceListActivity extends AppCompatActivity {
    private DeviceViewModel deviceViewModel;
    private DeviceListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new DeviceListAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        deviceViewModel.getAllDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                adapter.setData(devices);
            }
        });

        FloatingActionButton fabAddDevice = findViewById(R.id.fab_add_device);
        fabAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device newDevice = new Device("dev1", "pswd", "door");
                deviceViewModel.insert(newDevice);
            }
        });

    }
}
