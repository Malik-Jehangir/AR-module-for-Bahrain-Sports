package com.goys.android.app.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andorid.components.afilechooser.utils.FileUtils;

import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.emigration.EmigrationAndVisaActivity;
import com.goys.android.app.sportsparticipation.SportsParticipationsForNationalClubsActivity;

public class CommonActions {

	Activity currentActivity;
	Context currentContext;

	static Typeface universe_53, adobe_arabic_regular;

	static boolean isNetworkErrorShown = false;

	public CommonActions(Activity activity) {
		this.currentActivity = activity;

		adobe_arabic_regular = Typeface.createFromAsset(
				currentActivity.getAssets(), "fonts/adobe_arabic_regular.otf");

	}

	public CommonActions(Context activity) {

		currentContext = activity;
		adobe_arabic_regular = Typeface.createFromAsset(activity.getAssets(),
				"fonts/adobe_arabic_regular.otf");
	}

	public static void setTextTypeFace(TextView tv) {

		if (GOYSApplication.appLanguage.contains("ar")) {

			tv.setTypeface(adobe_arabic_regular);

		}

	}

	public void showProgress() {

		try {
			
		} catch (BadTokenException e) {

		} catch (Exception e) {

		}
	}

	public void showProgress(String msg) {
		try {
		} catch (BadTokenException e) {

		} catch (Exception e) {

		}
	}

