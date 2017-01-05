package com.coremantra.tutorial.todolist;

import java.util.ArrayList;

/**
 * Created by radhikak on 1/4/17.
 */

public class ToDo {
    private String mDescription;
    private boolean mIsDone;

    public ToDo(String description, boolean isDone) {
        mDescription = description;
        mIsDone = isDone;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isDone() {
        return mIsDone;
    }

    private static int lastToDoId = 0;

    public static ArrayList<ToDo> createToDos(int numToDos) {
        ArrayList<ToDo> toDos = new ArrayList<ToDo>();

        for (int i = 1; i <= numToDos; i++) {
            toDos.add(new ToDo("Item " + ++lastToDoId, i <= numToDos / 2));
        }

        return toDos;
    }
}
