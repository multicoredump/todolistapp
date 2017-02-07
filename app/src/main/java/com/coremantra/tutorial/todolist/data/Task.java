package com.coremantra.tutorial.todolist.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by radhikak on 1/6/17.
 */

/** How to use DBFLow
 * https://guides.codepath.com/android/DBFlow-Guide
 */

/** How to dump date on adb shell
 * http://stackoverflow.com/questions/75675/how-do-i-dump-the-data-of-some-sqlite3-tables
 */

/** How to use Parceler
 * https://guides.codepath.com/android/Using-Parceler
 * With DBFLow : https://guides.codepath.com/android/DBFlow-Guide#using-with-the-parceler-library
 */

@Table(database = AppDatabase.class)
@Parcel(analyze={Task.class})   // add Parceler annotation here
public class Task extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String description;

    @Column
    public Boolean isDone;

    @Column
    public Date timestamp;

    public enum Priority {
        HIGH,
        MEDIUM,
        LOW
    }

    @Column
    public Priority priority;

    // empty constructor needed by the Parceler library
    public Task() {
    }

    public Task(String description, Boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

//    public int getId() {
//        return id;
//    }

    public void setDescription(final String description) {
        this.description = description;
    }

//    public String getDescription() {
//        return description;
//    }

    public void setDone(Boolean done) {
        isDone = done;
    }

//    public boolean isDone() {
//        return isDone;
//    }

    public static  long getCount() {
        return new Select().distinct().from(Task.class).count();
    }

    @Override
    public String toString() {
        return id + "|" + description;
    }

    public static Task retrieveUsing(final int id)  {
        Task task = SQLite.select().
                from(Task.class).
                where(Task_Table.id.eq(id)).
                querySingle();

        return task;
    }
}
