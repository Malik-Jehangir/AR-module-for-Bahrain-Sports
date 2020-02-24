package com.goys.android.app.emigration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.andorid.components.afilechooser.utils.FileUtils;
import com.android.afilechooser.AttachementListArrayAdapter;
import com.goys.android.app.ActionbarActivity;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.db.model.Country;
import com.goys.android.app.sportsparticipation.SportsParticipationsForNationalClubsActivity;
import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.CommonObjects;
import com.goys.android.app.util.GoysLog;
import com.goys.android.app.util.GoysService;
import com.goys.android.app.util.ImageServices;
import com.goys.android.app.util.KeyValueSpinner;
import com.goys.android.app.util.MyAttachmentAdapter;
import com.goys.android.app.util.ResponseListener;

/*
 * 
 * This class represents Emigration Form page screen
 * 
 */
public class EmigrationAndVisaActivity extends ActionbarActivity implements
		ResponseListener,OnTouchListener {
	private static final String TAG = "ActivityEmigrationAndVisa";

	Button btn_submit;
	Button btn_chooseFile;
	EditText et_pinCode;
	EditText et_clubName;
	EditText et_email;
	EditText et_firstName;
	EditText et_middleName;
	EditText et_familyName;
	EditText et_job;
	EditText et_passportNumber;
	EditText et_personalNumber;
	RadioGroup rg_residenciesRadio;
	RadioGroup rg_visasRadio;
	RadioGroup rg_changeJobTitleRadios;
	RadioGroup rg_changeSponsorRadios;
	EditText et_other_option;
	RadioButton rb_residencies;
	RadioButton rb_visas;
	RadioButton rb_changeJobTitle;
	RadioButton rb_changeSponsor;
	Spinner spin_country;
	Spinner spin_requestType;
	ListView lv_attached_files;
	String imgPath;

	String requestTypeValue;
	ArrayList<Country> alCountries;
	/* EmigrationFormBean */
	EmigrationFormBean formBean;
	EmigrationFormBean[] formBeanList;
	Button btn_clear;
	TextView attachment_label;
	private String selectedImagePath = "";
	GoysService gs;
	Boolean hasFocusChanged;

	String tempPinCode = "";

	Uri imageUri;
	/* Attached Files ListView */
	public static ArrayList<String> attachedFiles;

	AttachementListArrayAdapter adapter;

	MyAttachmentAdapter myAdapter;

	private ArrayList<Attachment> attachmentList;

	ProgressDialog dialog;

	Boolean isActivityAvailable;

	@Override
	protected void onPause() {
		super.onPause();
		isActivityAvailable = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityAvailable = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (!GOYSApplication.getInstance().isEnglishLanguage()) {
			GOYSApplication.appLanguage = "ar";
			GOYSApplication.getInstance().changeLocale(
					GOYSApplication.appLanguage);
		}

		setActionbarTitle(getResources().getString(
				R.string.menu_emigration_visa));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_red));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emigration_visa_national_club);
		GoysLog.d(TAG, TAG + " onCreate");

		gs = new GoysService(this);
		gs.setResponseListener(this);

		initializeView();

		/*
		 * handling saveInstanceState for less then OS 4.4 where capture image
		 * intent call caller activity onCreate after take picture
		 */
		if (savedInstanceState == null) {
			attachmentList = new ArrayList<Attachment>();
		} else {
			String fileUri = savedInstanceState.getString("file-uri");
			if (!fileUri.equals("")) {
				imageUri = Uri.parse(fileUri);
			}
			attachmentList = savedInstanceState
					.getParcelableArrayList("attached_files");

			myAdapter = new MyAttachmentAdapter(lv_attached_files, this, 0,
					attachmentList);
			lv_attached_files.setAdapter(myAdapter);

		}

		alCountries = CommonObjects.getCountriesList();

		final KeyValueSpinner adapter = new KeyValueSpinner(
				EmigrationAndVisaActivity.this, alCountries);
		spin_country.setAdapter(adapter);

		btn_chooseFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewFileDialogWithFewOption();
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				formBeanList = new EmigrationFormBean[2];
				if (formBean == null) {
					formBean = new EmigrationFormBean();
				}
				formBean.setEmail(et_email.getText().toString());
				formBean.setPinCode(et_pinCode.getText().toString());
				formBean.setClubName(et_clubName.getText().toString());
				formBean.setFirstName(et_firstName.getText().toString());
				formBean.setMiddleName(et_middleName.getText().toString());
				formBean.setFamilyName(et_familyName.getText().toString());
				formBean.setJob(et_job.getText().toString());
				formBean.setPassporNumber(et_passportNumber.getText()
						.toString());
				formBean.setPersonalNumber(et_personalNumber.getText()
						.toString());

				requestTypeValue = getRadioSelectedValue();
				formBean.setSelectedRequestType(requestTypeValue);

				/* adding attachment list into formBean */
				if (attachmentList.size() > 0) {
					formBean.setAttachment(attachmentList);
				}

				formBean.setCountry(adapter.getIDFromIndex(spin_country
						.getSelectedItemPosition()));

				if (validateForm(formBean)) {

					if (CommonActions
							.hasConnection(EmigrationAndVisaActivity.this)) {

						boolean flag = gs.validatePinCode(
								EmigrationAndVisaActivity.this, et_pinCode);

						if (flag) {

							/*
							 * finally checking the pinCode again for if user
							 * input wrong value for pinCode after verify
							 * pincode
							 */
							if (tempPinCode.equals(et_pinCode.getText()
									.toString())) {

								formBeanList[0] = formBean;
								gs.emmigrationFormService(
										EmigrationAndVisaActivity.this,
										formBeanList);
							} else {

								gs.pinCodeService(
										EmigrationAndVisaActivity.this,
										et_pinCode.getText().toString());
							}

						} else {
							et_pinCode.setError(getResources().getString(
									(R.string.invalidePinCode)));
							et_clubName.setText("");
							et_email.setText("");
						}
					} else {
						CommonActions.alertDialogShow(
								EmigrationAndVisaActivity.this,
								getResources().getString(
										R.string.alert_error_title),
								getResources().getString(
										R.string.networkConnectionRequired));

					}

				}

			}
		});
	}

	public void setSpinnerAdaptor(Spinner spinner, String[] string) {
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, R.layout.item_spinner, string);
		spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
		spinner.setAdapter(spinnerArrayAdapter);

	}

	private void initializeView() {
		btn_submit = (Button) this.findViewById(R.id.submit);
		et_pinCode = (EditText) this.findViewById(R.id.pinCode);
		et_clubName = (EditText) this.findViewById(R.id.clubName);
		et_email = (EditText) this.findViewById(R.id.email);
		et_firstName = (EditText) this.findViewById(R.id.firstName);
		et_middleName = (EditText) this.findViewById(R.id.middleName);
		et_familyName = (EditText) this.findViewById(R.id.familyName);
		et_job = (EditText) this.findViewById(R.id.job);
		et_passportNumber = (EditText) this.findViewById(R.id.passportNumber);
		et_personalNumber = (EditText) this.findViewById(R.id.personalNumber);
		// et_comments = (EditText) this.findViewById(R.id.comments);
		spin_country = (Spinner) this.findViewById(R.id.country);
		spin_country.setOnTouchListener(this);
		spin_requestType = (Spinner) this.findViewById(R.id.request_type);
		spin_requestType.setOnTouchListener(this);
		setSpinnerAdaptor(spin_requestType,
				getResources().getStringArray(R.array.request_type_arrays));

		rg_residenciesRadio = (RadioGroup) this
				.findViewById(R.id.residenciesRadio);
		rg_visasRadio = (RadioGroup) this.findViewById(R.id.visasRadio);
		rg_changeJobTitleRadios = (RadioGroup) this
				.findViewById(R.id.changeJobTitleRadios);
		rg_changeSponsorRadios = (RadioGroup) this
				.findViewById(R.id.changeSponsorRadios);
		et_other_option = (EditText) this.findViewById(R.id.other_option);
		lv_attached_files = (ListView) this
				.findViewById(R.id.attached_files_listview);
		btn_chooseFile = (Button) this.findViewById(R.id.chooseFile);
		btn_clear = (Button) this.findViewById(R.id.clear);
		attachment_label = (TextView) findViewById(R.id.label_attachments);
		btn_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearForm();
			}
		});

		et_pinCode.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {
					if (gs.validatePinCode(EmigrationAndVisaActivity.this,
							et_pinCode)) {

						if (CommonActions
								.hasConnection(EmigrationAndVisaActivity.this)) {

							gs.pinCodeService(EmigrationAndVisaActivity.this,
									et_pinCode.getText().toString());
						} else {
							CommonActions
									.alertDialogShow(
											EmigrationAndVisaActivity.this,
											getResources().getString(
													R.string.alert_error_title),
											getResources()
													.getString(
															R.string.networkConnectionRequired));
						}
					} else {
						et_pinCode.post(new Runnable() {
							@Override
							public void run() {
								et_pinCode.requestFocus();
							}
						});
					}
				}
			}
		});

		spin_country.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				if (CommonObjects.getCountriesList()
						.get(spin_country.getSelectedItemPosition()).getId() == 297) {
					// india country id 297
					CommonActions.alertDialogShow(
							EmigrationAndVisaActivity.this, getResources()
									.getString(R.string.note), getResources()
									.getString(R.string.india_countrysel_msg));
				}
				if (CommonObjects.getCountriesList()
						.get(spin_country.getSelectedItemPosition()).getId() == 225) {
					// bangladesh country id 225
					CommonActions.alertDialogShow(
							EmigrationAndVisaActivity.this,
							getResources().getString(R.string.note),
							getResources().getString(
									R.string.bangladesh_countrysel_msg));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});

		spin_requestType
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						requestTypeValue = spin_requestType.getSelectedItem()
								.toString();
						System.out
								.println("onItemSelected " + requestTypeValue);

						if (requestTypeValue.equals(getResources().getString(
								R.string.request_type_residence))) {

							((TextView) findViewById(R.id.text_residencies))
									.setText(getResources().getString(
											R.string.request_type_residence));
							((RadioGroup) findViewById(R.id.residenciesRadio))
									.setVisibility(View.VISIBLE);
							((RadioGroup) findViewById(R.id.visasRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeJobTitleRadios))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeSponsorRadios))
									.setVisibility(View.GONE);
							((EditText) findViewById(R.id.other_option))
									.setVisibility(View.GONE);
							((TextView) findViewById(R.id.label_condition_to_be_met))
									.setVisibility(View.GONE);
						}

						if (requestTypeValue.equals(getResources().getString(
								R.string.request_type_visas))) {

							((TextView) findViewById(R.id.text_residencies))
									.setText(getResources().getString(
											R.string.request_type_visas));

							((RadioGroup) findViewById(R.id.residenciesRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.visasRadio))
									.setVisibility(View.VISIBLE);
							((RadioGroup) findViewById(R.id.changeJobTitleRadios))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeSponsorRadios))
									.setVisibility(View.GONE);
							((EditText) findViewById(R.id.other_option))
									.setVisibility(View.GONE);
							((TextView) findViewById(R.id.label_condition_to_be_met))
									.setVisibility(View.VISIBLE);

						}

						if (requestTypeValue.equals(getResources().getString(
								R.string.request_type_jobtitle))) {

							((TextView) findViewById(R.id.text_residencies))
									.setText(getResources().getString(
											R.string.request_type_jobtitle));

							((RadioGroup) findViewById(R.id.residenciesRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.visasRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeJobTitleRadios))
									.setVisibility(View.VISIBLE);
							((RadioGroup) findViewById(R.id.changeSponsorRadios))
									.setVisibility(View.GONE);
							((EditText) findViewById(R.id.other_option))
									.setVisibility(View.GONE);
							((TextView) findViewById(R.id.label_condition_to_be_met))
									.setVisibility(View.GONE);
						}

						if (requestTypeValue.equals(getResources().getString(
								R.string.request_type_sponsorship))) {

							((TextView) findViewById(R.id.text_residencies))
									.setText(getResources().getString(
											R.string.request_type_sponsorship));

							((RadioGroup) findViewById(R.id.residenciesRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.visasRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeJobTitleRadios))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeSponsorRadios))
									.setVisibility(View.VISIBLE);
							((EditText) findViewById(R.id.other_option))
									.setVisibility(View.GONE);
							((TextView) findViewById(R.id.label_condition_to_be_met))
									.setVisibility(View.GONE);
						}

						if (requestTypeValue.equals(getResources().getString(
								R.string.request_type_other))) {

							((TextView) findViewById(R.id.text_residencies))
									.setText(getResources().getString(
											R.string.request_type_other));

							((RadioGroup) findViewById(R.id.residenciesRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.visasRadio))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeJobTitleRadios))
									.setVisibility(View.GONE);
							((RadioGroup) findViewById(R.id.changeSponsorRadios))
									.setVisibility(View.GONE);
							((EditText) findViewById(R.id.other_option))
									.setVisibility(View.VISIBLE);
							((TextView) findViewById(R.id.label_condition_to_be_met))
									.setVisibility(View.GONE);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						System.out.println("onNothingSelected");
					}
				});
	}

	private boolean validateForm(EmigrationFormBean bean) {
		boolean validate = true;

		Pattern regex = Pattern.compile(AppConstants.SPECIAL_CHARACTER_PATTERN);

		if (!CommonObjects.isEmpty(bean.getPersonalNumber())
				&& bean.getPersonalNumber().length() != 9) {

			validate = false;
			et_personalNumber.requestFocus();
			et_personalNumber.setError(getResources().getString(
					R.string.invalid_number_lenght));
		}

		if (CommonObjects.isEmpty(bean.getPassporNumber())) {
			et_passportNumber.requestFocus();
			et_passportNumber.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;

		} else if (Pattern.compile(AppConstants.SYMBOLS_PATTERN)
				.matcher(bean.getPassporNumber()).find()) {
			validate = false;
			et_passportNumber.requestFocus();
			et_passportNumber.setError(getResources().getString(
					R.string.onlyCharacterAndNumberAllow));
		}

		if (CommonObjects.isEmpty(bean.getJob())) {
			et_job.requestFocus();
			et_job.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else {
			et_job.setError(null);
		}

		if (CommonObjects.isEmpty(bean.getFamilyName())) {
			et_familyName.requestFocus();
			et_familyName.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else if (Pattern.compile(AppConstants.ALPHA_PATTERN)
				.matcher(bean.getFamilyName()).find()) {
			validate = false;
			et_familyName.requestFocus();
			et_familyName.setError(getResources().getString(
					R.string.onlyCharacterAllow));
		} else {
			et_familyName.setError(null);
		}

		if (CommonObjects.isEmpty(bean.getMiddleName())) {
			et_middleName.requestFocus();
			et_middleName.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else if (Pattern.compile(AppConstants.ALPHA_PATTERN)
				.matcher(bean.getMiddleName()).find()) {
			validate = false;
			et_middleName.requestFocus();
			et_middleName.setError(getResources().getString(
					R.string.onlyCharacterAllow));
		} else {
			et_middleName.setError(null);
		}

		if (CommonObjects.isEmpty(bean.getFirstName())) {
			et_firstName.requestFocus();
			et_firstName.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else if (Pattern.compile(AppConstants.ALPHA_PATTERN)
				.matcher(bean.getFirstName()).find()) {
			validate = false;
			et_firstName.requestFocus();
			et_firstName.setError(getResources().getString(
					R.string.onlyCharacterAllow));
		}

		else {
			et_firstName.setError(null);
		}

		if (CommonObjects.isEmpty(bean.getPinCode())) {
			et_pinCode.requestFocus();
			et_pinCode
					.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else if (Pattern.compile(AppConstants.SYMBOLS_PATTERN)
				.matcher(bean.getPinCode()).find()) {
			validate = false;
			et_pinCode.requestFocus();
			et_pinCode.setError(getResources().getString(
					R.string.onlyCharacterAndNumberAllow));
		} else if (bean.getPinCode().length() < 6) {
			et_pinCode.requestFocus();
			et_pinCode.setError(getResources().getString(
					R.string.invalidePinCodeLength));
		} else if (bean.getPinCode().length() > 6) {
			et_pinCode.requestFocus();
			et_pinCode.setError(getResources().getString(
					R.string.invalidePinCodeLength));
		} else {
			et_pinCode.setError(null);
		}

		return validate;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		lv_attached_files.requestFocus();
		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == AppConstants.PICK_IMAGE) {
				selectedImagePath = FileUtils.getPath(this, data.getData());

				if (selectedImagePath != null) {
					String fileName = selectedImagePath.substring(
							selectedImagePath.lastIndexOf('/') + 1,
							selectedImagePath.length());

					// Check supported File extensions
					if (!CommonActions.isFileExtensionAllowed(
							EmigrationAndVisaActivity.this,
							FileUtils.getExtension(selectedImagePath))) {

						CommonActions
								.showUnsupportedFileError(EmigrationAndVisaActivity.this);
					} else {
						GoysLog.d(
								TAG,
								"File Extension "
										+ FileUtils
												.getExtension(selectedImagePath));
						lv_attached_files.setVisibility(View.VISIBLE);

						attachmentList.add(new Attachment(fileName,
								selectedImagePath));

						if (formBean == null) {
							formBean = new EmigrationFormBean();
						}
						formBean.setAttachment(attachmentList);
						
						// This is a new adapter that takes the actual list
						myAdapter = new MyAttachmentAdapter(lv_attached_files,
								this, 0, attachmentList);
						lv_attached_files.setAdapter(myAdapter);

						int heightofListView = lv_attached_files.getHeight();
						GoysLog.d(TAG, "attached_files_listview height "
								+ heightofListView);

						btn_chooseFile.requestFocus();
					}
				}

			} else if (requestCode == AppConstants.CAPTURE_IMAGE) {
				// FileOutputStream fo = null;
				String fileName = "";
				if (resultCode == Activity.RESULT_OK) {
					try {
						selectedImagePath = imageUri.getPath().toString();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				fileName = selectedImagePath.substring(
						selectedImagePath.lastIndexOf('/') + 1,
						selectedImagePath.length());
				lv_attached_files.setVisibility(View.VISIBLE);

				attachmentList.add(new Attachment(fileName, selectedImagePath));
				
				if (formBean == null) {
					formBean = new EmigrationFormBean();
				}
				formBean.setAttachment(attachmentList);

				// This is a new adapter that takes the actual list
				myAdapter = new MyAttachmentAdapter(lv_attached_files, this, 0,
						attachmentList);
				lv_attached_files.setAdapter(myAdapter);
				btn_chooseFile.requestFocus();

			} else {
				super.onActivityResult(requestCode, resultCode, data);

			}
		}
	}

	private void clearValuesForEditText(EditText editText) {

		editText.setText("");
		editText.setError(null);
	}

	public void clearForm() {

		clearValuesForEditText(et_pinCode);
		clearValuesForEditText(et_clubName);
		clearValuesForEditText(et_email);
		clearValuesForEditText(et_firstName);
		clearValuesForEditText(et_middleName);
		clearValuesForEditText(et_familyName);
		clearValuesForEditText(et_job);
		clearValuesForEditText(et_passportNumber);
		clearValuesForEditText(et_personalNumber);
		clearValuesForEditText(et_other_option);

		spin_country.setSelection(0);
		spin_requestType.setSelection(0);

		if (attachmentList.size() > 0) {
			attachmentList = new ArrayList<Attachment>();
			myAdapter.notifyDataSetChanged();
			lv_attached_files.setVisibility(View.GONE);
		}
		et_pinCode.requestFocus();

		rg_residenciesRadio.check(R.id.residenceies_option_one);
		rg_visasRadio.check(R.id.visas_option_one);
		rg_changeJobTitleRadios.check(R.id.changeJobTitle_option_one);
		rg_changeSponsorRadios.check(R.id.changeSponsor_option_one);

	}

	@Override
	public void onResponse(int serviceId, String responseObj) {

		if (serviceId == AppConstants.VALIDATE_PINCODE_SERVICE_ID) {
			if (responseObj != null && !responseObj.contains("Error")) {

				GoysLog.d(TAG, "onResponse " + responseObj);
				try {
					JSONObject obj = new JSONObject(responseObj);

					boolean isValid = obj.getBoolean("Valid");

					if (isValid) {

						String clubName = "";
						if (GOYSApplication.getInstance().isEnglishLanguage()) {
							clubName = obj.getString("ClubNameEN");
						} else {
							clubName = obj.getString("ClubNameAR");
						}

						String email = obj.getString("Email");

						et_clubName.setText(clubName);

						/* storing a valid pinCode after verified */
						tempPinCode = et_pinCode.getText().toString();

						et_email.setText(email);
						et_email.setError(null);
						et_pinCode.setError(null);
						et_clubName.setError(null);
					} else {
						et_pinCode.requestFocus();
						et_pinCode.setError(getResources().getString(
								(R.string.invalidePinCode)));
						et_clubName.setText("");
						et_email.setText("");
					}

				} catch (JSONException e) {
					GoysLog.e(TAG, "Error: " + e.toString());
					CommonActions.showErrorAlertDialog(
							EmigrationAndVisaActivity.this,
							0,
							getResources().getString(
									R.string.applicationundermaintenance));

				}

			} else {
				CommonActions.alertDialogShow(
						EmigrationAndVisaActivity.this,
						getResources().getString(R.string.alert_error_title),
						getResources().getString(
								R.string.applicationundermaintenance));

			}
		} else if (serviceId == AppConstants.EMIGRATIONANDVISA_SERVICE_ID) {

			if (responseObj != null && !responseObj.equals("")
					&& !responseObj.contains("Error")) {

				String msg = getResources().getString(
						R.string.successful_submission_msg)
						+ " " + responseObj;

				/* play success submission sound */
				if (GOYSApplication.getInstance().isMusicOn()) {
					MediaPlayer mPlayer2 = MediaPlayer.create(
							getApplicationContext(), R.raw.popup_sound);
					mPlayer2.start();
				}

				CommonActions
						.showSuccessfulSubmissionAlert(this, getResources()
								.getString(R.string.menu_emigration_visa), msg);

			} else {
				CommonActions.showErrorAlertDialog(this, 0, getResources()
						.getString(R.string.applicationundermaintenance));

			}

		}

	}

	@Override
	public void onError(int responseCode, String msg) {
		if (isActivityAvailable) {
			CommonActions.showErrorAlertDialog(EmigrationAndVisaActivity.this,
					responseCode, msg);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (imageUri == null) {
			outState.putString("file-uri", "");
		} else {
			outState.putString("file-uri", imageUri.toString());
		}

		outState.putParcelableArrayList("attached_files", attachmentList);

	}

	private String getRadioSelectedValue() {

		String value = spin_requestType.getSelectedItem().toString();

		int selectedId = 0;
		if (value.equals(getResources().getString(
				R.string.request_type_residence))) {

			selectedId = rg_residenciesRadio.getCheckedRadioButtonId();
			rb_residencies = (RadioButton) EmigrationAndVisaActivity.this
					.findViewById(selectedId);
			value = rb_residencies.getText().toString();

		} else if (value.equals(getResources().getString(
				R.string.request_type_jobtitle))) {
			selectedId = rg_changeJobTitleRadios.getCheckedRadioButtonId();

			rb_changeJobTitle = (RadioButton) EmigrationAndVisaActivity.this
					.findViewById(selectedId);
			value = rb_changeJobTitle.getText().toString();

		} else if (value.equals(getResources().getString(
				R.string.request_type_sponsorship))) {
			selectedId = rg_changeSponsorRadios.getCheckedRadioButtonId();

			rb_changeSponsor = (RadioButton) EmigrationAndVisaActivity.this
					.findViewById(selectedId);
			value = rb_changeSponsor.getText().toString();

		} else if (value.equals(getResources().getString(
				R.string.request_type_visas))) {
			selectedId = rg_visasRadio.getCheckedRadioButtonId();

			rb_visas = (RadioButton) EmigrationAndVisaActivity.this
					.findViewById(selectedId);
			value = rb_visas.getText().toString();

		} else if (value.equals(getResources().getString(
				R.string.request_type_other))) {

			value = et_other_option.getText().toString();
		}

		return value;
	}

	public void viewFileDialogWithFewOption() {

		final CharSequence[] items = {
				getResources().getString(R.string.takePicture),
				getResources().getString(R.string.picfromGal),
				getResources().getString(R.string.cancel) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.picker_dialog_title));
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				List<Intent> targetShareIntents = new ArrayList<Intent>();

				if (items[item].equals(getResources().getString(
						R.string.takePicture))) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					imageUri = ImageServices
							.getOutputImageFileUri(EmigrationAndVisaActivity.this);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, AppConstants.CAPTURE_IMAGE);

				} else if (items[item].equals(getResources().getString(
						R.string.picfromGal))) {
					
					// Implicitly allow the user to select a particular kind of
					// data
					Intent target = new Intent(Intent.ACTION_GET_CONTENT);
					// The MIME data type filter
					target.setType("*/*");
					// Only return URIs that can be opened with ContentResolver
					target.addCategory(Intent.CATEGORY_OPENABLE);

					List<ResolveInfo> resInfos = getPackageManager()
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
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setPackage(packageName);
								targetShareIntents.add(intent);
							}
						}
						if (!targetShareIntents.isEmpty()) {
							System.out.println("Have Intent");
							Intent chooserIntent = Intent.createChooser(
									targetShareIntents.remove(0),
									getString(R.string.chooser_title));
							chooserIntent.putExtra(
									Intent.EXTRA_INITIAL_INTENTS,
									targetShareIntents
											.toArray(new Parcelable[] {}));
							startActivityForResult(chooserIntent,
									AppConstants.PICK_IMAGE);
						} else {
							System.out.println("Do not Have Intent");
						}
					}

				} else if (items[item].equals(getResources().getString(
						R.string.cancel))) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	
	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public void hideSoftKeyboard() {
	    try {
	        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		
		switch (id) {
		case R.id.country:
		case R.id.request_type:
			hideSoftKeyboard();
			break;

		default:
			break;
		}
		
		return false;
	}
}
