package com.example.securepro.callbacks;

import java.util.Map;

public interface SaveDeviceCallback {
    void onSuccess(Map<String, Object> responseData);
    void onFailure(Map<String, Object> errorData);
}
