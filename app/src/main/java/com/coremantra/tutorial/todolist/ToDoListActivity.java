package com.coremantra.tutorial.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.coremantra.tutorial.todolist.data.Task;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.parceler.Parcels;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity {

    private static final String TAG = ToDoListActivity.class.getName();
    private static final int REQUEST_EDIT = 100;

    private int positionEdited = -1;

    List<Task> tasks;

    RecyclerView rvToDos;
    EditText etTodoDescription;

    TasksAdapter tasksAdapter;

    ItemClickSupport.OnItemLongClickListener itemLongClickListener = new ItemClickSupport.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
            Task toDelete = tasks.get(position);
            tasks.remove(position);
            tasksAdapter.notifyItemRemoved(position);

            // remove from database
            toDelete.delete();
            return true;
        }
    };

    ItemClickSupport.OnItemClickListener itemClickListener = new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            // create a toast and display todo name
            Task toEdit = tasks.get(position);
            positionEdited = position;
            launchEditView(toEdit);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        rvToDos = (RecyclerView) findViewById(R.id.rvToDos);
        etTodoDescription = (EditText) findViewById(R.id.etNewToDo);

        tasks = readExistingToDos();

        // Create adapter passing existing data
        tasksAdapter = new TasksAdapter(this, tasks);

        // Attach the adapter to the recyclerview to populate items
        rvToDos.setAdapter(tasksAdapter);

        // Set layout manager to position the items
        rvToDos.setLayoutManager(new LinearLayoutManager(this));

        ItemClickSupport.addTo(rvToDos).setOnItemLongClickListener(itemLongClickListener);
        ItemClickSupport.addTo(rvToDos).setOnItemClickListener(itemClickListener);
    }

    private List<Task> readExistingToDos() {
        // read from database
        return SQLite.select().from(Task.class).queryList();
    }

    public void onAddItem(View view) {
        String description = etTodoDescription.getText().toString();

        if (!description.isEmpty()) {
            // create a new todo_item and save it to database
            Task task = new Task(etTodoDescription.getText().toString(), false);
            task.save();

            // add this todo_item at the end of the list
            int position = tasks.size();
            tasks.add(position, task);
            tasksAdapter.notifyItemInserted(position);
            rvToDos.scrollToPosition(position);

            etTodoDescription.setText("");
        }
    }

    private void launchEditView(Task toEdit) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("taskToEdit", Parcels.wrap(toEdit));
        startActivityForResult(intent, REQUEST_EDIT); // brings up the second activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT) {
            // update in-memory data model

            Task updatedTask = Task.retrieveUsing(tasks.get(positionEdited).id);
            tasks.set(positionEdited, updatedTask);

            // notiify recycler view's adapter to update the view
            tasksAdapter.notifyItemChanged(positionEdited);
        }
    }
}

