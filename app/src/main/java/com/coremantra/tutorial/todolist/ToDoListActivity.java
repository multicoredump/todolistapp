package com.coremantra.tutorial.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.coremantra.tutorial.todolist.data.Task;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity {

    private static final String TAG = ToDoListActivity.class.getName();
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
            Toast.makeText(getApplicationContext(), toEdit.getDescription(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);

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
        // create a new todo_item and save it to database
        Task task = new Task();
        task.setDescription(etTodoDescription.getText().toString());
        task.setDone(false);
        task.save();

        // add this todo_item at the end of the list
        int position = tasks.size();
        Log.d(TAG, "position: " + position);
        tasks.add(position, task);
        tasksAdapter.notifyItemInserted(position);
        rvToDos.scrollToPosition(position);

        etTodoDescription.setText("");
    }

}
