package com.example.securepro.presentation.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securepro.R;
import com.example.securepro.callbacks.SaveDeviceCallback;
import com.example.securepro.controllers.DeviceController.DeviceController;
import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.model.User;
import com.example.securepro.presentation.login.UserViewModel;
import com.example.securepro.utils.BaseActivity;
import com.example.securepro.utils.DeviceListAdapter;

import java.util.Map;

public class AddDeviceInfo extends BaseActivity {

    private DeviceViewModel deviceViewModel;
    private UserViewModel userViewModel;
    private User user;

    EditText deviceIdEditText, deviceNameEditText, deviceTypeEditText, passwordEditText, passwordCheckEditText;
    Button submitBtn;
    private static String LOCKED = "Locked";
    private String TAG = "addDeviceTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        getLayoutInflater().inflate(R.layout.activity_add_device_info, findViewById(R.id.content_frame));
        setupNavigation();

        Context context = getApplicationContext();

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
                } else if (!password.equals(reenterPassword)) {
                        Toast toast = Toast.makeText(context, "Password does not match!!", Toast.LENGTH_SHORT);
                        toast.show();
                } else {
                    // insert into database
                    Device newDevice = new Device(
                            deviceIdEditText.getText().toString(),
                            deviceNameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            deviceTypeEditText.getText().toString(),
                            LOCKED
                    );

                    userViewModel.getUser().observe(AddDeviceInfo.this, new Observer<User>() {
                        @Override
                        public void onChanged(User user1) {
                            if(user1 != null){
                                user = user1;
                                saveDevice(AddDeviceInfo.this, user, newDevice);
                            }
                        }
                    });



                    finish();
                }
            }
        });
    }

    private void saveDevice(Context context, User user, Device device){

        Log.d(TAG, "saveDevice: " + user.toString());

        DeviceController deviceController = new DeviceController();
        deviceController.saveDevice(context, user.getUserId(), device, new SaveDeviceCallback() {
            @Override
            public void onSuccess(Map<String, Object> responseData) {

                if(responseData.get("code").equals(201)){
                    deviceViewModel.insert(device);
                } else if (responseData.get("code").equals(200)){
                    deviceViewModel.update(device);
                }

                String message = responseData.containsKey("message") ? responseData.get("message").toString() : "message not get";
                Log.d(TAG, "onSuccess: " + message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Map<String, Object> errorData) {
                String message = errorData.containsKey("message") ? errorData.get("message").toString() : "message not get";
                Log.d(TAG, "onFailure: " + message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}