package com.goys.android.app.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.db.model.MaintenanceClub;
import com.goys.android.app.db.model.MaintenancePublic;
import com.goys.android.app.emigration.Attachment;
import com.goys.android.app.emigration.EmigrationAndVisaActivity;
import com.goys.android.app.emigration.EmigrationFormBean;
import com.goys.android.app.maintenance.MaintenanceFormActivity;
import com.goys.android.app.sportsparticipation.SportsParticipationFormBean;
import com.goys.android.app.sportsparticipation.SportsParticipationsForNationalClubsActivity;

public class GoysService {

	Context context;
	ResponseListener responseListner;
	ProgressDialog dialog;
	protected static final String TAG = "GoysService";

	public GoysService(Context context) {
		super();
		this.context = context;
	}

	public void setResponseListener(ResponseListener listener) {
		this.responseListner = listener;
	}

	public void callEventCalendarService(final Activity ctx, final String language) {

		final String urlfinal = AppConstants.EVENT_CALENDAR_SERVICE + "?language="
				+ language;
		GoysLog.d(TAG, "callNewsService URL " + urlfinal);

		new AsyncTask<String, Void, String>() {

			protected void onPreExecute() {
				// ca.showProgress();
			};

			@Override
			protected String doInBackground(String... params) {

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				String responseBody = null;

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
						GoysLog.d(TAG, "response Body " + responseBody);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return responseBody;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (result != null && !result.contains("Error")) {
					responseListner.onResponse(AppConstants.EVENTCALENDAR_SERVICE_ID,
							result);
				} else {
					responseListner.onError(
							AppConstants.EVENTCALENDAR_SERVICE_ID,
							context.getResources().getString(
									R.string.applicationundermaintenance));
				}
			}

		}.execute(urlfinal);
	}

	
	
	public void callNewsService(final Activity ctx, int startLimit,
			final int endLimit) {

		final String urlfinal = AppConstants.NEWS_SERVICE + "?lower="
				+ startLimit + "&upper=" + endLimit;
		GoysLog.d(TAG, "callEventCalendar URL " + urlfinal);

		new AsyncTask<String, Void, String>() {

			protected void onPreExecute() {
				// ca.showProgress();
			};

			@Override
			protected String doInBackground(String... params) {

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				String responseBody = null;

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
						GoysLog.d(TAG, "response Body " + responseBody);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return responseBody;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (result != null && !result.contains("Error")) {
					responseListner.onResponse(AppConstants.NEWS_SERVICE_ID,
							result);
				} else {
					responseListner.onError(
							AppConstants.NEWS_SERVICE_ID,
							context.getResources().getString(
									R.string.applicationundermaintenance));
				}
			}

		}.execute(urlfinal);
	}

	public void callNewsServiceWithLanguage(final Activity ctx,
			final int startLimit, final int endLimit, final String language) {

		final String urlfinal = AppConstants.NEWS_SERVICE + "?lower="
				+ startLimit + "&upper=" + endLimit + "&language=" + language;
		GoysLog.d(TAG, "callNewsService URL " + urlfinal);

		new AsyncTask<String, Void, String>() {

			protected void onPreExecute() {
				// ca.showProgress();
			};

			@Override
			protected String doInBackground(String... params) {

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				String responseBody = null;

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
						GoysLog.d(TAG, "response Body " + responseBody);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return responseBody;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (result != null && !result.contains("Error")) {
					responseListner.onResponse(AppConstants.NEWS_SERVICE_ID,
							result);
				} else {
					responseListner.onError(
							AppConstants.NEWS_SERVICE_ID,
							context.getResources().getString(
									R.string.applicationundermaintenance));
				}
			}

		}.execute(urlfinal);
	}

	public boolean validatePinCode(final Activity act, final EditText et_pinCode) {

		boolean flag = true;
		if (CommonObjects.isEmpty(et_pinCode.getText().toString())) {
			flag = false;
			et_pinCode.setError(act.getResources().getString(
					(R.string.emptyField)));

		} else if (Pattern.compile(AppConstants.SYMBOLS_PATTERN)
				.matcher(et_pinCode.getText().toString()).find()) {
			flag = false;

			et_pinCode.setError(act.getResources().getString(
					R.string.onlyCharacterAndNumberAllow));

		} else if (et_pinCode.getText().toString().length() < 6) {
			flag = false;
			et_pinCode.setError(act.getResources().getString(
					R.string.invalidePinCodeLength));
		} else if (et_pinCode.getText().toString().length() > 6) {
			flag = false;
			et_pinCode.setError(act.getResources().getString(
					R.string.invalidePinCodeLength));
		} else {
			et_pinCode.setError(null);
		}
		return flag;
	}

