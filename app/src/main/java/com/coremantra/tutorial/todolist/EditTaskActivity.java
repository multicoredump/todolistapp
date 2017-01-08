package com.coremantra.tutorial.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.coremantra.tutorial.todolist.data.Task;

import org.parceler.Parcels;

import static com.coremantra.tutorial.todolist.R.id.etName;

/**
 * Created by radhikak on 1/7/17.
 */

public class EditTaskActivity  extends Activity {

    public static final String TAG = EditTaskActivity.class.getName();

    EditText etTaskName;
    CheckBox cbIsDone;

    Task toEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTaskName = (EditText) findViewById(etName);
        cbIsDone = (CheckBox) findViewById(R.id.cbIsDone);

        toEdit = Parcels.unwrap(getIntent().getParcelableExtra("taskToEdit"));

        etTaskName.setText(toEdit.description);
        // Make sure the cursor in the text field is at the end of the current text
        etTaskName.setSelection(etTaskName.getText().length());
        etTaskName.requestFocus();

        cbIsDone.setChecked(toEdit.isDone);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void onSaveItem(View view) {
        toEdit.setDescription(etTaskName.getText().toString());
        toEdit.setDone(cbIsDone.isChecked());
        toEdit.save();

        setResult(RESULT_OK); // set result code and bundle data for response
        finish();
    }


}
