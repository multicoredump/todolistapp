package com.coremantra.tutorial.todolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by radhikak on 1/4/17.
 */

public class ToDosAdapter extends RecyclerView.Adapter<ToDosAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView descriptionTextView;
        public CheckBox isDoneCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            descriptionTextView = (TextView) itemView.findViewById(R.id.todoDescription);
            isDoneCheckBox = (CheckBox) itemView.findViewById(R.id.todoIsDone);
        }
    }

    private List<ToDo> mToDos;

    private Context mContext;

    public ToDosAdapter(Context context, List<ToDo> toDos) {
        mContext = context;
        mToDos = toDos;
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
        ToDo toDo = mToDos.get(position);

        // Set item views based on your views and data model
        holder.descriptionTextView.setText(toDo.getDescription());
        holder.isDoneCheckBox.setChecked(toDo.isDone());
    }

    // Returns the total count of items in the list

    @Override
    public int getItemCount() {
        Log.d("TAG", String.valueOf(mToDos.size()));
        return mToDos.size();
    }
}