	public boolean verifyPinCode(Activity act, final String pinCode) {
		final StringBuilder sb = new StringBuilder();
		boolean flag = false;

		Thread t1 = new Thread(new Runnable() {

			public void run() {
				String urlfinal = AppConstants.VALIDATE_PINCODE_SERVICE_URL
						+ "?id=" + pinCode;
				String responseBody = null;

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(urlfinal);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
						sb.append(responseBody);
						GoysLog.d(TAG, "response Body " + responseBody);

					}
				} catch (Exception e) {
					GoysLog.d(TAG, "Exception in verifyPinCode");
					e.printStackTrace();
				}
			}
		});

		t1.start();
		try {
			GoysLog.d(TAG, "response Body1 " + sb.toString());

			JSONObject obj = new JSONObject(sb.toString());

			flag = obj.getBoolean("Valid");

		} catch (JSONException e) {

			GoysLog.e(TAG, "Errorkk: " + e.toString());
			CommonActions.showErrorAlertDialog(act, 0, act.getResources()
					.getString(R.string.applicationundermaintenance));
		}

		return flag;
	}

	public void pinCodeService(final Activity ctx, final String pinCode) {

		dialog = new ProgressDialog(ctx);
		final String urlfinal = AppConstants.VALIDATE_PINCODE_SERVICE_URL
				+ "?id=" + pinCode;
		GoysLog.d(TAG, "callNewsService URL " + urlfinal);

		new AsyncTask<String, Void, String>() {
			String responseBody = null;

			protected void onPreExecute() {
				dialog.setMessage(ctx.getResources().getString(
						R.string.verifying_pincode_msg));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(String... params) {

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
						GoysLog.d(TAG, "response Body " + responseBody);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return responseBody;
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				try {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				} catch (final IllegalArgumentException e) {

				} catch (final Exception e) {

				} finally {
					dialog = null;
				}

				if (result != null && !result.contains("Error")) {
					try {
						JSONObject obj = new JSONObject(result);

						boolean isValid = obj.getBoolean("Valid");

					} catch (JSONException e) {
						GoysLog.e(TAG, "Error: " + e.toString());
						CommonActions.showErrorAlertDialog(
								ctx,
								0,
								ctx.getResources().getString(
										R.string.applicationundermaintenance));
					}

					responseListner.onResponse(
							AppConstants.VALIDATE_PINCODE_SERVICE_ID, result);
				} else {
					CommonActions.showErrorAlertDialog(
							ctx,
							0,
							ctx.getResources().getString(
									R.string.applicationundermaintenance));
				}
			};

		}.execute(urlfinal);

		System.out.println("HellWordl");
	}

	public void sportParticipationFormService(
			final SportsParticipationsForNationalClubsActivity act,
			SportsParticipationFormBean[] sportParticipationBean) {
		new AsyncTask<SportsParticipationFormBean, Void, String>() {

			protected void onPreExecute() {
				dialog = new ProgressDialog(act);
				dialog.setMessage(act.getResources().getString(
						R.string.submission_in_progress));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(
					SportsParticipationFormBean... params) {
				String responseBody = null;
				SportsParticipationFormBean obj = params[0];

				String url = AppConstants.SPORTS_PARTICIPATION_SERVICE_URL;
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

					try {
						MultipartEntity entity = new MultipartEntity(
								HttpMultipartMode.BROWSER_COMPATIBLE);

						entity.addPart(
								"PinCode",
								new StringBody(obj.getPinCode(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"ClubName",
								new StringBody(obj.getClubName(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"GamesName",
								new StringBody(obj
										.getChampionship_and_gamename(),
										Charset.forName(HTTP.UTF_8)));
						entity.addPart("Notes", new StringBody(obj.getNotes(),
								Charset.forName(HTTP.UTF_8)));

						if (obj.getAttachedFilesForBankAccountDetail()
								.getFilePath() != "") {

							entity.addPart("f", new FileBody(new File(obj
									.getAttachedFilesForBankAccountDetail()
									.getFilePath())));
						}

						if (obj.getAttachedFilesForConsentOfConcerned()
								.getFilePath() != "") {
							entity.addPart("f", new FileBody(new File(obj
									.getAttachedFilesForConsentOfConcerned()
									.getFilePath())));
						}
						
						/*
						if (obj.getAttachedFilesForParticipationBudgetForm()
								.getFilePath() != "") {
							entity.addPart(
									"f",
									new FileBody(
											new File(
													obj.getAttachedFilesForParticipationBudgetForm()
															.getFilePath())));
						}
						*/
						
						if (obj.getAttachedFilesForParticipationCopy()
								.getFilePath() != "") {
							entity.addPart("f", new FileBody(new File(obj
									.getAttachedFilesForParticipationCopy()
									.getFilePath())));
						}

						if (obj.getAttachedFilesForParticipationDate()
								.getFilePath() != "") {
							entity.addPart("f", new FileBody(new File(obj
									.getAttachedFilesForParticipationDate()
									.getFilePath())));
						}

						if (obj.getAttachedFilesForParticipationList()
								.getFilePath() != "") {
							entity.addPart("f", new FileBody(new File(obj
									.getAttachedFilesForParticipationList()
									.getFilePath())));
						}

						if (obj.getAttachedFilesForTournamentList()
								.getFilePath() != "") {
							entity.addPart("f", new FileBody(new File(obj
									.getAttachedFilesForTournamentList()
									.getFilePath())));
						}

						if (obj.getAttachedFilesForQuotationsAndSupporting()
								.getFilePath() != "") {
							entity.addPart(
									"f",
									new FileBody(
											new File(
													obj.getAttachedFilesForQuotationsAndSupporting()
															.getFilePath())));
						}

						httppost.setEntity(entity);
						HttpResponse response = httpclient.execute(httppost);
						int responseCode = response.getStatusLine()
								.getStatusCode();
						HttpEntity http_entity = null;
						switch (responseCode) {
						case 200:
							http_entity = response.getEntity();
							if (entity != null) {
								responseBody = EntityUtils
										.toString(http_entity);
								GoysLog.d(TAG, "response Body " + responseBody);
							}

							break;
						case 202:
							http_entity = response.getEntity();
							if (entity != null) {
								responseBody = EntityUtils
										.toString(http_entity);
								GoysLog.d(TAG, "response Body " + responseBody);
							}

							break;
						case 401:
						case 404:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.serviceNotAvailable));
							break;
						default:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.applicationundermaintenance));
							break;
						}
						GoysLog.d(TAG, "Emmigration Service Response "
								+ response.toString());
					} catch (ClientProtocolException e) {
						GoysLog.d(TAG,
								"ClientProtocolException " + e.getMessage()
										+ " " + e);

					} catch (IOException e) {
						GoysLog.d(TAG, "IOException " + e.getMessage() + " "
								+ e);
					}

				} catch (Exception t) {
					GoysLog.d(TAG, "IOException " + t.getMessage() + " " + t);

					t.printStackTrace();
				}

				return responseBody;
			}

			@Override
			protected void onPostExecute(String data) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();

					responseListner.onResponse(
							AppConstants.SPORTPARTICIPATION_SERVICE_ID, data);

				}
			}
		}.execute(sportParticipationBean);

	}

	public void emmigrationFormService(final EmigrationAndVisaActivity act,
			EmigrationFormBean[] emigrationBean) {
		new AsyncTask<EmigrationFormBean, Void, String>() {

			protected void onPreExecute() {
				dialog = new ProgressDialog(act);
				dialog.setMessage(act.getResources().getString(
						R.string.submission_in_progress));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(EmigrationFormBean... params) {
				String responseBody = null;
				EmigrationFormBean obj = params[0];
				List<Attachment> attachedFiles = obj.getAttachment();
				String url = AppConstants.EMIGRATION_VISA_SERVICE_URL;
				GoysLog.d(TAG, "emmigrationFormService " + url);
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

					try {
						MultipartEntity entity = new MultipartEntity(
								HttpMultipartMode.BROWSER_COMPATIBLE);

						entity.addPart(
								"PinCode",
								new StringBody(obj.getPinCode(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"ClubName",
								new StringBody(obj.getClubName(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart("Email", new StringBody(obj.getEmail(),
								Charset.forName(HTTP.UTF_8)));
						entity.addPart(
								"FirstName",
								new StringBody(obj.getFirstName(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"MiddleName",
								new StringBody(obj.getMiddleName(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"FamilyName",
								new StringBody(obj.getFamilyName(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"CountryID",
								new StringBody(Integer.toString(obj
										.getCountry())));
						entity.addPart(
								"PassportNumber",
								new StringBody(obj.getPassporNumber(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"PersonalNumber",
								new StringBody(obj.getPersonalNumber(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart("Job", new StringBody(obj.getJob(),
								Charset.forName(HTTP.UTF_8)));
						entity.addPart("RequestType",
								new StringBody(obj.getSelectedRequestType(),
										Charset.forName(HTTP.UTF_8)));
						entity.addPart("RadioChoice",
								new StringBody(obj.getSelectedRequestType(),
										Charset.forName(HTTP.UTF_8)));
						entity.addPart("Notes",
								new StringBody("", Charset.forName(HTTP.UTF_8)));

						if (attachedFiles != null && attachedFiles.size() > 0) {
							for (Attachment attachment : attachedFiles)
								{
								File f = new File(attachment.getFilePath());
								entity.addPart("f", new FileBody(f));
							}
						}

						httppost.setEntity(entity);
						HttpResponse response = httpclient.execute(httppost);
						int responseCode = response.getStatusLine()
								.getStatusCode();
						switch (responseCode) {
						case 200:
						case 202:
							HttpEntity http_entity = response.getEntity();
							if (entity != null) {
								responseBody = EntityUtils
										.toString(http_entity);
								Log.d(TAG, "response Body " + responseBody);
							}
							break;
						case 401:
						case 404:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.serviceNotAvailable));
							break;
						default:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.applicationundermaintenance));
							break;
						}
						GoysLog.d(TAG, "Emmigration Service Response "
								+ response.toString());
					} catch (ClientProtocolException e) {
						GoysLog.d(TAG,
								"ClientProtocolException " + e.getMessage());
					} catch (IOException e) {
						GoysLog.d(TAG, "IOException " + e.getMessage());
					}

				} catch (Exception t) {
					t.printStackTrace();
				}

				return responseBody;
			}

			@Override
			protected void onPostExecute(final String data) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();

					responseListner.onResponse(
							AppConstants.EMIGRATIONANDVISA_SERVICE_ID, data);

				}
			}

		}.execute(emigrationBean);
	}

	public void maintenancePinCodeService(final Activity ctx,
			final String pinCode) {

		dialog = new ProgressDialog(ctx);
		final String urlfinal = AppConstants.MAINTENANCE_VALIDATE_PINCODE_SERVICE_URL
				+ "?id=" + pinCode;
		GoysLog.d(TAG, "maintenanceValidatePincode URL " + urlfinal);

		new AsyncTask<String, Void, String>() {
			String responseBody = null;

			protected void onPreExecute() {
				dialog.setMessage(ctx.getResources().getString(
						R.string.verifying_pincode_msg));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(String... params) {

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
						GoysLog.d(TAG, "response Body " + responseBody);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return responseBody;
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				/*
				 * if(ctx.isDestroyed()){ return; }
				 */

				try {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				} catch (final IllegalArgumentException e) {

				} catch (final Exception e) {

				} finally {
					dialog = null;
				}

				if (result != null && !result.contains("Error")) {
					try {
						JSONArray jsonArray = new JSONArray(result);
						JSONObject obj = new JSONObject();
						obj = jsonArray.getJSONObject(0);

						boolean isValid = obj.getBoolean("Valid");

					} catch (JSONException e) {
						GoysLog.e(TAG, "Error: " + e.toString());
						CommonActions.showErrorAlertDialog(
								ctx,
								0,
								ctx.getResources().getString(
										R.string.applicationundermaintenance));
					}

					responseListner
							.onResponse(
									AppConstants.MAINTENANCE_VALIDATE_PINCODE_SERVICE_ID,
									result);
				} else {
					CommonActions.showErrorAlertDialog(
							ctx,
							0,
							ctx.getResources().getString(
									R.string.applicationundermaintenance));
				}
			};

		}.execute(urlfinal);

	}

	public void registerPushNotification(final Activity ctx,
			final String regID, final String platform, final String Language) {

		final String urlfinal = Uri
				.parse(AppConstants.REGISTER_PUSH_NOTIFICATION_URL).buildUpon()
				.appendQueryParameter("Language", Language)
				.appendQueryParameter("Platform", platform)
				.appendQueryParameter("RegID", regID).build().toString();

		GoysLog.d(TAG, "Register for Push Notification" + urlfinal);

		new AsyncTask<String, Void, String>() {
			String responseBody = null;

			protected void onPreExecute() {

			}

			@Override
			protected String doInBackground(String... params) {

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return responseBody;
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				if (result != null && !result.contains("Error")) {
					Log.d("response", result);

					responseListner.onResponse(
							AppConstants.REGISTER_PUSH_NOTIFICATION_ID, result);
				} else {
					CommonActions.showErrorAlertDialog(
							ctx,
							0,
							ctx.getResources().getString(
									R.string.applicationundermaintenance));
				}
			};

		}.execute(urlfinal);

	}

	public void getServiceTypeService1(final Activity ctx,
			final String facilityName, final String maintenanceType) {

		dialog = new ProgressDialog(ctx);

		final String lang = GOYSApplication.getInstance().isEnglishLanguage() ? "en"
				: "ar";

		final String urlfinal = Uri
				.parse(AppConstants.MAINTENANCE_GET_SERVICE_TYPE_URL)
				.buildUpon().build().toString();

		GoysLog.d(TAG, "getServiceTypeService URL " + urlfinal);

		new AsyncTask<String, Void, String>() {
			String responseBody = null;

			protected void onPreExecute() {
				dialog.setMessage(ctx.getResources().getString(
						R.string.lbl_fetching_service_types));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(String... params) {

				JSONObject json = new JSONObject();
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				HttpResponse response = null;

				try {
					json.put("FacilitiesName", facilityName);
					json.put("MaintenanceType", maintenanceType);
					json.put("Language", lang);

					StringEntity se = new StringEntity(json.toString(), "UTF-8");
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					httpPost.setEntity(se);

					response = httpClient.execute(httpPost);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				GoysLog.d(TAG, responseBody);
				
				return responseBody;
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				try {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				} catch (final IllegalArgumentException e) {

				} catch (final Exception e) {

				} finally {
					dialog = null;
				}

				if (result != null && !result.contains("Error")) {
					try {

						JSONArray jsonArray = new JSONArray(result);
						for (int i = 0; i < jsonArray.length(); i++) {
							Log.d("JsonARRAY", jsonArray.getString(i));
						}

					} catch (JSONException e) {
						GoysLog.e(TAG, "Error: " + e.toString());

					}

					responseListner.onResponse(
							AppConstants.MAINTENANCE_GET_SERVICE_TYPE_ID,
							result);
				} else {
					CommonActions.showErrorAlertDialog(
							ctx,
							0,
							ctx.getResources().getString(
									R.string.applicationundermaintenance));
				}
			};

		}.execute(urlfinal);

	}

	public void getFacilitiesByServiceType(final Activity ctx,
			final String serviceType) {

		dialog = new ProgressDialog(ctx);

		final String lang = GOYSApplication.getInstance().isEnglishLanguage() ? "en"
				: "ar";

		final String urlfinal = Uri.parse(AppConstants.MAINTENANCE_GET_FACILITIES_BY_SERVICE_TYPE_URL).buildUpon().build().toString();

		GoysLog.d(TAG, "getFacilitiesByServiceType URL " + urlfinal);

		new AsyncTask<String, Void, String>() {
			String responseBody = null;

			protected void onPreExecute() {
				dialog.setMessage(ctx.getResources().getString(
						R.string.lbl_fetching_facilities));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(String... params) {

				JSONObject json = new JSONObject();
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);
				HttpResponse response = null;

				try {
					json.put("serviceType", serviceType);
					json.put("Language", lang);

					StringEntity se = new StringEntity(json.toString(), "UTF-8");
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					httpPost.setEntity(se);

					response = httpClient.execute(httpPost);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return responseBody;
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				try {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				} catch (final IllegalArgumentException e) {

				} catch (final Exception e) {

				} finally {
					dialog = null;
				}

				if (result != null && !result.contains("Error")) {
					try {

						JSONArray jsonArray = new JSONArray(result);
						for (int i = 0; i < jsonArray.length(); i++) {
							Log.d("JsonARRAY", jsonArray.getString(i));
						}

					} catch (JSONException e) {
						GoysLog.e(TAG, "Error: " + e.toString());

					}

					responseListner
							.onResponse(
									AppConstants.MAINTENANCE_GET_FACILITIES_BY_SERVICE_TYPE,
									result);
				} else {
					CommonActions.showErrorAlertDialog(
							ctx,
							0,
							ctx.getResources().getString(
									R.string.applicationundermaintenance));
				}
			};

		}.execute(urlfinal);

	}

	public void registerPushNotification1(final Activity ctx,
			final String regID, final String platform, final String Language) {

		Thread t = new Thread() {

			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						100000); // Timeout Limit
				HttpResponse response;
				JSONObject json = new JSONObject();
				Boolean isSubmitted = false;
				String responseBody = null;

				try {
					HttpPost post = new HttpPost(
							AppConstants.REGISTER_PUSH_NOTIFICATION_URL);
					json.put("RegID", regID);
					json.put("Platform", platform);
					json.put("Language", Language);

					StringEntity se = new StringEntity(json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					response = client.execute(post);

					/* Checking response */
					if (response != null) {
						// InputStream in = response.getEntity().getContent();
						// //Get the data in the entity

					}

					HttpEntity entity = response.getEntity();
					if (entity != null) {
						isSubmitted = Boolean.parseBoolean(EntityUtils
								.toString(entity));
						GoysLog.d(TAG, "response Body " + isSubmitted + "");
					}

					responseListner.onResponse(
							AppConstants.REGISTER_PUSH_NOTIFICATION_ID,
							String.valueOf(isSubmitted));

				} catch (Exception e) {
					e.printStackTrace();
				}

				Looper.loop(); // Loop in the message queue
			}
		};

		t.start();

	}

	public void MaintenancePublicFormService(final MaintenanceFormActivity act,
			MaintenancePublic[] maintenancePublicBean) {
		new AsyncTask<MaintenancePublic, Void, String>() {

			final String lang = GOYSApplication.getInstance().isEnglishLanguage() ? "en"
					: "ar";
			
			protected void onPreExecute() {
				dialog = new ProgressDialog(act);
				dialog.setMessage(act.getResources().getString(
						R.string.submission_in_progress));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(MaintenancePublic... params) {
				String responseBody = null;
				MaintenancePublic obj = params[0];

				if (obj != null) {
					Log.d("MaintenancePublic Obj ", obj.toString());
				}
				List<Attachment> attachedFiles = obj.getAttachment();
				String url = AppConstants.MAINTENANCE_SERVICE_URL;
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

					try {
						MultipartEntity entity = new MultipartEntity(
								HttpMultipartMode.BROWSER_COMPATIBLE);

						entity.addPart(
								"ServiceType",
								new StringBody(obj.getServiceType(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart("RequestType", new StringBody("Public",
								Charset.forName(HTTP.UTF_8)));

						entity.addPart(
								"JobDescription",
								new StringBody(obj.getJobDescription(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart(
								"FacilitiesName",
								new StringBody(obj.getFacilityName(), Charset
										.forName(HTTP.UTF_8)));

						/*entity.addPart(
								"MaintenanceType",
								new StringBody(act.getResources().getString(
										R.string.text_emergency), Charset
										.forName(HTTP.UTF_8)));
						 */
						entity.addPart(
								"MaintenanceType",
								new StringBody(obj.getMaintenanceType(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart(
								"PlaceRequiredMaintenance",
								new StringBody(obj
										.getPlaceRequiredMaintenance(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart(
								"Location",
								new StringBody(obj.getLocationType(), Charset
										.forName(HTTP.UTF_8)));
						// obj.getBuildingNo()+","+obj.getRoadNo()+","+obj.getBlockNo()+","+obj.getArea()+","+obj.getGovernorate()
						entity.addPart("Email", new StringBody(obj.getEmail(),
								Charset.forName(HTTP.UTF_8)));

						entity.addPart(
								"Mobile",
								new StringBody(obj.getMobileNo(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart(
								"BuildingNo",
								new StringBody(obj.getBuildingNo(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart("RoadNo", new StringBody(
								obj.getRoadNo(), Charset.forName(HTTP.UTF_8)));

						entity.addPart(
								"BlockNo",
								new StringBody(obj.getBlockNo(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart("Area", new StringBody(obj.getArea(),
								Charset.forName(HTTP.UTF_8)));

						entity.addPart(
								"Governorate",
								new StringBody(obj.getGovernorate(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart("Date", new StringBody(obj.getDate(),
								Charset.forName(HTTP.UTF_8)));

						entity.addPart("Language", new StringBody(lang));
						
/*						entity.addPart("Contractor", new StringBody(""));

						entity.addPart("Remarks", new StringBody(""));

						entity.addPart("PinCode", new StringBody(""));*/

						if (attachedFiles != null && attachedFiles.size() > 0) {
							for (Attachment attachment : attachedFiles) {
								File f = new File(attachment.getFilePath());
								entity.addPart("f", new FileBody(f));
							}
						}

						
						 /* java.io.ByteArrayOutputStream out = new
						  java.io.ByteArrayOutputStream( (int)
						  entity.getContentLength()); entity.writeTo(out);
						  String entityContentAsString = new
						  String(out.toByteArray()); Log.e("multipartEntitty:",
						  "" + entityContentAsString);
						 */

						httppost.setEntity(entity);
						HttpResponse response = httpclient.execute(httppost);
						int responseCode = response.getStatusLine()
								.getStatusCode();
						
						Log.d(TAG, "response Code " + responseCode);
						
						switch (responseCode) {
						case 200:
						case 202:
							HttpEntity http_entity = response.getEntity();
							if (entity != null) {
								responseBody = EntityUtils
										.toString(http_entity);
								Log.d(TAG, "response Body " + responseBody);
							}
							break;
						case 401:
						case 404:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.serviceNotAvailable));
							break;
						default:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.applicationundermaintenance));
							break;
						}
						GoysLog.d(TAG, "Maintenance Public Service Response "
								+ response.toString());
					} catch (ClientProtocolException e) {
						GoysLog.d(TAG,
								"ClientProtocolException " + e.getMessage());
					} catch (IOException e) {
						GoysLog.d(TAG, "IOException " + e.getMessage());
					}

				} catch (Exception t) {
					t.printStackTrace();
				}

				return responseBody;
			}

			@Override
			protected void onPostExecute(String data) {
				try {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();

						responseListner.onResponse(
								AppConstants.MAINTENANCE_PUBLIC_SERVICE_ID,
								data);

					}
				} catch (final IllegalArgumentException e) {

				} catch (final Exception e) {

				} finally {
					dialog = null;
				}

			}

		}.execute(maintenancePublicBean);
	}

	public void MaintenanceClubFormService(final MaintenanceFormActivity act,
			MaintenanceClub[] maintenanceClubBean) {
		new AsyncTask<MaintenanceClub, Void, String>() {

			final String lang = GOYSApplication.getInstance().isEnglishLanguage() ? "en"
					: "ar";
			
			protected void onPreExecute() {
				dialog = new ProgressDialog(act);
				dialog.setMessage(act.getResources().getString(
						R.string.submission_in_progress));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(MaintenanceClub... params) {
				String responseBody = null;
				MaintenanceClub obj = params[0];
				List<Attachment> attachedFiles = obj.getAttachment();
				String url = AppConstants.MAINTENANCE_SERVICE_URL;
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

					try {
						MultipartEntity entity = new MultipartEntity(
								HttpMultipartMode.BROWSER_COMPATIBLE);

						entity.addPart(
								"PinCode",
								new StringBody(obj.getPinCode(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"FacilitiesName",
								new StringBody(obj.getFacilityName(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart("Email", new StringBody(obj.getEmail(),
								Charset.forName(HTTP.UTF_8)));
						entity.addPart("MaintenanceType",
								new StringBody(obj.getMaintenanceType(),
										Charset.forName(HTTP.UTF_8)));
						entity.addPart("RequestType", new StringBody(
								"Internal", Charset.forName(HTTP.UTF_8)));

						entity.addPart(
								"ServiceType",
								new StringBody(obj.getServiceType(), Charset
										.forName(HTTP.UTF_8)));
						entity.addPart(
								"JobDescription",
								new StringBody(obj.getJobDescription(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart(
								"PlaceRequiredMaintenance",
								new StringBody(obj
										.getPlaceRequiredMaintenance(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart(
								"Location",
								new StringBody(obj.getLocationType(), Charset
										.forName(HTTP.UTF_8)));
						// obj.getBuildingNo()+","+obj.getRoadNo()+","+obj.getBlockNo()+","+obj.getArea()+","+obj.getGovernorate()
						entity.addPart(
								"BuildingNo",
								new StringBody(obj.getBuildingNo(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart("RoadNo", new StringBody(
								obj.getRoadNo(), Charset.forName(HTTP.UTF_8)));

						entity.addPart(
								"BlockNo",
								new StringBody(obj.getBlockNo(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart("Area", new StringBody(obj.getArea(),
								Charset.forName(HTTP.UTF_8)));

						entity.addPart(
								"Governorate",
								new StringBody(obj.getGovernorate(), Charset
										.forName(HTTP.UTF_8)));

						entity.addPart("Date", new StringBody(obj.getDate(),
								Charset.forName(HTTP.UTF_8)));
						
						entity.addPart("Language", new StringBody(lang));

						if (attachedFiles != null && attachedFiles.size() > 0) {
							for (Attachment attachment : attachedFiles) {
								File f = new File(attachment.getFilePath());
								entity.addPart("f", new FileBody(f));
							}
						}

						 /*java.io.ByteArrayOutputStream out = new
								  java.io.ByteArrayOutputStream( (int)
								  entity.getContentLength()); entity.writeTo(out);
								  String entityContentAsString = new
								  String(out.toByteArray()); Log.e("multipartEntitty:",
								  "" + entityContentAsString);
								*/ 
						
						httppost.setEntity(entity);
						HttpResponse response = httpclient.execute(httppost);
						int responseCode = response.getStatusLine()
								.getStatusCode();
						switch (responseCode) {
						case 200:
						case 202:
							HttpEntity http_entity = response.getEntity();
							Log.d(TAG, "entity " + response.getEntity());
							if (entity != null) {
								responseBody = EntityUtils
										.toString(http_entity);
								Log.d(TAG, "response Body " + responseBody);
							}
							break;
						case 401:
						case 404:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.serviceNotAvailable));
							break;
						default:
							CommonActions
									.alertDialogShow(
											act,
											act.getResources().getString(
													R.string.note),
											act.getResources()
													.getString(
															R.string.applicationundermaintenance));
							break;
						}
						GoysLog.d(TAG, "Maintenance Club Service Response "
								+ response.toString());
					} catch (ClientProtocolException e) {
						GoysLog.d(TAG,
								"ClientProtocolException " + e.getMessage());
					} catch (IOException e) {
						GoysLog.d(TAG, "IOException " + e.getMessage());
					}

				} catch (Exception t) {
					t.printStackTrace();
				}

				return responseBody;
			}

			@Override
			protected void onPostExecute(String data) {
				try {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();

						responseListner.onResponse(
								AppConstants.MAINTENANCE_CLUB_SERVICE_ID, data);

					}
				} catch (final IllegalArgumentException e) {

				} catch (final Exception e) {

				} finally {
					dialog = null;
				}

			}

		}.execute(maintenanceClubBean);
	}

	public void getLocationService(final Activity ctx, final String latitude,
			final String longitude) {

		dialog = new ProgressDialog(ctx);

		final String urlfinal = Uri
				.parse(AppConstants.MAINTENANCE_GET_LOCATION_SERVICE_URL)
				.buildUpon().appendQueryParameter("x", latitude)
				.appendQueryParameter("y", longitude).build().toString();

		GoysLog.d(TAG, "getLocationService URL " + urlfinal);

		new AsyncTask<String, Void, String>() {
			String responseBody = null;

			protected void onPreExecute() {
				dialog.setMessage(ctx.getResources().getString(
						R.string.lbl_fetching_location));
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected String doInBackground(String... params) {

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(params[0]);
				HttpResponse response = null;
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				GoysLog.d(TAG, urlfinal);

				try {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						responseBody = EntityUtils.toString(entity);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return responseBody;
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				try {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				} catch (IllegalArgumentException e) {

				} catch (Exception e) {

				} finally {
					dialog = null;
				}

				if (result != null && !result.contains("Error")) {
					try {
						JSONObject obj = new JSONObject(result);

					} catch (JSONException e) {
						GoysLog.e(TAG, "Error: " + e.toString());
						CommonActions.showErrorAlertDialog(
								ctx,
								0,
								ctx.getResources().getString(
										R.string.applicationundermaintenance));
					}

					responseListner.onResponse(
							AppConstants.MAINTENANCE_GET_LOCATION_SERVICE_ID,
							result);
				} else {
					CommonActions.showErrorAlertDialog(
							ctx,
							0,
							ctx.getResources().getString(
									R.string.applicationundermaintenance));
				}
			};

		}.execute(urlfinal);

	}

}
