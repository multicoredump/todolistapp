package com.coremantra.tutorial.todolist.data;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by radhikak on 1/6/17.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "ToDoListAppDatabase";

    public static final int VERSION = 2;
}
