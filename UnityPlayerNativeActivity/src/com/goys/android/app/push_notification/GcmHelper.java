package com.goys.android.app.push_notification;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.goys.android.app.maintenance.MaintenancePublicUserFragment;
import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.GoysLog;
import com.goys.android.app.util.GoysService;
import com.goys.android.app.util.ResponseListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class GcmHelper implements ResponseListener {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	//Goys ID
	String SENDER_ID ="972589178411";
	
	/**
	 * Tag used on log messages.
	 */
	String TAG = "GCMDemo";

	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;
	

	String regid = "";
	Activity activity;
	CommonActions ca;
	GoysService gs;
	
	public GcmHelper(Context context, Activity activity) {

		this.activity = activity;
		this.context = context;
		this.ca = new CommonActions(activity);
		
		gs = new GoysService(activity);
		gs.setResponseListener(this);

	}

	@SuppressLint("NewApi")
	public void register() {
		
	
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(activity);
			regid = getRegistrationId(context);
			
			if (regid.isEmpty()) {
				registerInBackground();
			}else{
			    
			    GoysLog.e(TAG, "regid : " +regid);
			}
			
		} else {
			GoysLog.e(TAG, "No valid Google Play Services APK found.");
		}

	}
	
	private boolean checkPlayServices() 
	{
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				GoysLog.e(TAG, "This device is not supported.");
				activity.finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {

		String registrationId = ca.getValueString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			GoysLog.e(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = ca.getValueInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			GoysLog.e(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	@SuppressWarnings("unchecked")
	private void registerInBackground() {
		new AsyncTask() {
			protected String doInBackground(Object... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					
					
					GoysLog.e(TAG, "System Enter to Registration Backend");
					
					sendRegistrationIdToBackend(regid);

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (Exception ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			protected void onPostExecute(Object msg) {

				GoysLog.e(TAG, msg.toString());
			}
		}.execute(null, null, null);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(String regId) {
		// Your implementation here.
		//aduService.sendRegistrationId(regId, "Android", "0", ADUApplication.getLanguagePreferences());
		//gs.registerPushNotification(activity, regId, "Android", "");
		gs.registerPushNotification1(activity, regId, "Android", "");
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		int appVersion = getAppVersion(context);
		GoysLog.e(TAG, "Saving regId on app version " + appVersion);
		ca.saveUserPreferences(PROPERTY_REG_ID, regId);
		ca.savePreferences(PROPERTY_APP_VERSION, appVersion);
	}
	
	/**
	 * Remove the app registration id from shared preference
	 * @param context
	 */
	private void removeRegistrationId(Context context) {
		int appVersion = getAppVersion(context);
		GoysLog.e(TAG, "Removing regId on app version " + appVersion);
		ca.saveUserPreferences(PROPERTY_REG_ID, "");
	}

	@Override
	public void onResponse(int serviceId, String responseObj) {
		 if (activity != null) {
	            switch (serviceId) {
	                case AppConstants.REGISTER_PUSH_NOTIFICATION_ID:

	                    if (responseObj != null) {
	                        Log.d(TAG+"in onresponse", responseObj);
	                        if (responseObj.equalsIgnoreCase("true")) {
	                        	Log.d("Device Register on Server", "Successfully register app id");
	                          
	                         } else {
	                        	 //Clear reg id from local storage	
	                        	 removeRegistrationId(activity);
	                        	 Log.d("Device does not Register on Server", "UnSuccessfull register app id");
		                    }
	                        
	                    } else {
	                    	//Clear reg id from local storage	
	                    	removeRegistrationId(activity);
	                    	Log.d("Device not Register on Server", "UnSuccessfull register app id");
	                        
	                    }

	                    break;
	                    
	                
	               

	                default:
	                    break;
	            }
	        }
		
	}

	@Override
	public void onError(int responseCode, String msg) {
		
	}
	
	
}
