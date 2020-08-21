package com.example.my_app.screens.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.my_app.R;
import com.example.my_app.model.Note;
import com.example.my_app.App;

public class NoteDetailsActivity extends AppCompatActivity {

    // манипуляции с заметкой - как ее передавать и возвращать
    // 1 способ - передавать id заметки из основного списка, а здесь доставать по id из БД
    // Выбираем 2 способ - целиком через bundle передаем всю заметку внутрь этого activity
    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";

    // заметка, которую мы создаем или редактируем
    private Note note;

    // текстовое поле для ввода
    private EditText editText;

    // для вызова activity используем функцию
    public static void start(Activity caller, Note note) {
        // для запуска одного activity из другого создаем intent(activity-вызывающее, класс activity-вызываемого)
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if (note != null) {
            //прикрепляем заметку к нашему intent'у
            intent.putExtra(EXTRA_NOTE, note);
        }
        // запускаем activity по этому intent'у
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // приложение читает файл разметки и создает классы по описанию activity_note_details,
        // которые автоматически добавляются к activity
        setContentView(R.layout.activity_note_details);

        // панель инструментов
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // кнопка назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // текст заголовка
        setTitle(getString(R.string.note_details_title));

        // достаем editText (текстовое поля для ввода) по id
        editText = findViewById(R.id.text);

        // если intent при запуске activity, достаем нашу заметку из intent'a как он есть
        // и затем отправляем текст
        if (getIntent().hasExtra(EXTRA_NOTE)) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
        }
        // если заметки в intent'е нет, создаем новую
        else {
            note = new Note();
        }
    }

    // создание меню опций - сохранения
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // обработка для событий
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // проверяем id нажатой кнопки
        switch (item.getItemId()) {
            // если это кнопка "домой"( была нажата назад)
            case android.R.id.home:
                // тогда завершаем activity
                finish();
                break;
            // если кнопка меню сохранения
            case R.id.action_save:
                // проверка, есть ли какой-то текст
                if (editText.getText().length() > 0) {
                    // нужно заполнить заметку, текст берется из поля для ввода
                    note.text = editText.getText().toString();
                    // по умолчанию не сделано
                    note.done = false;
                    note.timestamp = System.currentTimeMillis();

                    // если заметка в интенте передавалась, то новая, ее нужно вставить, если старая - обновить
                    if (getIntent().hasExtra(EXTRA_NOTE)) {
                        App.getInstance().getNoteDao().update(note);
                    } else {
                        App.getInstance().getNoteDao().insert(note);
                    }
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
