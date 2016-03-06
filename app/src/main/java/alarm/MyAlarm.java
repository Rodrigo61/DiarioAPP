package alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.rodrigo.myapplication.R;

import global.SystemSettings;
import createTask.CreateTaskActivity;

import java.util.Calendar;

/**
 * Created by rodrigo on 27/01/16.
 */
public class MyAlarm extends BroadcastReceiver {

    public static final String STARTED_ALARM = "STARTED_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotificationToNewTask(context);
    }


    private void sendNotificationToNewTask(Context context) {
        Intent displayMessageIntent = new Intent(context, CreateTaskActivity.class);

        //TODO: é interessante nao sobrecarregar o broadcastReceiver delegando a uma Service para essa tarefa
        if(!SystemSettings.isSleepHour(context.getContentResolver())) {
            sendNotification(context, displayMessageIntent, R.drawable.ic_class_black_48dp);
            playAlertRingtone(context);
        }
    }


    private Notification createNotification(Context context, PendingIntent intent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = null;

        builder.setTicker(context.getString(R.string.MyAlarm_ticker_notification));
        builder.setContentTitle(context.getString(R.string.MyAlarm_title_notification));
        builder.setContentText(context.getString(R.string.MyAlarm_desc_notification));
        builder.setSmallIcon(R.drawable.ic_class_black_48dp);
        builder.setContentIntent(intent);

        notification = builder.build();
        notification.vibrate = new long[]{150, 300, 150, 600};
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        return notification;

    }


    private void sendNotification(Context context, Intent intent, int notificationID){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = createNotification(context, pendingIntent);

        notificationManager.notify(notificationID, notification);

    }


    private void playAlertRingtone(Context context){

        try{
            Uri ringtoneURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneURI);
            ringtone.play();
        }
        catch(Exception e){
            Log.i("MyAlarm", "Ringtone FAILED");
        }

    }


    public static void createAlarmIntent(Context context){
        Log.i("MyAlarm", "createAlarmIntent");
        SystemSettings.getAlarmDelay(context);
        long alarmDelay = SystemSettings.getAlarmDelay(context);

        if(!alarmRunning(context) && alarmDelay != 0){
            Intent alarmIntent = new Intent(STARTED_ALARM);
            PendingIntent alarmBroadcastIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 3);

            AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmDelay, alarmBroadcastIntent);
        }
    }


    public static boolean alarmRunning(Context context){
        Intent alarmIntent = new Intent(STARTED_ALARM);

        if(PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null){
            return true;
        }else{
            return false;
        }
    }


    public static void destroyAlarmIntent(Context context){
        if(alarmRunning(context)){
            Intent alarmIntent = new Intent(STARTED_ALARM);
            PendingIntent alarmBroadcastIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarm.cancel(alarmBroadcastIntent);
            alarmBroadcastIntent.cancel();
        }
    }


    public static void restartOrCreateAlarmIntent(Context context){
        destroyAlarmIntent(context);
        createAlarmIntent(context);
    }

}

