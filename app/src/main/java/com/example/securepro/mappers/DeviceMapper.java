package com.example.securepro.mappers;

import android.util.Log;

import com.example.securepro.domain.model.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceMapper {
    private static String TAG = "DeviceMapper";

    public static List<Device> deserializer(String response) throws JSONException {
        Log.d(TAG, "from deserializer: "+ response);
        JSONObject jsonObject = new JSONObject(response);

        List<Device> deviceList = new ArrayList<>();

        if(jsonObject.has("devices")){
            JSONArray jsonArray = new JSONArray(jsonObject.getString("devices"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = jsonArray.getJSONObject(i);

                String id = element.has("id") ? element.getString("id") : null;
                String name = element.has("name") ? element.getString("name") : "device";
                String password = element.has("password") ? element.getString("password") : "abc";
                String deviceType = element.has("deviceType") ? element.getString("deviceType") : "other";
                String status = element.has("status") ? element.getString("status") : "Locked";

                Device device = new Device(id, name, password, deviceType, status);
                deviceList.add(device);
            }

        }

        return deviceList;
    }
}
