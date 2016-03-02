package updateTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.example.rodrigo.myapplication.R;

import java.util.Date;

import global.Category;
import global.Task;
import database.DatabaseContract;

public class UpdateTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_update_task);

        updateActivity();
    }


    private void updateActivity() {
        Task task = getExtraIntentTask();
        populateEditTextWithTask(task);
    }


    @NonNull
    private Task getExtraIntentTask() {
        Intent intent = getIntent();
        Task task = new Task();

        task.setTitle(intent.getStringExtra("title"));
        task.setDescription(intent.getStringExtra("description"));
        task.setCategory(new Category(intent.getStringExtra("category")));
        task.setID(intent.getIntExtra("_ID", -1));

        return task;
    }


    private int getIntentTaskID(){
        Intent intent = getIntent();
        return intent.getIntExtra("_ID", -1);
    }


    private void populateEditTextWithTask(Task task) {
        EditText title = (EditText) findViewById(R.id.task_title_editText);
        EditText description = (EditText) findViewById(R.id.task_description_editText);
        EditText category = (EditText) findViewById(R.id.task_category_editText);

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        category.setText(task.getCategory().toString());
    }


    public void onClickUpdateTask(View view){
        Task task = createTaskFromForm();
        ContentValues updateValues = new ContentValues();

        String selection = DatabaseContract.Task._ID + " = ?";
        String selectionArgs[] = new String[]{getIntentTaskID()+""};

        updateValues.put(DatabaseContract.Task.COLUMN_NAME_TITLE, task.getTitle());
        updateValues.put(DatabaseContract.Task.COLUMN_NAME_DESCRIPTION, task.getDescription());
        updateValues.put(DatabaseContract.Task.COLUMN_NAME_CATEGORY, task.getCategory().toString());

        getContentResolver().update(
                DatabaseContract.Task.CONTENT_URI_FOR_ROWS,
                updateValues,
                selection,
                selectionArgs);

        finish();
    }


    public void onClickDeleteTask(View view){
        Task task = getExtraIntentTask();

        String selection = DatabaseContract.Task._ID + " LIKE ?";
        String selectionArgs[] = new String[]{task.getID()+""};

        getContentResolver().delete(DatabaseContract.Task.CONTENT_URI_FOR_ROWS, selection, selectionArgs);

        finish();
    }


    public Task createTaskFromForm(){
        EditText title = (EditText) findViewById(R.id.task_title_editText);
        EditText category = (EditText) findViewById(R.id.task_category_editText);
        EditText description = (EditText) findViewById(R.id.task_description_editText);

        long current_date = new Date().getTime();

        Task newTask = new Task(title.getText().toString(),
                new Category(category.getText().toString()),
                description.getText().toString(), current_date, -1);

        return newTask;
    }

}
