package com.coremantra.tutorial.todolist.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;

public class EditTaskFragment extends DialogFragment {

    private static final String TAG = ToDoListApplication.BASE_TAG + EditTaskFragment.class.getName();

    private static final String TASK_TO_EDIT = "taskToEdit";

    private Task toEdit;

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

    public interface OnEditTaskInteractionListener {
        void onFinishEditDialog(boolean dateUpdated);
        void onCancelEditDialog();
    }

    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String description = etTaskName.getText().toString();
            boolean dateUpdated = false;
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

                dateUpdated = dueDate.compareTo(toEdit.timestamp) == 0 ? false : true;

                toEdit.update(etTaskName.getText().toString(), cbIsDone.isChecked(), priority, dueDate);
            }
            if (listener != null) listener.onFinishEditDialog(dateUpdated);
            dismiss();
        }
    };

    private View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) listener.onCancelEditDialog();
            dismiss();
        }
    };

    private OnEditTaskInteractionListener listener;

    public EditTaskFragment() {
        // Required empty public constructor for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditTaskFragment.
     */
    public static EditTaskFragment newInstance(Task toEdit) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable(TASK_TO_EDIT, Parcels.wrap(toEdit));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            toEdit = Parcels.unwrap(args.getParcelable(TASK_TO_EDIT));
        }
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

        etTaskName.setText(toEdit.description);
        // Make sure the cursor in the text field is at the end of the current text
        etTaskName.setSelection(etTaskName.getText().length());
        etTaskName.requestFocus();

        cbIsDone.setChecked(toEdit.isDone);

        // set priority
        int priorityRadioId = R.id.radioHigh;
        switch (toEdit.priority) {
            case HIGH:
                priorityRadioId = R.id.radioHigh;
                break;
            case MEDIUM:
                priorityRadioId = R.id.radioMedium;
                break;
            case LOW:
                priorityRadioId = R.id.radioLow;
                break;
        }
        rgPriority.check(priorityRadioId);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(toEdit.timestamp);

        dpDueDate.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), null);

        btSave = (Button) view.findViewById(R.id.btSave);
        btSave.setOnClickListener(saveClickListener);

        btCancel = (Button) view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(cancelClickListener);

        getDialog().setTitle("Edit Task");

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditTaskInteractionListener) {
            listener = (OnEditTaskInteractionListener) context;
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
