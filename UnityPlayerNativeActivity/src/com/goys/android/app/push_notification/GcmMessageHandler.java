
package com.goys.android.app.push_notification;



import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.news.NewsActivity;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.GoysLog;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;



public class GcmMessageHandler extends IntentService {

    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;

    NotificationCompat.Builder builder;

    String msg, titleEn,titleAr,title,id;
    
    CommonActions ca;

    String TAG = "GCMMessageHandler";

    private Handler handler;
    
    
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        ca = new CommonActions(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
             * GCM will be extended in the future with new message types, just
             * ignore any message types you're not interested in, or that you
             * don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("GCM NOTIFICATION" ,"Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("GCM NOTIFICATION",
                        "Deleted messages on server: " );
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 2; i++) {
                    GoysLog.e(TAG, "Working... " + (i + 1) + "/2 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }
                }

                GoysLog.e(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

                titleEn = extras.getString("titleEN");
                titleAr = extras.getString("titleAR");
                
                // Post notification of received message.
                sendNotification(titleEn, titleAr);
                GoysLog.e(TAG, "Received: " + extras.toString());

                // Showing Toast for testing.
                showToast();
                GoysLog.e(TAG, "Received : (" + messageType + ")  " + extras.toString());

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String titleEn,String titleAr) {
       
    	mNotificationManager = (NotificationManager)this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int requestID = (int) System.currentTimeMillis();
      
        if(isEnglishLanguage()){
        	title = titleEn;
        }else{
        	title = titleAr;
        }
        
        
        Intent resultIntent = new Intent(this, NewsActivity.class);
        
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(NewsActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);

        
      
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher).setContentTitle("GOYS").setContentText(title)
                .setAutoCancel(true);
        //.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
         mBuilder.setContentIntent(resultPendingIntent);
        
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    
    public boolean isEnglishLanguage() {
		boolean isEnglishSelected = false;

		if (ca.getValueInt("langType", -1) == 0) {
			isEnglishSelected = true;
		} else if (ca.getValueInt("langType", -1) == 1) {
			isEnglishSelected = false;
		}
		return isEnglishSelected;
	}

}
