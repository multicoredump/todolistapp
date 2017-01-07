package com.coremantra.tutorial.todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTaskName;
        public CheckBox cbTaskIsDone;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            cbTaskIsDone = (CheckBox) itemView.findViewById(R.id.cbTaskIsDone);
        }
    }

    private List<Task> mTasks;

    private Context mContext;

    public TasksAdapter(Context context, List<Task> tasks) {
        mContext = context;
        mTasks = tasks;
    }

    private Context getContext() {
        return mContext;
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

        // Get the data model based on position
        Task task = mTasks.get(position);

        // Set item views based on your views and data model
        holder.tvTaskName.setText(task.getDescription());
        holder.cbTaskIsDone.setChecked(task.isDone());
    }

    // Returns the total count of items in the list

    @Override
    public int getItemCount() {
        Log.d("TAG", String.valueOf(mTasks.size()));
        return mTasks.size();
    }
}
