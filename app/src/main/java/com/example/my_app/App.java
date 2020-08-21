package com.example.my_app;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.my_app.data.AppDatabase;
import com.example.my_app.data.NoteDao;

public class App extends Application {

    private AppDatabase database;
    private NoteDao noteDao;

    private static App instance;
    public static App getInstance(){
        return instance;
    }

    // ctrl+o, инициализация
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
// создание БД
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app_db_name")
                .allowMainThreadQueries()// разрешение на запросы к БД из основного потока
                .build();

        // получение dao объекта
        noteDao = database.noteDao();
    }

    // для досутпа извне
    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public void setNoteDao(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}