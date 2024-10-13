package com.example.securepro.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.securepro.data.database.AppRoomDatabase;
import com.example.securepro.data.local.UserDao;
import com.example.securepro.domain.model.User;
import com.example.securepro.domain.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepositoryImpl implements UserRepository {

    private UserDao userDao;
    private ExecutorService executorService;
    private LiveData<List<User>> userList;
    private LiveData<User> userLiveData;
    private String TAG = "user_repo_impl";

    public UserRepositoryImpl(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getInstance(application);
        userDao = db.userDao();
        userLiveData = userDao.getUser();
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void insertUser(User user) {
        Log.d(TAG, "insertUser: " + user.toString());
        executorService.execute(() -> userDao.insert(user));
    }

    @Override
    public LiveData<User> getUser() {
        return userLiveData;
    }

    @Override
    public void updateUser(User user) {
        executorService.execute(() -> userDao.update(user));
    }

    @Override
    public void deleteUser() {
        executorService.execute(() -> userDao.deleteUser());
    }

    @Override
    public LiveData<List<User>> getAllUsers() {
        return userList;
    }
}
