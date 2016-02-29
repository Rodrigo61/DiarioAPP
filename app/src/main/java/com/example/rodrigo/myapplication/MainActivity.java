package com.example.rodrigo.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import alarm.MyAlarm;
import categoryPieChart.PieChartActivity;
import createTask.CreateTaskActivity;
import taskList.TaskListActivity;
import weekList.WeekListActivity;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        System.out.println("onCreate");

        MyAlarm.createAlarmIntent(this);

    }


    public void openTaskActivity(View view){
        Intent taskActivityIntent = new Intent(this, CreateTaskActivity.class);
        startActivity(taskActivityIntent);
    }



    public void openWeekListActivity(View view){
        Intent taskWeekListActivityIntent = new Intent(this, WeekListActivity.class);
        startActivity(taskWeekListActivityIntent);
    }


    public void openSettingsActivity(View view){
        Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsActivityIntent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        MyAlarm.destroyAlarmIntent(this);
    }

}
