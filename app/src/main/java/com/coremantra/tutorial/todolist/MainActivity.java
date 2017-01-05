package com.coremantra.tutorial.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aToDoAdapter;

    private ListView mLvItems;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateTodoListItems();

        mEditText = (EditText) findViewById(R.id.etEditText);
        mLvItems = (ListView) findViewById(R.id.lvItems);
        mLvItems.setAdapter(aToDoAdapter);

        mLvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
            }
        });
    }

    public void populateTodoListItems() {
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, todoItems);
    }

    private void readItems() {
        File file = new File(getFilesDir(), "todo.txt");

        /* UnComment for first run */
//        String filename = "todo.txt";
//        String string = "Hello world!";
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(string.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            if (file.exists())
                todoItems = new ArrayList<>(FileUtils.readLines(file));
        } catch (IOException e) {
            Log.e("MainActivity", "Exception", e);
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");

        try {
           FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            Log.e("MainActivity", "Exception", e);
        }
    }

    public void onAddItem(View view) {
        aToDoAdapter.add(mEditText.getText().toString());
        mEditText.setText("");
        writeItems();
    }

}
