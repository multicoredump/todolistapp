package com.coremantra.tutorial.todolist;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by radhikak on 1/6/17.
 */

public class ToDoListApplication extends Application {

    public static final String BASE_TAG = "ToDo: ";

    @Override
    public void onCreate() {
        super.onCreate();

        // Instantiate DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
        // add for verbose logging
         FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        FlowManager.destroy();
    }
}
