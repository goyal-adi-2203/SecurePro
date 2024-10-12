package com.example.securepro.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.securepro.domain.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user_table LIMIT 1")
    LiveData<User> getUser();

    @Update
    void update(User user);

    @Query("DELETE FROM user_table")
    void deleteUser();

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();
}
