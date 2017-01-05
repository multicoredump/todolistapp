package com.coremantra.tutorial.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {

    private static final String TAG = ToDoListActivity.class.getName();
    ArrayList<ToDo> toDoItems;

    RecyclerView rvToDos;
    EditText etTodoDescription;

    ToDosAdapter toDosAdapter;

    ItemClickSupport.OnItemLongClickListener onItemLongClickListener = new ItemClickSupport.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
            toDoItems.remove(position);
            toDosAdapter.notifyItemRemoved(position);
            // save data to db
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);

        rvToDos = (RecyclerView) findViewById(R.id.rvToDos);
        etTodoDescription = (EditText) findViewById(R.id.etNewToDo);

        toDoItems = ToDo.createToDos(14);

        // Create adapter passing in the sample user data
        toDosAdapter = new ToDosAdapter(this, toDoItems);

        // Attach the adapter to the recyclerview to populate items
        rvToDos.setAdapter(toDosAdapter);

        // Set layout manager to position the items
        rvToDos.setLayoutManager(new LinearLayoutManager(this));

        ItemClickSupport.addTo(rvToDos).setOnItemLongClickListener(onItemLongClickListener);
    }

    public void onAddItem(View view) {
        // create a new todo_item
        ToDo toDo = new ToDo(etTodoDescription.getText().toString(), false);
        etTodoDescription.setText("");

        // add this todo_item at the end of the list
        int position = toDoItems.size();
        Log.d(TAG, "position: " + position);
        toDoItems.add(position, toDo);
        toDosAdapter.notifyItemInserted(position);
        rvToDos.scrollToPosition(position);
    }

}
