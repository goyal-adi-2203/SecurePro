package com.example.securepro.presentation.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepro.R;
import com.example.securepro.domain.model.Device;

public class AddDeviceInfo extends AppCompatActivity {

    private DeviceViewModel deviceViewModel;

    EditText deviceIdEditText, deviceNameEditText, deviceTypeEditText, passwordEditText, passwordCheckEditText;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_info);

        Context context = getApplicationContext();

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);

        deviceIdEditText = (EditText) findViewById(R.id.deviceIdEditText);
        deviceNameEditText = (EditText) findViewById(R.id.deviceNameEditText);
        deviceTypeEditText = (EditText) findViewById(R.id.deviceTypeEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordCheckEditText = (EditText) findViewById(R.id.reenterPasswordEditText);

        submitBtn = (Button) findViewById(R.id.submitBtnAddDevice);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                String reenterPassword = passwordCheckEditText.getText().toString();

                if (deviceIdEditText.getText() == null) {
                    Toast toast = Toast.makeText(context, "Enter Id!!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    if (!password.equals(reenterPassword)) {
                        Toast toast = Toast.makeText(context, "Password does not match!!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Device newDevice = new Device(deviceIdEditText.getText().toString(), deviceNameEditText.getText().toString(), passwordEditText.getText().toString(), deviceTypeEditText.getText().toString());
                        deviceViewModel.insert(newDevice);
                        Toast toast = Toast.makeText(context, "Device added!!", Toast.LENGTH_SHORT);
                        toast.show();

                        finish();
                    }
                }

            }
        });
    }
}