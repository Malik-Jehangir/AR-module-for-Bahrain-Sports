package com.goys.android.app.maintenance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andorid.components.afilechooser.utils.FileUtils;
import com.google.gson.JsonObject;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.db.model.MaintenancePublic;
import com.goys.android.app.db.model.ServiceData;
import com.goys.android.app.emigration.Attachment;
import com.goys.android.app.emigration.EmigrationAndVisaActivity;
import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.CommonObjects;
import com.goys.android.app.util.GPSTracker;
import com.goys.android.app.util.GoysLog;
import com.goys.android.app.util.GoysService;
import com.goys.android.app.util.ImageServices;
import com.goys.android.app.util.MyAttachmentAdapter;
import com.goys.android.app.util.ResponseListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputFilter.LengthFilter;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MaintenancePublicUserFragment extends Fragment implements
		ResponseListener, OnTouchListener {

	private static final String TAG = "MaintenancePublicUserFragment";

	View mainView;

	TextView labelAttachment, instruction, labelServiceError,
			labelFacilityError, labelGovernorateError;

	MaintenancePublic publicFormItem;

	MaintenancePublic[] publicFormItemList;

	ImageButton ibDatePicker, ibManual, ibGPS;

	Button bSubmit, bChooseFile;

	EditText etJobDescription, etPlaceRequiredMaintenance, etBuildingNo,
			etRoadNo, etBlockNo, etEmail, etMobileNo, etDate, etGovernorate,
			etArea;

	Spinner spServiceType, spFacilityName, spGovernorate;

	RadioGroup rgLocationDetail;

	// Variable for storing current date
	private int mYear, mMonth, mDay;

	ListView lv_attached_files;

	private String selectedImagePath = "";

	private ArrayList<Attachment> attachmentList;

	MyAttachmentAdapter myAdapter;

	GoysService gs;

	GPSTracker gps;

	Uri imageUri;

	String[] serviceTypes = null;

	String[] facilitiesList = null;

	String[] governorateList = null;

	ArrayList<ServiceData> facilitiesListModel = null;
	ArrayList<ServiceData> serviceTypesModel = null;
	ArrayList<ServiceData> governateListModel = null;
	ArrayList<ServiceData> maintenanceListModel = null;
	String governorateId = null;

	CommonActions ca;

	Boolean isRotate = false;

	LinearLayout llGovernorate, llServiceType, llFacilityName, llChooseFile;

	@Override
	public void onResume() {
		super.onResume();
		isRotate = false;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		spServiceType = (Spinner) mainView.findViewById(R.id.sp_service_type);
		if (savedInstanceState == null) {
			attachmentList = new ArrayList<Attachment>();

			spServiceType.requestFocus();
			spServiceType.setEnabled(false);

			hitGetServiceTypeService();
		} else {

			String fileUri = savedInstanceState.getString("file-uri");
			if (!fileUri.equals("")) {
				imageUri = Uri.parse(fileUri);
			}

			attachmentList = savedInstanceState
					.getParcelableArrayList("attached_files");

			myAdapter = new MyAttachmentAdapter(lv_attached_files,
					getActivity(), 0, attachmentList);
			lv_attached_files.setAdapter(myAdapter);

			if (savedInstanceState.getStringArray("serviceTypes") != null) {
				serviceTypes = savedInstanceState
						.getStringArray("serviceTypes");
				setSpinnerAdaptor(spServiceType, serviceTypes);

				serviceTypesModel = savedInstanceState
						.getParcelableArrayList("serviceTypesModel");

				if (serviceTypes.length > 0) {
					spServiceType.setEnabled(true);
				}
			}

			if (savedInstanceState.getStringArray("governorateList") != null) {
				governorateList = savedInstanceState
						.getStringArray("governorateList");

				setSpinnerAdaptor(spGovernorate, governorateList);

				governateListModel = savedInstanceState
						.getParcelableArrayList("governateListModel");

				if (governorateList.length > 0) {
					spGovernorate.setEnabled(true);
				}
			}

			if (savedInstanceState
					.getParcelableArrayList("maintenanceListModel") != null) {
				maintenanceListModel = savedInstanceState
						.getParcelableArrayList("maintenanceListModel");
			}

			if (savedInstanceState.getStringArray("facilitiesList") != null) {
				facilitiesList = savedInstanceState
						.getStringArray("facilitiesList");

				setSpinnerAdaptor(spFacilityName, facilitiesList);

				facilitiesListModel = savedInstanceState
						.getParcelableArrayList("facilitiesListModel");

				if (facilitiesList.length > 0) {
					spFacilityName.setEnabled(true);
				}
			}

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			isRotate = true;
		} else {
			isRotate = false;
		}

		mainView = inflater.inflate(
				R.layout.fragment_maintenance_form_publicuser, null);

		initView();
		initObj();

		return mainView;
	}

	private void initObj() {
		gs = new GoysService(getActivity());
		gs.setResponseListener(MaintenancePublicUserFragment.this);
		gps = new GPSTracker(getActivity());

		ca = new CommonActions(getActivity());

	}

	public void hitGetServiceTypeService() {
		if (((MaintenanceFormActivity) getActivity()).pager.getCurrentItem() == 0) {
			if (CommonActions.hasConnection(getActivity())) {
				gs.getServiceTypeService1(
						(MaintenanceFormActivity) getActivity(), "",
						"Emergency");

			} else {
				CommonActions.alertDialogShow(getActivity(), getResources()
						.getString(R.string.alert_error_title), getResources()
						.getString(R.string.networkConnectionRequired));
			}
		}
	}

	private void initView() {

		ibManual = (ImageButton) mainView.findViewById(R.id.ib_manual);
		ibManual.setSelected(true);
		ibGPS = (ImageButton) mainView.findViewById(R.id.ib_gps);

		ibManual.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etGovernorate.setVisibility(View.GONE);
				spGovernorate.setVisibility(View.VISIBLE);
				ibManual.setSelected(true);
				ibGPS.setSelected(false);

				if (((MaintenanceFormActivity) getActivity()).isGovernorateEnable) {
					setSpinnerAdaptor(spGovernorate, governorateList);
				}

				etBuildingNo.setEnabled(true);
				etBlockNo.setEnabled(true);
				etRoadNo.setEnabled(true);
				etArea.setEnabled(true);

				etBuildingNo.setText(null);
				etBlockNo.setText(null);
				etRoadNo.setText(null);
				etArea.setText(null);
				etGovernorate.setText(null);

			}
		});

		ibGPS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				etGovernorate.setVisibility(View.VISIBLE);
				spGovernorate.setVisibility(View.GONE);
				// spGovernorate.setSelection(0);
				ibManual.setSelected(false);
				ibGPS.setSelected(true);

				etBuildingNo.setEnabled(false);
				etBlockNo.setEnabled(false);
				etRoadNo.setEnabled(false);
				etArea.setEnabled(false);
				etGovernorate.setEnabled(false);

				if (!isRotate) {
					if (gps.isGPSEnable()) {
						if (gps.canGetLocation()) {

							// double latitude = 26.1740065;
							// double longitude = 50.5485543;

							double latitude = gps.getLatitude();
							double longitude = gps.getLongitude();

							Log.d("lat", latitude + "");
							Log.d("long", longitude + "");

							if (CommonActions.hasConnection(getActivity())) {
								gs.getLocationService(getActivity(),
										String.valueOf(latitude),
										String.valueOf(longitude));
							} else {
								CommonActions
										.alertDialogShow(
												getActivity(),
												getResources()
														.getString(
																R.string.alert_error_title),
												getResources()
														.getString(
																R.string.networkConnectionRequired));
								selectManual();
							}

							// 26.1740065
							// 50.5485543
							// String.valueOf(latitude),
							// String.valueOf(longitude)
						} else {
							// can't get location
							// GPS or Network is not enabled
							// Ask user to enable GPS/network in
							// settings
							gps.showSettingsAlert();
							selectManual();
						}
					} else {
						gps.showSettingsAlert();
						selectManual();
					}
				} else {
					isRotate = false;
				}

			}
		});

		etJobDescription = (EditText) mainView
				.findViewById(R.id.et_job_description);
		etPlaceRequiredMaintenance = (EditText) mainView
				.findViewById(R.id.et_place_required_maintenance);
		etBuildingNo = (EditText) mainView.findViewById(R.id.et_building_no);
		etRoadNo = (EditText) mainView.findViewById(R.id.et_road_no);
		etBlockNo = (EditText) mainView.findViewById(R.id.et_block_no);
		etGovernorate = (EditText) mainView.findViewById(R.id.et_governorate);
		etArea = (EditText) mainView.findViewById(R.id.et_area);
		etEmail = (EditText) mainView.findViewById(R.id.et_email);
		etMobileNo = (EditText) mainView.findViewById(R.id.et_mobile_no);
		etDate = (EditText) mainView.findViewById(R.id.et_date);
		lv_attached_files = (ListView) mainView
				.findViewById(R.id.attached_files_listview);
		labelAttachment = (TextView) mainView
				.findViewById(R.id.label_attachments);
		labelServiceError = (TextView) mainView
				.findViewById(R.id.label_service_type_error);
		labelFacilityError = (TextView) mainView
				.findViewById(R.id.label_facility_error);
		labelGovernorateError = (TextView) mainView
				.findViewById(R.id.label_governorate_error);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// etDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
		etDate.setText(android.text.format.DateFormat.format("dd MMMM, yyyy",
				new java.util.Date()));

		llServiceType = (LinearLayout) mainView
				.findViewById(R.id.ll_service_type);
		llServiceType.setFocusableInTouchMode(true);
		spServiceType = (Spinner) mainView.findViewById(R.id.sp_service_type);
		spServiceType.setOnTouchListener(this);
		// spServiceType.setFocusableInTouchMode(true);
		// spServiceType.setEnabled(false);
		setSpinnerAdaptor(spServiceType, getActivity().getResources()
				.getStringArray(R.array.service_type_public_emergency_arrays));
		/*
		 * spServiceType.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * InputMethodManager
		 * imm=(InputMethodManager)getActivity().getApplicationContext
		 * ().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		 * imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
		 * .getWindowToken(), 0); return false; } }) ;
		 */
		spServiceType.post(new Runnable() {

			@Override
			public void run() {

				spServiceType
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {

								if (position >= 0) {
									labelServiceError.setVisibility(View.GONE);
									((TextView) parent.getChildAt(0))
											.setTextColor(getResources()
													.getColor(R.color.black));

									if (CommonActions
											.hasConnection(getActivity())) {

										gs.getFacilitiesByServiceType(
												(MaintenanceFormActivity) getActivity(),
												spServiceType.getSelectedItem()
														.toString());

									} else {
										CommonActions
												.alertDialogShow(
														getActivity(),
														getResources()
																.getString(
																		R.string.alert_error_title),
														getResources()
																.getString(
																		R.string.networkConnectionRequired));

									}

								}

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
							}
						});

			}
		});

		instruction = (TextView) mainView.findViewById(R.id.instruction);
		instruction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonActions
						.alertDialogShow(
								getActivity(),
								getActivity().getResources().getString(
										R.string.note),
								getActivity().getResources().getString(
										R.string.instruction_message));
			}
		});

		llFacilityName = (LinearLayout) mainView
				.findViewById(R.id.ll_facility_name);
		llFacilityName.setFocusableInTouchMode(true);
		spFacilityName = (Spinner) mainView.findViewById(R.id.sp_facility_name);
		spFacilityName.setOnTouchListener(this);
		// spFacilityName.setFocusableInTouchMode(true);
		spFacilityName.setEnabled(false);
		spFacilityName.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= 0) {
					labelFacilityError.setVisibility(View.GONE);
					((TextView) parent.getChildAt(0))
							.setTextColor(getResources()
									.getColor(R.color.black));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		llGovernorate = (LinearLayout) mainView
				.findViewById(R.id.ll_governorate);
		llGovernorate.setFocusableInTouchMode(true);
		spGovernorate = (Spinner) mainView.findViewById(R.id.sp_governorate);
		spGovernorate.setOnTouchListener(this);
		// spGovernorate.setFocusable(false);
		// spGovernorate.setFocusableInTouchMode(true);
		spGovernorate.setEnabled(false);

		spGovernorate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= 0) {

					labelGovernorateError.setVisibility(View.GONE);
					((TextView) parent.getChildAt(0))
							.setTextColor(getResources()
									.getColor(R.color.black));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		llChooseFile = (LinearLayout) mainView
				.findViewById(R.id.ll_choose_file);
		llChooseFile.setFocusableInTouchMode(true);
		bChooseFile = (Button) mainView.findViewById(R.id.b_chooseFile);
		// bChooseFile.setFocusableInTouchMode(true);
		bChooseFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * CommonActions.viewFileDialogWithFewOption(getActivity(),
				 * MaintenancePublicUserFragment.this);
				 */
				viewFileDialogWithFewOption();
			}
		});

		bSubmit = (Button) mainView.findViewById(R.id.b_submit);
		bSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				publicFormItemList = new MaintenancePublic[2];

				storeFormDataIntoModel();

				if (validateForm(publicFormItem)) {

					if (CommonActions.hasConnection(getActivity())) {
						publicFormItemList[0] = publicFormItem;
						gs.MaintenancePublicFormService(
								(MaintenanceFormActivity) getActivity(),
								publicFormItemList);

					} else {
						CommonActions.alertDialogShow(
								getActivity(),
								getResources().getString(
										R.string.alert_error_title),
								getResources().getString(
										R.string.networkConnectionRequired));

					}

				}

			}
		});

	}

	private void selectManual() {
		etGovernorate.setVisibility(View.GONE);
		spGovernorate.setVisibility(View.VISIBLE);
		ibManual.setSelected(true);
		ibGPS.setSelected(false);

		if (((MaintenanceFormActivity) getActivity()).isGovernorateEnable) {
			setSpinnerAdaptor(spGovernorate, governorateList);
		}

		etBuildingNo.setEnabled(true);
		etBlockNo.setEnabled(true);
		etRoadNo.setEnabled(true);
		etArea.setEnabled(true);

		etBuildingNo.setText(null);
		etBlockNo.setText(null);
		etRoadNo.setText(null);
		etArea.setText(null);
		etGovernorate.setText(null);

	}

	public void hideSoftKeyboard() {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setSpinnerAdaptor(Spinner spinner, String[] string) {

		SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), string);
		spinner.setAdapter(adapter);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (imageUri == null) {
			outState.putString("file-uri", "");
		} else {
			outState.putString("file-uri", imageUri.toString());
		}
		outState.putParcelableArrayList("attached_files", attachmentList);

		if (serviceTypes != null) {
			outState.putStringArray("serviceTypes", serviceTypes);
			outState.putParcelableArrayList("serviceTypesModel",
					serviceTypesModel);
		}

		if (governorateList != null) {
			outState.putStringArray("governorateList", governorateList);
			outState.putParcelableArrayList("governateListModel",
					governateListModel);
		}

		if (facilitiesList != null) {
			outState.putStringArray("facilitiesList", facilitiesList);
			outState.putParcelableArrayList("facilitiesListModel",
					facilitiesListModel);
		}

		if (maintenanceListModel != null) {
			outState.putParcelableArrayList("maintenanceListModel",
					maintenanceListModel);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		lv_attached_files.requestFocus();
		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == AppConstants.PICK_IMAGE) {
				selectedImagePath = FileUtils.getPath(getActivity(),
						data.getData());

				if (selectedImagePath != null) {
					String fileName = selectedImagePath.substring(
							selectedImagePath.lastIndexOf('/') + 1,
							selectedImagePath.length());

					// Check supported File extensions
					if (!CommonActions.isFileExtensionAllowed(getActivity(),
							FileUtils.getExtension(selectedImagePath))) {

						CommonActions.showUnsupportedFileError(getActivity());
					} else {

						lv_attached_files.setVisibility(View.VISIBLE);

						attachmentList.add(new Attachment(fileName,
								selectedImagePath));

						if (publicFormItem == null) {
							publicFormItem = new MaintenancePublic();
						}
						publicFormItem.setAttachment(attachmentList);
						labelAttachment.setVisibility(View.GONE);

						// This is a new adapter that takes the actual list
						myAdapter = new MyAttachmentAdapter(lv_attached_files,
								getActivity(), 0, attachmentList);
						lv_attached_files.setAdapter(myAdapter);

						bChooseFile.requestFocus();
					}
				}

			} else if (requestCode == AppConstants.CAPTURE_IMAGE) {

				String fileName = "";
				if (resultCode == Activity.RESULT_OK) {
					try {
						selectedImagePath = imageUri.getPath().toString();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				// this is the url that where you are saved the image
				fileName = selectedImagePath.substring(
						selectedImagePath.lastIndexOf('/') + 1,
						selectedImagePath.length());

				lv_attached_files.setVisibility(View.VISIBLE);

				attachmentList.add(new Attachment(fileName, selectedImagePath));
				labelAttachment.setVisibility(View.GONE);

				if (publicFormItem == null) {
					publicFormItem = new MaintenancePublic();
				}
				publicFormItem.setAttachment(attachmentList);

				// This is a new adapter that takes the actual list
				myAdapter = new MyAttachmentAdapter(lv_attached_files,
						getActivity(), 0, attachmentList);
				lv_attached_files.setAdapter(myAdapter);
				bChooseFile.requestFocus();

			} else {
				super.onActivityResult(requestCode, resultCode, data);

			}
		}
	}

	public void storeFormDataIntoModel() {

		publicFormItem = new MaintenancePublic();

		publicFormItem.setJobDescription(etJobDescription.getText().toString());
		publicFormItem.setPlaceRequiredMaintenance(etPlaceRequiredMaintenance
				.getText().toString());
		publicFormItem.setBuildingNo(etBuildingNo.getText().toString());
		publicFormItem.setRoadNo(etRoadNo.getText().toString());
		publicFormItem.setBlockNo(etBlockNo.getText().toString());
		publicFormItem.setEmail(etEmail.getText().toString());
		publicFormItem.setArea(etArea.getText().toString());
		publicFormItem.setMobileNo(etMobileNo.getText().toString());

		publicFormItem.setMaintenanceType(getMaintenanceId());

		// publicFormItem.setDate(etDate.getText().toString());

		/*
		 * SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM,yyyy",
		 * Locale.ENGLISH); String formattedDate = dateFormat.format(new
		 * Date());
		 */

		Date cDate = new Date();
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy",
				Locale.ENGLISH).format(cDate);

		/*
		 * final Calendar c = Calendar.getInstance(); String formattedDate =
		 * c.get
		 * (Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(
		 * Calendar .YEAR);
		 */

		publicFormItem.setDate(formattedDate);

		if (spServiceType.getSelectedItemPosition() >= 0) {
			/*
			 * publicFormItem.setServiceType(spServiceType.getSelectedItem()
			 * .toString());
			 */
			publicFormItem.setServiceType(serviceTypesModel.get(
					spServiceType.getSelectedItemPosition()).getID());
		}

		if (spFacilityName.getSelectedItemPosition() >= 0) {
			/*
			 * publicFormItem.setFacilityName(spFacilityName.getSelectedItem()
			 * .toString());
			 */
			publicFormItem.setFacilityName(facilitiesListModel.get(
					spFacilityName.getSelectedItemPosition()).getID());
		}

		if (spGovernorate.isShown()) {
			if (spGovernorate.getSelectedItemPosition() >= 0) {
				/*
				 * publicFormItem.setGovernorate(spGovernorate.getSelectedItem()
				 * .toString());
				 */
				publicFormItem.setGovernorate(governateListModel.get(
						spGovernorate.getSelectedItemPosition()).getID());
			} else {
				publicFormItem.setGovernorate("");
			}
		} else {
			// publicFormItem.setGovernorate(etGovernorate.getText().toString());
			publicFormItem.setGovernorate(governorateId);
		}
		/* adding attachment list into formBean */
		if (attachmentList.size() > 0) {
			publicFormItem.setAttachment(attachmentList);
		}

		if (ibManual.isSelected()) {
			publicFormItem.setLocationType("Manual");
		} else {
			publicFormItem.setLocationType("GPS");
		}

	}

	public String getMaintenanceId() {
		for (int i = 0; i < maintenanceListModel.size(); i++) {

			if (getResources().getString(R.string.text_emergency)
					.equalsIgnoreCase(maintenanceListModel.get(i).getValue())) {
				return maintenanceListModel.get(i).getID();
			}
		}

		return "-1";
	}

	public boolean validateForm(MaintenancePublic publicFormItem) {
		boolean validate = true;

		if (CommonObjects.isEmpty(publicFormItem.getDate())) {
			etDate.setError(getResources().getString((R.string.emptyField)));
			validate = false;

		} else {
			etDate.setError(null);
		}

		if (!CommonObjects.isEmpty(publicFormItem.getMobileNo())
				&& publicFormItem.getMobileNo().length() != 8) {
			etMobileNo.setError(getActivity().getResources().getString(
					R.string.maintenance_invalid_number_lenght));
			etMobileNo.requestFocus();
			validate = false;
		} else {
			etMobileNo.setError(null);
		}

		if (CommonObjects.isEmpty(publicFormItem.getEmail())) {
			etEmail.requestFocus();
			etEmail.setError(getResources().getString((R.string.emptyField)));
			validate = false;

		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
				publicFormItem.getEmail()).matches()) {
			etEmail.requestFocus();
			etEmail.setError(getActivity().getResources().getString(
					R.string.invalidEmail));
			validate = false;

		} else {
			etEmail.setError(null);
		}

		if (publicFormItem.getAttachment() == null) {
			labelAttachment.setVisibility(View.VISIBLE);
			// bChooseFile.requestFocus();
			llChooseFile.requestFocus();
			validate = false;
		}

		// Governorate
		if (ibManual.isSelected()) {
			if (spGovernorate.getSelectedItemPosition() < 0) {
				labelGovernorateError.setVisibility(View.VISIBLE);
				validate = false;
				// spGovernorate.requestFocus();
				llGovernorate.requestFocus();
			}

		} else {
			if (CommonObjects.isEmpty(publicFormItem.getGovernorate())) {
				etGovernorate.requestFocus();
				etGovernorate.setError(getResources().getString(
						(R.string.emptyField)));
				validate = false;
			} else {
				etGovernorate.setError(null);
			}
		}

		// Area
		if (CommonObjects.isEmpty(publicFormItem.getArea())) {
			etArea.requestFocus();
			etArea.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else {
			etArea.setError(null);
		}

		// Block No
		if (CommonObjects.isEmpty(publicFormItem.getBlockNo())) {
			etBlockNo.requestFocus();
			etBlockNo.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} /*else if (!CommonObjects.isEmpty(publicFormItem.getBlockNo())
				&& Pattern.compile(AppConstants.ALPHA_NUMERIC_SPACE)
						.matcher(publicFormItem.getBlockNo()).find()) {
			etBlockNo.setError(getActivity().getResources().getString(
					R.string.onlyCharacterAndNumberAllow));
			etBlockNo.requestFocus();
			validate = false;
		}*/ else {
			etBlockNo.setError(null);
		}

		// Road No
		if (CommonObjects.isEmpty(publicFormItem.getRoadNo())) {
			etRoadNo.requestFocus();
			etRoadNo.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else if (!CommonObjects.isEmpty(publicFormItem.getRoadNo())
				&& Pattern.compile(AppConstants.ALPHA_NUMERIC_SPACE)
						.matcher(publicFormItem.getRoadNo()).find()) {
			etRoadNo.setError(getActivity().getResources().getString(
					R.string.onlyCharacterAndNumberAllow));
			etRoadNo.requestFocus();
			validate = false;
		} else {
			etRoadNo.setError(null);
		}

		// Building No
		if (CommonObjects.isEmpty(publicFormItem.getBuildingNo())) {
			etBuildingNo.requestFocus();
			etBuildingNo.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else {
			etBuildingNo.setError(null);
		}

		if (CommonObjects.isEmpty(publicFormItem.getPlaceRequiredMaintenance())) {
			etPlaceRequiredMaintenance.requestFocus();
			etPlaceRequiredMaintenance.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;

		} else {
			etPlaceRequiredMaintenance.setError(null);
		}

		if (spFacilityName.getSelectedItemPosition() < 0) {
			labelFacilityError.setVisibility(View.VISIBLE);
			validate = false;
			// spFacilityName.requestFocus();
			llFacilityName.requestFocus();
		}

		if (CommonObjects.isEmpty(publicFormItem.getJobDescription())) {
			etJobDescription.requestFocus();
			etJobDescription.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else {
			etJobDescription.setError(null);
		}

		if (spServiceType.getSelectedItemPosition() < 0) {
			labelServiceError.setVisibility(View.VISIBLE);
			validate = false;
			// spServiceType.requestFocus();
			llServiceType.requestFocus();
		}

		return validate;
	}

	private void showData() {
		Log.d("job Description", publicFormItem.getJobDescription());
		Log.d("Place Requirement", publicFormItem.getPlaceRequiredMaintenance());
		Log.d("Building No", publicFormItem.getBuildingNo());
		Log.d("Road No", publicFormItem.getRoadNo());
		Log.d("Block No", publicFormItem.getBlockNo());
		Log.d("Email", publicFormItem.getEmail());
		Log.d("Mobile No", publicFormItem.getMobileNo());
		Log.d("Date", publicFormItem.getDate());
		Log.d("Service Type", publicFormItem.getServiceType());
		Log.d("Facility Name", publicFormItem.getFacilityName());
		Log.d("Governrate", publicFormItem.getGovernorate());
		Log.d("Attachment", publicFormItem.getAttachment().toString());
	}

	private void clearValuesForEditText(EditText editText) {

		editText.setText("");
		editText.setError(null);
	}

	public void clearForm() {

		clearValuesForEditText(etMobileNo);
		clearValuesForEditText(etEmail);
		clearValuesForEditText(etJobDescription);
		clearValuesForEditText(etPlaceRequiredMaintenance);
		clearValuesForEditText(etBuildingNo);
		clearValuesForEditText(etRoadNo);
		clearValuesForEditText(etBlockNo);
		clearValuesForEditText(etArea);
		clearValuesForEditText(etDate);
		clearValuesForEditText(etGovernorate);

		spGovernorate.setSelection(0);
		spServiceType.setSelection(0);

		attachmentList = new ArrayList<Attachment>();
		lv_attached_files.setVisibility(View.GONE);
		labelAttachment.setVisibility(View.GONE);

	}

	@Override
	public void onResponse(int serviceId, String responseObj) {

		if (getActivity() != null && responseObj != null
				&& !responseObj.contains("Error") && !responseObj.equals("")) {
			if (serviceId == AppConstants.MAINTENANCE_PUBLIC_SERVICE_ID) {

				String msg = getResources().getString(
						R.string.successful_submission_msg)
						+ " " + responseObj;

				Log.d("Response", msg);

				// play success submission sound if
				if (GOYSApplication.getInstance().isMusicOn()) {
					MediaPlayer mPlayer2 = MediaPlayer.create(getActivity(),
							R.raw.popup_sound);
					mPlayer2.start();
				}

				CommonActions.showSuccessfulSubmissionAlert(getActivity(),
						getResources().getString(R.string.lbl_snapfix_form),
						msg);

			} else {

			}

			if (serviceId == AppConstants.MAINTENANCE_GET_SERVICE_TYPE_ID) {

				Log.d("Response Service Types", responseObj);
				parseServiceTypeJson(responseObj);

			}

			if (serviceId == AppConstants.MAINTENANCE_GET_LOCATION_SERVICE_ID) {
				Log.d("Response Get Location", responseObj);
				try {
					JSONObject obj = new JSONObject(responseObj);

					if (GOYSApplication.getInstance().isEnglishLanguage()) {
						if (obj.getString("GOVERNORATE").equalsIgnoreCase(
								"null")
								&& obj.getString("BLOCK").equalsIgnoreCase(
										"null")
								&& obj.getString("BUILDING").equalsIgnoreCase(
										"null")
								&& obj.getString("ROAD").equalsIgnoreCase(
										"null")
								&& obj.getString("AREA").equalsIgnoreCase(
										"null")
								&& obj.getString("GOVERNORATE_ID")
										.equalsIgnoreCase("null")) {

							CommonActions.showErrorAlertDialog(
									getActivity(),
									0,
									getResources().getString(
											R.string.cannot_get_location));
							selectManual();
						}

					} else {
						if (obj.getString("GOVERNORATE_AR").equalsIgnoreCase(
								"null")
								&& obj.getString("BLOCK").equalsIgnoreCase(
										"null")
								&& obj.getString("BUILDING").equalsIgnoreCase(
										"null")
								&& obj.getString("ROAD").equalsIgnoreCase(
										"null")
								&& obj.getString("AREA_AR").equalsIgnoreCase(
										"null")
								&& obj.getString("GOVERNORATE_ID")
										.equalsIgnoreCase("null")) {

							CommonActions.showErrorAlertDialog(
									getActivity(),
									0,
									getResources().getString(
											R.string.cannot_get_location));
							selectManual();

						}

					}

					if (GOYSApplication.getInstance().isEnglishLanguage()) {
						if (!obj.getString("GOVERNORATE").equalsIgnoreCase(
								"null")) {
							etGovernorate.setText(obj.getString("GOVERNORATE"));
							labelGovernorateError.setVisibility(View.GONE);
						}
					} else {
						if (!obj.getString("GOVERNORATE_AR").equalsIgnoreCase(
								"null")) {
							etGovernorate.setText(obj
									.getString("GOVERNORATE_AR"));
							labelGovernorateError.setVisibility(View.GONE);
						}
					}

					if (!obj.getString("BLOCK").equalsIgnoreCase("null")) {
						etBlockNo.setText(obj.getString("BLOCK"));
					}
					if (!obj.getString("BUILDING").equalsIgnoreCase("null")) {
						etBuildingNo.setText(obj.getString("BUILDING"));
					}
					if (!obj.getString("ROAD").equalsIgnoreCase("null")) {
						etRoadNo.setText(obj.getString("ROAD"));
					}
					if (!obj.getString("GOVERNORATE_ID").equalsIgnoreCase(
							"null")) {
						governorateId = obj.getString("GOVERNORATE_ID");
					}

					if (GOYSApplication.getInstance().isEnglishLanguage()) {
						if (!obj.getString("AREA").equalsIgnoreCase("null")) {
							etArea.setText(obj.getString("AREA"));
						}
					} else {
						if (!obj.getString("AREA_AR").equalsIgnoreCase("null")) {
							etArea.setText(obj.getString("AREA_AR"));
						}
					}

				} catch (JSONException e) {
					GoysLog.e(TAG, "Error: " + e.toString());
					CommonActions.showErrorAlertDialog(
							getActivity(),
							0,
							getResources().getString(
									R.string.applicationundermaintenance));
					e.printStackTrace();
				}
			}

			if (serviceId == AppConstants.MAINTENANCE_GET_FACILITIES_BY_SERVICE_TYPE) {
				Log.d("Response Facilities List", responseObj);
				parseFacilitiesJson(responseObj);

			}

		} else {
			CommonActions.alertDialogShow(getActivity(), getResources()
					.getString(R.string.alert_error_title), getResources()
					.getString(R.string.applicationundermaintenance));

		}

	}

	@Override
	public void onError(int responseCode, String msg) {
		if (getActivity() != null) {
			CommonActions
					.showErrorAlertDialog(getActivity(), responseCode, msg);
		}

	}

	private void parseFacilitiesJson(String responseObj) {
		try {

			JSONArray jsonArray = new JSONArray(responseObj);
			JSONObject jsonObject = null;
			facilitiesList = new String[jsonArray.length()];
			facilitiesListModel = new ArrayList<ServiceData>();

			for (int i = 0; i < jsonArray.length(); i++) {

				jsonObject = jsonArray.getJSONObject(i);
				facilitiesListModel.add(new ServiceData(jsonObject
						.getString("ID"), jsonObject.getString("Value")));

				facilitiesList[i] = jsonObject.getString("Value");

			}

		} catch (JSONException e) {
			GoysLog.e(TAG, "Error: " + e.toString());

		}

		if (facilitiesList != null) {
			setSpinnerAdaptor(spFacilityName, facilitiesList);
			spFacilityName.setEnabled(true);
		} else {

		}

	}

	private void parseServiceTypeJson(String responseObj) {
		JSONArray jsonServiceArray = null, jsonMaintenaceArray = null, jsonGovernorateArray = null;
		JSONObject jsonReader = null;
		String[] serviveTypesList = null;

		serviceTypesModel = new ArrayList<ServiceData>();

		try {
			jsonReader = new JSONObject(responseObj);
			jsonServiceArray = jsonReader.getJSONArray("ServiceType");
			jsonMaintenaceArray = jsonReader.getJSONArray("MaintenanceType");
			jsonGovernorateArray = jsonReader.getJSONArray("Governorate");

			ca.saveUserPreferences("Governorate",
					jsonGovernorateArray.toString());

			serviveTypesList = new String[jsonServiceArray.length()];
			// facilitiesList = new String[jsonFacilityArray.length()];
			governorateList = new String[jsonGovernorateArray.length()];

		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (serviveTypesList != null) {
			// Parsing Service Types
			for (int i = 0; i < jsonServiceArray.length(); i++) {
				try {
					jsonReader = jsonServiceArray.getJSONObject(i);

					serviceTypesModel.add(new ServiceData(jsonReader
							.getString("ID"), jsonReader.getString("Value")));

					serviveTypesList[i] = jsonReader.getString("Value");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			serviceTypes = serviveTypesList;

			SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),
					serviveTypesList);
			spServiceType.setAdapter(adapter);
			spServiceType.setEnabled(true);
		} else {
			// Show Error
		}

		// Parsing Maintenance Names
		if (jsonMaintenaceArray != null) {
			maintenanceListModel = new ArrayList<ServiceData>();
			for (int i = 0; i < jsonMaintenaceArray.length(); i++) {
				try {
					// facilitiesList[i] = jsonMaintenaceArray.getString(i);

					jsonReader = jsonMaintenaceArray.getJSONObject(i);

					maintenanceListModel.add(new ServiceData(jsonReader
							.getString("ID"), jsonReader.getString("Value")));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		if (governorateList != null) {
			// Parsing Governorate
			governateListModel = new ArrayList<ServiceData>();

			for (int i = 0; i < jsonGovernorateArray.length(); i++) {
				try {

					jsonReader = jsonGovernorateArray.getJSONObject(i);

					governateListModel.add(new ServiceData(jsonReader
							.getString("ID"), jsonReader.getString("Value")));

					governorateList[i] = jsonReader.getString("Value");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			setSpinnerAdaptor(spGovernorate, governorateList);
			spGovernorate.setEnabled(true);
			((MaintenanceFormActivity) getActivity()).isGovernorateEnable = true;

		} else {
			// Show Error Here
		}

		/*
		 * setSpinnerAdaptor(spFacilityName, facilitiesList);
		 * spFacilityName.setEnabled(true);
		 */
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(contentUri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void viewFileDialogWithFewOption() {

		final CharSequence[] items = {
				getResources().getString(R.string.takePicture),
				getResources().getString(R.string.picfromGal),
				getResources().getString(R.string.cancel) };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getResources().getString(R.string.picker_dialog_title));
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				List<Intent> targetShareIntents = new ArrayList<Intent>();

				if (items[item].equals(getResources().getString(
						R.string.takePicture))) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					imageUri = ImageServices
							.getOutputImageFileUri(getActivity());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					MaintenancePublicUserFragment.this.startActivityForResult(
							intent, AppConstants.CAPTURE_IMAGE);

				} else if (items[item].equals(getResources().getString(
						R.string.picfromGal))) {

					// Implicitly allow the user to select a particular kind of
					// data
					Intent target = new Intent(Intent.ACTION_GET_CONTENT);
					// The MIME data type filter
					target.setType("*/*");
					// Only return URIs that can be opened with ContentResolver
					target.addCategory(Intent.CATEGORY_OPENABLE);

					List<ResolveInfo> resInfos = getActivity()
							.getPackageManager().queryIntentActivities(target,
									0);
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
							MaintenancePublicUserFragment.this
									.startActivityForResult(chooserIntent,
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int id = v.getId();

		switch (id) {
		case R.id.sp_service_type:
		case R.id.sp_facility_name:
		case R.id.sp_governorate:
			hideSoftKeyboard();
			break;

		default:
			break;
		}

		return false;
	}

}
