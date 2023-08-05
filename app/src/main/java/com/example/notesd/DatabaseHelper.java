package com.example.notesd;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Notes.class}, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {

    final static private String DB_NAME = "NOTES";
    static private DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    public abstract NotesDao notesdao();
}
