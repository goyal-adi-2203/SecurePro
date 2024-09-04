package com.example.securepro.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.securepro.domain.model.Device;

@Database(entities = {Device.class}, version = 1)
public abstract class DeviceDatabase extends RoomDatabase {
    private static DeviceDatabase instance;
    public DeviceDao deviceDao;

    public static synchronized DeviceDatabase getInstance(Context context){
        if(instance == null){
            instance =
                    Room.databaseBuilder(context.getApplicationContext(),
                            DeviceDatabase.class, "device_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };


}
