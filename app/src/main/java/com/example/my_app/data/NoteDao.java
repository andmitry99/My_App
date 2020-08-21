// этот класс описывает интрефейс, что мы хотим делать с БД

// для функционирования нашего слоя данных необходимо создать data access object (dao),
// то есть объект, через который будет осуществляться доступ к таблице БД, запись,
// чтение, выборка и т.д.
package com.example.my_app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.my_app.model.Note;

import java.util.List;

// аннотация для того, чтобы room знал, что это dao, а не просто интрефейс
@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note")
    List<Note> getAll();

    // LiveData - это специальный объект, который автоматически обновляется для тех объектов,
    // которые на него подписаны
    // пользовательский интерфейс может подписаться на ливдату
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllLiveData();

    // загрузить все заметки, у которых id из списка
    @Query("SELECT * FROM Note WHERE uid IN (:userIds)")
    List<Note> loadAllByAds (int[] userIds);

    // выборка по id
    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
    Note findById(int uid);

    // в скобках - если я хочу вставить заметку в БД с уже существующим id,
    // то при вставке будет произведена замена старой сущности на новую
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

}