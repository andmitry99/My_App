// этот класс представляет из себя всю БД в целом
package com.example.my_app.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.my_app.model.Note;

// указать, какие сущности будут в БД
// entities - класс Note с аннотацией entity
// version - для редактирования версий приложения
// exportSchema - история версий
@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // метод, который позволяет получить доступ к dao нашей модели
    public abstract NoteDao noteDao();

}
