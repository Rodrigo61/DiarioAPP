package com.example.rodrigo.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import alarm.MyAlarm;
import database.DatabaseContract;
import database.SleepHourDBAccessor;
import global.SystemSettings;
import global.Utils;

public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_settings);

        loadSavedSettingsToForm();
    }


    private void loadSavedSettingsToForm() {
        setAlarmDelaySettings();
        setReviewDaySettings();
        setSleepHourSetting();
    }


    private void setSleepHourSetting(){
        LinearLayout container = (LinearLayout) findViewById(R.id.current_hour_container);
        ArrayList<LinearLayout> hourViewList = gethourViewListFromDatabase();

        container.setOrientation(LinearLayout.VERTICAL);

        for(View hour : hourViewList){
            hour.setVisibility(View.VISIBLE);
            container.addView(hour);
        }

    }


    private ArrayList<LinearLayout> gethourViewListFromDatabase() {
        ArrayList<LinearLayout> hourViewList = new ArrayList<>();

        Cursor cursor = SleepHourDBAccessor.select(getContentResolver(), null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String timeRangeBegin = convertMillisToTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.SleepHour.COLUMN_NAME_TIME_RANGE_BEGIN)));
            String timeRangeEnd = convertMillisToTimeText(cursor.getLong(cursor.getColumnIndex(DatabaseContract.SleepHour.COLUMN_NAME_TIME_RANGE_END)));
            String days = cursor.getString(cursor.getColumnIndex(DatabaseContract.SleepHour.COLUMN_NAME_DAYS));
            int _ID = cursor.getInt(cursor.getColumnIndex(DatabaseContract.SleepHour._ID));

            LinearLayout layout= createHourView(timeRangeBegin, timeRangeEnd, days, _ID);
            hourViewList.add(layout);

            cursor.moveToNext();
        }

        return hourViewList;

    }


    private LinearLayout createHourView(String timeRangeBegin, String timeRangeEnd, String days, int _ID) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        EditText timeRangeBeginText = createEditText(timeRangeBegin);
        EditText timeRangeEndText = createEditText(timeRangeEnd);
        EditText daysText = createEditText(days);
        Button deleteButton = createDeleteButton(layout, _ID);

        layout.addView(timeRangeBeginText);
        layout.addView(timeRangeEndText);
        layout.addView(daysText);
        layout.addView(deleteButton);

        return layout;
    }


    public Button createDeleteButton(final LinearLayout parent, final int _ID){
        Button button = new Button(this);
        button.setText(R.string.settings_sleep_hour_delete_hour);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewManager)parent.getParent()).removeView(parent); //destroy hour line view

                String selection = "_ID = ?";
                String selectionArgs[] = {_ID+""};
                SleepHourDBAccessor.delete(getContentResolver(), selection, selectionArgs);
            }
        });

        return button;
    }


    @NonNull
    private EditText createEditText(String text) {
        EditText editText = new EditText(this);
        editText.setText(text);
        editText.setEnabled(false);
        return editText;
    }

    private void setReviewDaySettings() {
        EditText reviewDayText = (EditText) findViewById(R.id.settings_review_day_timeText);

        reviewDayText.setText(SystemSettings.getReviewDay(this) + "");
    }


    private void setAlarmDelaySettings() {
        EditText alarmDelayTimeText = (EditText) findViewById(R.id.settings_alarm_delay_timeText);

        alarmDelayTimeText.setText(Utils.convertMillisToMinutes(SystemSettings.getAlarmDelay(this)) + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.settings_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(getString(R.string.settings_repeat_delay), getAlarmDelaySettings());
        editor.putInt(getString(R.string.settings_review_day), getReviewDaySettings());

        editor.commit();

        updateApplicationServices();
        System.out.println("Save Settings done");
    }

    private int getReviewDaySettings() {
        EditText reviewDayText = (EditText) findViewById(R.id.settings_review_day_timeText);

        return Integer.parseInt(reviewDayText.getText().toString());
    }


    public long getAlarmDelaySettings(){

        EditText alarmDelayTimeText = (EditText) findViewById(R.id.settings_alarm_delay_timeText);
        int minutes = Integer.parseInt(alarmDelayTimeText.getText().toString());

        return Utils.convertMinutesToMillis(minutes);
    }


    public void newSleepHourOnClick(View view){
        EditText timeBeginEditText = (EditText) findViewById(R.id.settings_sleep_hour_new_hour_begin_editText);
        EditText timeEndEditText = (EditText) findViewById(R.id.settings_sleep_hour_new_hour_end_editText);
        EditText daysEditText = (EditText) findViewById(R.id.settings_sleep_hour_new_hour_days_editText);

        long timeRangeBegin = convertTimeTextToMillis(timeBeginEditText.getText().toString());
        long timeRangeEnd   = convertTimeTextToMillis(timeEndEditText.getText().toString());
        String days = daysEditText.getText().toString();

        SleepHourDBAccessor.insert(getContentResolver(), timeRangeBegin, timeRangeEnd, days);

        addSleepHourToLayout();
    }


    private void addSleepHourToLayout(){
        LinearLayout container = (LinearLayout) findViewById(R.id.current_hour_container);
        container.removeAllViews();

        setSleepHourSetting();
    }

    private long convertTimeTextToMillis(String timeText) {
        StringTokenizer stringTokenizer = new StringTokenizer(timeText, ":");

        long time = Utils.convertHourToMillis(Integer.parseInt(stringTokenizer.nextToken()));
        time += Utils.convertMinutesToMillis(Integer.parseInt(stringTokenizer.nextToken()));

        return time;
    }


    private String convertMillisToTimeText(long millis){
        return new SimpleDateFormat("mm:ss").format(new Date(millis));
    }


    public void updateApplicationServices(){
        MyAlarm.restartOrCreateAlarmIntent(this);
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment((EditText)v);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private EditText editText;

        public TimePickerFragment(EditText editText){
            this.editText = (EditText) editText;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editText.setText(hourOfDay+":"+minute);
        }
    }
}
