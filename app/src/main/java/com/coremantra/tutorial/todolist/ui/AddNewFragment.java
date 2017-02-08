package com.coremantra.tutorial.todolist.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.coremantra.tutorial.todolist.R;
import com.coremantra.tutorial.todolist.ToDoListApplication;
import com.coremantra.tutorial.todolist.data.Task;

import java.util.Calendar;
import java.util.Date;

public class AddNewFragment extends DialogFragment{

    private static final String TAG = ToDoListApplication.BASE_TAG + AddNewFragment.class.getName();

    EditText etTaskName;
    CheckBox cbIsDone;
    Button btSave;
    Button btCancel;
    RadioGroup rgPriority;
    DatePicker dpDueDate;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnAddTaskInteractionListener {
        void onFinishAddDialog(Task toAdd);
        void onCancelAddDialog();
    }


    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String description = etTaskName.getText().toString();

            if (!description.isEmpty()) {
                // create a new todo_item and save it to database

                int checkedPriorityId = rgPriority.getCheckedRadioButtonId();
                Task.Priority priority = Task.Priority.HIGH;
                switch (checkedPriorityId) {
                    case R.id.radioHigh: priority = Task.Priority.HIGH;
                        break;
                    case R.id.radioMedium: priority = Task.Priority.MEDIUM;
                        break;
                    case R.id.radioLow: priority = Task.Priority.LOW;
                        break;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.DAY_OF_MONTH, dpDueDate.getDayOfMonth());
                calendar.set(Calendar.MONTH, dpDueDate.getMonth());
                calendar.set(Calendar.YEAR, dpDueDate.getYear());
                Date dueDate = calendar.getTime();
                Log.d(TAG, dueDate.toString());

                Task task = new Task(etTaskName.getText().toString(), cbIsDone.isChecked(), priority, dueDate);
                task.save();
                if (listener != null) listener.onFinishAddDialog(task);
            }

            dismiss();
        }
    };

    private View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) listener.onCancelAddDialog();
            dismiss();
        }
    };

    private OnAddTaskInteractionListener listener;

    public AddNewFragment() {
        // Required empty public constructor for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTaskName = (EditText) view.findViewById(R.id.etName);
        cbIsDone = (CheckBox) view.findViewById(R.id.cbIsDone);
        rgPriority = (RadioGroup) view.findViewById(R.id.radio_group_priority);
        dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);

        etTaskName.requestFocus();

        btSave = (Button) view.findViewById(R.id.btSave);
        btSave.setOnClickListener(saveClickListener);

        btCancel = (Button) view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(cancelClickListener);

        getDialog().setTitle(R.string.add_new_task);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddTaskInteractionListener) {
            listener = (OnAddTaskInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
