package com.coremantra.tutorial.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.coremantra.tutorial.todolist.R;
import com.coremantra.tutorial.todolist.ToDoListApplication;
import com.coremantra.tutorial.todolist.adapaters.TasksAdapter;
import com.coremantra.tutorial.todolist.data.Task;
import com.coremantra.tutorial.todolist.data.Task_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements EditTaskFragment.OnEditTaskInteractionListener, AddNewFragment.OnAddTaskInteractionListener {

    private static final String TAG = ToDoListApplication.BASE_TAG + ToDoListActivity.class.getName();
    private static final int REQUEST_EDIT = 100;

    private int positionEdited = -1;

    private List<Task> tasks;

    private RecyclerView rvToDos;

    private FloatingActionButton fabAdd;

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
            launchEditDialog(toEdit);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        rvToDos = (RecyclerView) findViewById(R.id.rvToDos);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);

        tasks = readExistingToDos();

        // Create adapter passing existing data
        tasksAdapter = new TasksAdapter(tasks);

        // Attach the adapter to the recyclerview to populate items
        rvToDos.setAdapter(tasksAdapter);

        // Set layout manager to position the items
        rvToDos.setLayoutManager(new LinearLayoutManager(this));

        ItemClickSupport.addTo(rvToDos).setOnItemLongClickListener(itemLongClickListener);
        ItemClickSupport.addTo(rvToDos).setOnItemClickListener(itemClickListener);
    }

    private List<Task> readExistingToDos() {
        // read from database
        return SQLite.select().from(Task.class).orderBy(Task_Table.timestamp, true).queryList();
    }

    private void launchEditDialog(Task toEdit) {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(toEdit);
        editTaskFragment.show(fm, "fragment_task");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT) {
            // update in-memory data model
            onUpdateItem();
        }
    }

    private void onUpdateItem() {
        Task updatedTask = Task.retrieveUsing(tasks.get(positionEdited).id);
        tasks.set(positionEdited, updatedTask);
        // notify recycler view's adapter to update the view
        tasksAdapter.notifyItemChanged(positionEdited);
    }

    @Override
    public void onFinishEditDialog(boolean dateUpdated) {
        if (dateUpdated) { // re-sort tasks by due date
            tasks = readExistingToDos();
            tasksAdapter.updateTasks(tasks);
        } else {
            onUpdateItem();
        }
    }

    @Override
    public void onCancelEditDialog() {}

    public void onAddClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        new AddNewFragment().show(fm, "fragment_add_task");
    }

    @Override
    public void onFinishAddDialog(Task toAdd) {
        // add this todo_item at the end of the list
        int position = tasks.size();
        tasks = readExistingToDos();
        tasksAdapter.updateTasks(tasks);
        rvToDos.scrollToPosition(position);
    }

    @Override
    public void onCancelAddDialog() {}
}

