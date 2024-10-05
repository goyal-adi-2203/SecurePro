package com.example.securepro.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.securepro.data.local.DeviceDao;
import com.example.securepro.domain.model.Device;

@Database(entities = {Device.class}, version = 2, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    private static AppRoomDatabase instance;
    public abstract DeviceDao deviceDao();

    public static synchronized AppRoomDatabase getInstance(Context context){
        if(instance == null){
            instance =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "secure_pro_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }

        return instance;
    }

}
