package com.coremantra.tutorial.todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.coremantra.tutorial.todolist.data.Task;

import java.util.List;

/**
 * Created by radhikak on 1/4/17.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private static List<Task> taskList;
    
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTaskName;
        public CheckBox cbTaskIsDone;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            cbTaskIsDone = (CheckBox) itemView.findViewById(R.id.cbTaskIsDone);
            cbTaskIsDone.setOnClickListener(this);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            // Check if an item was deleted, but the user clicked it before the UI removed it
            if (position != RecyclerView.NO_POSITION) {
                Task toEdit = taskList.get(position);
                toEdit.setDone(((CheckBox)view).isChecked());
                toEdit.save();
            }
        }
    }

    // // TODO: 2/6/17 Consider Singleton 
    public TasksAdapter(List<Task> tasks) {
        taskList = tasks;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View todoView = inflater.inflate(R.layout.todo_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(todoView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position - from in-memory data
        Task task = taskList.get(position);

        // Set item views based on your views and data model
        holder.tvTaskName.setText(task.description);
        holder.cbTaskIsDone.setChecked(task.isDone);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
