package createTask;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.myapplication.R;

import java.util.Date;

import global.Category;
import global.Task;
import database.DatabaseContract;

public class CreateTaskActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_task);
    }


    public void saveTaskOnDB(){
        Task newTask = createTaskFromForm();

        ContentValues values = extractTaskToContentValues(newTask);

        getContentResolver().insert(DatabaseContract.Task.CONTENT_URI_FOR_ROWS, values);
    }


    private ContentValues extractTaskToContentValues(Task task) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.Task.COLUMN_NAME_TITLE, task.getTitle());
        values.put(DatabaseContract.Task.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(DatabaseContract.Task.COLUMN_NAME_CATEGORY, task.getCategory().toString());
        values.put(DatabaseContract.Task.COLUMN_NAME_DATE, task.getDate());

        return values;
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


    public void onClickSaveTask(View view){
        Log.i("TaskActivity", "onClickSaveTask");
        saveTaskOnDB();
        showConfirmationToast();
        Log.i("TaskActivity", "TASK SAVED");
        finish();
    }


    private void showConfirmationToast() {
        Toast.makeText(this, R.string.task_save_toast, Toast.LENGTH_LONG);
    }


}
