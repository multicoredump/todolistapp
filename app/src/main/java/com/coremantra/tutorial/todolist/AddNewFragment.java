package com.coremantra.tutorial.todolist;

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
import android.widget.EditText;

import com.coremantra.tutorial.todolist.data.Task;

public class AddNewFragment extends DialogFragment {

    private static final String TAG = ToDoListApplication.BASE_TAG + AddNewFragment.class.getName();

    EditText etTaskName;
    CheckBox cbIsDone;
    Button btSave;
    Button btCancel;

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
                Task task = new Task(etTaskName.getText().toString(), cbIsDone.isChecked());
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
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTaskName = (EditText) view.findViewById(R.id.etName);
        cbIsDone = (CheckBox) view.findViewById(R.id.cbIsDone);


        etTaskName.requestFocus();


        btSave = (Button) view.findViewById(R.id.btSave);
        btSave.setOnClickListener(saveClickListener);

        btCancel = (Button) view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(cancelClickListener);

        getDialog().setTitle("Add New Task");

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
