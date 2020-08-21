// этот класс описывает конкретную сущность
package com.example.my_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

// как сделать, чтобы эту модель сохранить в базу данных
@Entity
// entity - указывает, что этот класс является сущностью
// entity - это сущность которая является отображением в базе данных
// implements Parcelable - чтобы иметь возможность передавать заметку Note между Activity
// в проекте 2 экрана - главный и экран созданя и редактирования заметки,
// они будут реализованы как Activity
public class Note implements Parcelable {

    // это значит, что когда мы будем вставлять новый элемент в базу данных - новую заметку
    // то ей автоматически присвоится следующий по порядку свободный номер id
    @PrimaryKey(autoGenerate = true)
    // уникальный идентификатор, свое значение для каждой из сущностей
    public int uid;

    // в этой аннотации пишем название колонки в БД, в которой будут сохраняться соответствующие поля
    @ColumnInfo(name = "text")
    // текст заметки
    public String text;

    @ColumnInfo(name = "timestamp")
    // необходимо знать, в какое время была создана заметка
    public long timestamp;

    @ColumnInfo(name = "done")
    // проверить, сделано дело или нет
    public boolean done;

    // конструктор из параметров
    public Note() {
    }

    //____________________________________________________________
    // alt + insert -> equals() and hashcode()
    // эти функции для сравнения объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;

        if (done != note.done) return false;
        if (uid != note.uid) return false;
        if (timestamp != note.timestamp) return false;
        return text != null ? text.equals(note.text) : note.text == null;
    }


    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (done ? 1 : 0);
        return result;
    }

    //_________________________________________________________
    protected Note(Parcel in) {
        uid = in.readInt();
        text = in.readString();
        timestamp = in.readLong();
        done = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (done ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}