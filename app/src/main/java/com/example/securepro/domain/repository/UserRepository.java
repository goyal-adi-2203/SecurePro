package com.example.securepro.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.securepro.domain.model.Device;
import com.example.securepro.domain.model.User;

import java.util.List;

public interface UserRepository {
    public void insertUser(User user);
    public LiveData<User> getUser();
    public void updateUser(User user);
    public void deleteUser();
    public LiveData<List<User>> getAllUsers();
}
