package com.example.my_app.screens.main;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.my_app.App;
import com.example.my_app.R;
import com.example.my_app.model.Note;
import com.example.my_app.screens.details.NoteDetailsActivity;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {

    private SortedList<Note> sortedList;

    public Adapter() {
        // класс SortedList предназначен для того,
        // чтобы автоматически определять изменения внутри себя и
        // выдавать соответствующие команды что в нем обновилось на RecyclerView
        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {

            // compare сравнивает между собой два объекта,
            // с помощью него SortedList понимает, какой из элементов больше
            @Override
            public int compare(Note o1, Note o2) {
                if (!o2.done && o1.done) {
                    return 1;
                }
                if (o2.done && !o1.done) {
                    return -1;
                }
                // сравнение по аремени создания
                return (int) (o2.timestamp - o1.timestamp);
            }

            // когда элемент меняет позицию, вызывается этот метод
            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            // true если два элемента полностью равны
            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
                return oldItem.equals(newItem);
            }

            // true если у двух элементов совпадают id
            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
                return item1.uid == item2.uid;
            }

            // далее функции, которые сообщают адаптеру об изменениях
            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    // создаем новый ViewHolder и возвращаем
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false));
    }

    // привязка конкретной заметки к ViewHolder'у
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    // число элементов в адаптере
    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    // функция позволит обновить список содержимого адаптера
    public void setItems(List<Note> notes) {
        sortedList.replaceAll(notes);
    }

    // создать класс ViewHolder для отдельного элемента,
    // этот класс хранит в себе ссылки на View
    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteText;
        CheckBox completed;
        View delete;

        // хранение отображающейся в текущий момент заметки
        Note note;

        boolean silentUpdate;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);

            noteText = itemView.findViewById(R.id.note_text);
            completed = itemView.findViewById(R.id.completed);
            delete = itemView.findViewById(R.id.delete);

            // обработчик для itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoteDetailsActivity.start((Activity) itemView.getContext(), note);
                }
            });

            // обработчик delete
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.getInstance().getNoteDao().delete(note);
                }
            });

            // обработчик completed
            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!silentUpdate) {
                        note.done = isChecked;
                        App.getInstance().getNoteDao().update(note);
                    }
                    updateStrokeOut();
                }
            });
        }

        // функция, которая отображает значение полей заметки на View
        public void bind (Note note) {
            this.note = note;

            // в поле для текста записываем текст заметки
            noteText.setText(note.text);
            updateStrokeOut();

            // устанавливаем значение CheckBox - выполнено или нет
            silentUpdate = true;
            completed.setChecked(note.done);
            silentUpdate = false;
        }

        // проверка на зачеркивание
        private void updateStrokeOut() {
            if (note.done) {
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                noteText.setPaintFlags(noteText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
}