	public void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = currentActivity
				.getSharedPreferences(GOYSApplication.APP_SETTINGS_FILE,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void clearAllPreferences() {
		SharedPreferences sharedPreferences = currentActivity
				.getSharedPreferences(GOYSApplication.APP_SETTINGS_FILE,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	public void saveUserPreferences(String key, String value) {
		SharedPreferences sharedPreferences = currentActivity
				.getSharedPreferences(GOYSApplication.APP_SETTINGS_FILE,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getValueString(String key, String default_value) {

		SharedPreferences sharedPreferences = currentActivity
				.getSharedPreferences(GOYSApplication.APP_SETTINGS_FILE,
						Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, default_value);
	}

	public boolean getValueBoolean(String key, boolean default_value) {

		SharedPreferences sharedPreferences = currentActivity
				.getSharedPreferences(GOYSApplication.APP_SETTINGS_FILE,
						Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, default_value);
	}

	public int getValueInt(String key, int default_value) {

		SharedPreferences sharedPreferences = ((currentActivity != null ? currentActivity
				: currentContext)).getSharedPreferences(
				GOYSApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, default_value);
	}

	public static boolean hasConnection(Activity currentActivity) {

		try {
			ConnectivityManager cm = (ConnectivityManager) currentActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo wifiNetwork = cm
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetwork != null && wifiNetwork.isConnected()) {
				return true;
			}

			NetworkInfo mobileNetwork = cm
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobileNetwork != null && mobileNetwork.isConnected()) {
				return true;
			}

			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			GoysLog.e("Network Error ", e.getMessage());
		}

		return false;
	}

	public Typeface getUniverse_53() {
		return universe_53;
	}

	public void savePreferences(String key, int value) {
		SharedPreferences sharedPreferences = ((currentActivity != null ? currentActivity
				: currentContext)).getSharedPreferences(
				GOYSApplication.APP_SETTINGS_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void alertDialogShow(Activity ctx, String title,
			String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(
						ctx.getResources().getString(R.string.alert_button_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.setView(null);

		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showSuccessfulSubmissionAlert(final Activity act,
			String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(
						act.getResources().getString(R.string.alert_button_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();

								/*
								 * checking the instance and calling clear form
								 * method for appropriate class.
								 */
								if (act instanceof EmigrationAndVisaActivity) {
									((EmigrationAndVisaActivity) act)
											.clearForm();
								} else if (act instanceof SportsParticipationsForNationalClubsActivity) {
									((SportsParticipationsForNationalClubsActivity) act)
											.clearForm();
								}
								act.finish();

							}

						});
		builder.setView(null);

		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showErrorAlertDialog(Context ctx, int error_code,
			String msg) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(
				ctx.getResources().getString(R.string.alert_error_title))
				.setMessage(msg)
				.setCancelable(false)
				.setNegativeButton(
						ctx.getResources().getString(R.string.alert_button_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.setView(null);

		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showNetworkError(Context ctx, int responseCode) {

		if (!isNetworkErrorShown) {

			switch (responseCode) {

			}
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setTitle("")
					.setMessage(
							ctx.getResources()
									.getString(R.string.error_network))
					.setCancelable(false)
					.setNegativeButton(
							ctx.getResources().getString(
									R.string.alert_button_ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									isNetworkErrorShown = false;
								}
							});
			builder.setView(null);

			AlertDialog alert = builder.create();
			alert.show();

			isNetworkErrorShown = true;
		}

	}

	public static void viewFileDialog(final Activity currentAct) {

		final CharSequence[] items = {
				currentAct.getResources().getString(R.string.takePicture),
				currentAct.getResources().getString(R.string.picfromGal),
				currentAct.getResources().getString(R.string.cancel) };

		AlertDialog.Builder builder = new AlertDialog.Builder(currentAct);
		builder.setTitle(currentAct.getResources().getString(
				R.string.picker_dialog_title));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals(currentAct.getResources().getString(
						R.string.takePicture))) {
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					currentAct.startActivityForResult(cameraIntent,
							AppConstants.CAPTURE_IMAGE);

				} else if (items[item].equals(currentAct.getResources()
						.getString(R.string.picfromGal))) {
					Intent target = FileUtils.createGetContentIntent();
					// Create the chooser Intent
					Intent intent = Intent.createChooser(target,
							currentAct.getString(R.string.chooser_title));
					try {
						currentAct.startActivityForResult(target,
								AppConstants.PICK_IMAGE);
					} catch (ActivityNotFoundException e) {
						// The reason for the existence of aFileChooser
						System.out.println(e.getMessage());
					}
				} else if (items[item].equals(currentAct.getResources()
						.getString(R.string.cancel))) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public static void viewFileDialogWithFewOption(final Activity currentAct) {

		final CharSequence[] items = {
				currentAct.getResources().getString(R.string.takePicture),
				currentAct.getResources().getString(R.string.picfromGal),
				currentAct.getResources().getString(R.string.cancel) };

		AlertDialog.Builder builder = new AlertDialog.Builder(currentAct);
		builder.setTitle(currentAct.getResources().getString(
				R.string.picker_dialog_title));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				List<Intent> targetShareIntents = new ArrayList<Intent>();

				if (items[item].equals(currentAct.getResources().getString(
						R.string.takePicture))) {
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					currentAct.startActivityForResult(cameraIntent,
							AppConstants.CAPTURE_IMAGE);

				} else if (items[item].equals(currentAct.getResources()
						.getString(R.string.picfromGal))) {
					// Intent target = FileUtils.createGetContentIntent();

					// Implicitly allow the user to select a particular kind of
					// data
					Intent target = new Intent(Intent.ACTION_GET_CONTENT);
					// The MIME data type filter
					target.setType("*/*");
					// Only return URIs that can be opened with ContentResolver
					target.addCategory(Intent.CATEGORY_OPENABLE);

					List<ResolveInfo> resInfos = currentAct.getPackageManager()
							.queryIntentActivities(target, 0);
					if (!resInfos.isEmpty()) {
						System.out.println("Have package");
						for (ResolveInfo resInfo : resInfos) {
							String packageName = resInfo.activityInfo.packageName;
							Log.i("Package Name", packageName);
							if (packageName.contains("com.goys.android.app")) {
								Intent intent = new Intent();
								intent.setComponent(new ComponentName(
										packageName, resInfo.activityInfo.name));
								intent.setAction(Intent.ACTION_GET_CONTENT);
								intent.setType("*/*");
								// intent.putExtra(Intent.EXTRA_TEXT, "Text");
								// intent.putExtra(Intent.EXTRA_SUBJECT,
								// "Subject");
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setPackage(packageName);
								targetShareIntents.add(intent);
							}
						}
						if (!targetShareIntents.isEmpty()) {
							System.out.println("Have Intent");
							Intent chooserIntent = Intent.createChooser(
									targetShareIntents.remove(0), currentAct
											.getString(R.string.chooser_title));
							chooserIntent.putExtra(
									Intent.EXTRA_INITIAL_INTENTS,
									targetShareIntents
											.toArray(new Parcelable[] {}));
							currentAct.startActivityForResult(chooserIntent,
									AppConstants.PICK_IMAGE);
						} else {
							System.out.println("Do not Have Intent");
							// showDialaog(this);
						}
					}

					
				} else if (items[item].equals(currentAct.getResources()
						.getString(R.string.cancel))) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public static Uri setImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/DCIM/", "image" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		return imgUri;
	}

	public static File getTempFile(Context context) {
		// it will return /sdcard/image.tmp
		final File path = new File(Environment.getExternalStorageDirectory(),
				context.getPackageName());
		if (!path.exists()) {
			path.mkdir();
		}
		return new File(path, "image" + new Date().getTime() + ".png");
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();

		GoysLog.e("camera adap count", "" + listAdapter.getCount());

		if (listAdapter == null || listAdapter.getCount() < 2) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);

			totalHeight += listItem.getMeasuredHeight();
			GoysLog.d("Height", totalHeight + "");
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static boolean isFileExtensionAllowed(Activity ctx,
			String fileExtension) {

		boolean flag = true;

		String unsupportedExt[] = ctx.getResources().getStringArray(
				R.array.unsupportedExentions);

		for (String string : unsupportedExt) {
			if (fileExtension.equalsIgnoreCase(string)) {
				flag = false;
				break;
			}
		}

		return flag;
	}

	public static void showUnsupportedFileError(Activity ctx) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(
				ctx.getResources().getString(R.string.alert_error_title))
				.setMessage(
						ctx.getResources().getString(
								R.string.fileTypeNotSupported))
				.setCancelable(false)
				.setNegativeButton(
						ctx.getResources().getString(R.string.alert_button_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.setView(null);

		AlertDialog alert = builder.create();
		alert.show();
	}

	private static boolean checkCameraFacing(final int facing) {
		if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
			return false;
		}
		final int cameraCount = Camera.getNumberOfCameras();
		CameraInfo info = new CameraInfo();
		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, info);
			if (facing == info.facing) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasBackFacingCamera() {
		final int CAMERA_FACING_BACK = 0;
		return checkCameraFacing(CAMERA_FACING_BACK);
	}

	public static boolean hasFrontFacingCamera() {
		final int CAMERA_FACING_BACK = 1;
		return checkCameraFacing(CAMERA_FACING_BACK);
	}

	public static int getSdkVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static String convertStringToUtf8(String str) {
		String newString = null;
		try {
			newString = new String(str.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newString;
	}
	
	public static void viewFileDialogWithFewOption(final Activity currentAct, final Fragment fr) {

		final CharSequence[] items = {
				currentAct.getResources().getString(R.string.takePicture),
				currentAct.getResources().getString(R.string.picfromGal),
				currentAct.getResources().getString(R.string.cancel) };

		AlertDialog.Builder builder = new AlertDialog.Builder(currentAct);
		builder.setTitle(currentAct.getResources().getString(
				R.string.picker_dialog_title));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				List<Intent> targetShareIntents = new ArrayList<Intent>();

				if (items[item].equals(currentAct.getResources().getString(
						R.string.takePicture))) {
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					fr.startActivityForResult(cameraIntent,
							AppConstants.CAPTURE_IMAGE);

				} else if (items[item].equals(currentAct.getResources()
						.getString(R.string.picfromGal))) {
					// Intent target = FileUtils.createGetContentIntent();

					// Implicitly allow the user to select a particular kind of
					// data
					Intent target = new Intent(Intent.ACTION_GET_CONTENT);
					// The MIME data type filter
					target.setType("*/*");
					// Only return URIs that can be opened with ContentResolver
					target.addCategory(Intent.CATEGORY_OPENABLE);

					List<ResolveInfo> resInfos = currentAct.getPackageManager()
							.queryIntentActivities(target, 0);
					if (!resInfos.isEmpty()) {
						System.out.println("Have package");
						for (ResolveInfo resInfo : resInfos) {
							String packageName = resInfo.activityInfo.packageName;
							Log.i("Package Name", packageName);
							if (packageName.contains("com.goys.android.app")) {
								Intent intent = new Intent();
								intent.setComponent(new ComponentName(
										packageName, resInfo.activityInfo.name));
								intent.setAction(Intent.ACTION_GET_CONTENT);
								intent.setType("*/*");
								// intent.putExtra(Intent.EXTRA_TEXT, "Text");
								// intent.putExtra(Intent.EXTRA_SUBJECT,
								// "Subject");
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setPackage(packageName);
								targetShareIntents.add(intent);
							}
						}
						if (!targetShareIntents.isEmpty()) {
							System.out.println("Have Intent");
							Intent chooserIntent = Intent.createChooser(
									targetShareIntents.remove(0), currentAct
											.getString(R.string.chooser_title));
							chooserIntent.putExtra(
									Intent.EXTRA_INITIAL_INTENTS,
									targetShareIntents
											.toArray(new Parcelable[] {}));
							fr.startActivityForResult(chooserIntent,
									AppConstants.PICK_IMAGE);
						} else {
							System.out.println("Do not Have Intent");
							// showDialaog(this);
						}
					}

					
				} else if (items[item].equals(currentAct.getResources()
						.getString(R.string.cancel))) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	
}
