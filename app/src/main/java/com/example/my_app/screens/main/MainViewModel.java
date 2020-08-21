// этот класс предоставляет доступ к данным о списке заметок для того,
// чтобы этот список отобразился на главном экране
package com.example.my_app.screens.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.my_app.App;
import com.example.my_app.model.Note;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();

    // getter
    public LiveData<List<Note>> getNoteLiveData() {
        return noteLiveData;
    }

}
