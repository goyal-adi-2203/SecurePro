package com.example.securepro.presentation.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.securepro.data.repository.UserRepositoryImpl;
import com.example.securepro.domain.model.User;
import com.example.securepro.domain.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<User> userLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepositoryImpl(application);
        userLiveData = userRepository.getUser();
        if(userLiveData == null){
            userLiveData = new MutableLiveData<>();
        }
    }

    public void insert(User user){
        userRepository.insertUser(user);
    }

    public LiveData<User> getUser(){
        return userLiveData;
    }

    public void update(User user){
        userRepository.updateUser(user);
    }

    public void delete(){
        userRepository.deleteUser();
    }
}
