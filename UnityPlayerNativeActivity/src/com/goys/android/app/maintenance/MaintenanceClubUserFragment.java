package com.goys.android.app.maintenance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.andorid.components.afilechooser.utils.FileUtils;
import com.google.gson.JsonArray;
import com.goys.android.app.GOYSApplication;
import com.goys.android.app.R;
import com.goys.android.app.db.model.Facilities;
import com.goys.android.app.db.model.MaintenanceClub;
import com.goys.android.app.db.model.ServiceData;
import com.goys.android.app.emigration.Attachment;
import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.CommonObjects;
import com.goys.android.app.util.GPSTracker;
import com.goys.android.app.util.GoysLog;
import com.goys.android.app.util.GoysService;
import com.goys.android.app.util.ImageServices;
import com.goys.android.app.util.MyAttachmentAdapter;
import com.goys.android.app.util.ResponseListener;

public class MaintenanceClubUserFragment extends Fragment implements
		ResponseListener, OnTouchListener {

	private static final String TAG = "MaintenanceClubUserFragment";

	View mainView;

	TextView labelAttachment, instruction, labelServiceError,
			labelFacilityName, labelMaintenanceType, labelGovernorateError;

	MaintenanceClub clubFormItem;

	MaintenanceClub[] clubFormItemList;

	ImageButton ibDatePicker, ibManual, ibGPS;

	Button bSubmit, bChooseFile;

	EditText etPinCode, etEmail, etJobDescription, etPlaceRequiredMaintenance,
			etBuildingNo, etRoadNo, etBlockNo, etArea, etDate, etGovernorate,
			etFacilityName;

	Spinner spServiceType, spGovernorate, spMaintenanceType;

	RadioGroup rgLocationDetail;

	// Variable for storing current date
	private int mYear, mMonth, mDay;

	ListView lv_attached_files;

	private String selectedImagePath = "";

	private ArrayList<Attachment> attachmentList;

	MyAttachmentAdapter myAdapter;

	GoysService gs;

	GPSTracker gps;

	Facilities facilitiesItem;

	ArrayList<Facilities> facilitiesList = new ArrayList<Facilities>();

	Uri imageUri;

	String[] facilitiesStringList, emailList = null;

	String[] serviceTypes = null;

	String[] governorateList = null;

	Boolean isRotate = false, ScreenRotation = false;
	String tempPinCode = "";

	CommonActions ca;

	ArrayList<ServiceData> facilitiesListModel = null;
	ArrayList<ServiceData> serviceTypesModel = null;
	ArrayList<ServiceData> governateListModel = null;
	ArrayList<ServiceData> maintenanceListModel = null;

	String facilitiesNameId = null;
	String governorateId = null;

	LinearLayout llChooseFile, llGovernorate, llMaintenanceType,
			llServiceTypes;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState == null) {
			attachmentList = new ArrayList<Attachment>();
		} else {
			isRotate = true;
			bChooseFile.setFocusableInTouchMode(true);
			bChooseFile.requestFocus();

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
				serviceTypesModel = savedInstanceState
						.getParcelableArrayList("serviceTypesModel");

				ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
						getActivity(), R.layout.item_spinner_header,
						serviceTypes);
				spinnerArrayAdapter1
						.setDropDownViewResource(R.layout.item_spinner);
				spServiceType.setAdapter(spinnerArrayAdapter1);
				if (serviceTypes.length > 0) {
					spServiceType.setEnabled(true);
				}
			}

			if (savedInstanceState.getStringArray("governorateList") != null) {
				governorateList = savedInstanceState
						.getStringArray("governorateList");

				governateListModel = savedInstanceState
						.getParcelableArrayList("governateListModel");

				setSpinnerAdaptor(spGovernorate, governorateList);

				if (governorateList.length > 0) {
					spGovernorate.setEnabled(true);
				}
			}

			if (savedInstanceState.getParcelableArrayList("emailList") != null) {
				facilitiesList = savedInstanceState
						.getParcelableArrayList("emailList");

				setSpinnerAdaptor(
						spMaintenanceType,
						getActivity().getResources().getStringArray(
								R.array.maintenance_type_array));
				spMaintenanceType.setEnabled(true);
			}

			if (savedInstanceState
					.getParcelableArrayList("maintenanceListModel") != null) {
				maintenanceListModel = savedInstanceState
						.getParcelableArrayList("maintenanceListModel");
			}

			if (savedInstanceState.getString("tempPinCode") != null) {
				tempPinCode = savedInstanceState.getString("tempPinCode");
			}

			if (savedInstanceState.getString("email") != null) {
				spGovernorate.setEnabled(true);
				etEmail.setText(savedInstanceState.getString("email"));
				etFacilityName.setText(savedInstanceState
						.getString("etFacilityName"));
				facilitiesNameId = savedInstanceState
						.getString("facilitiesNameId");
			}

			ScreenRotation = savedInstanceState.getBoolean("ScreenRotation");
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		isRotate = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			isRotate = true;
		}

		mainView = inflater.inflate(
				R.layout.fragment_maintenance_form_clubuser, null);

		initView();
		initObj();

		return mainView;
	}

	private void initObj() {
		gs = new GoysService(getActivity());
		gs.setResponseListener(MaintenanceClubUserFragment.this);
		gps = new GPSTracker(getActivity());

		ca = new CommonActions(getActivity());

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

				GoysLog.d(TAG, "gps.isGPSEnable() " + gps.isGPSEnable());
				if (!isRotate) {
					if (gps.isGPSEnable()) {
						if (gps.canGetLocation()) {

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
					// isRotate = false;
				}

			}
		});

		ibDatePicker = (ImageButton) mainView.findViewById(R.id.ib_date_picker);
		ibDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		etPinCode = (EditText) mainView.findViewById(R.id.et_pin_code);

		etPinCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.d("Text Watcher", "Charseq:" + s.toString() + "\nStart:"
						+ start + "\nBefore:" + before + "\nCount" + count);
				if (count == 0) {

					etFacilityName.setText(null);
					etEmail.setText(null);
					spMaintenanceType.setEnabled(false);
					setSpinnerAdaptor(
							spMaintenanceType,
							getActivity().getResources().getStringArray(
									R.array.maintenance_type_array));
					spServiceType.setEnabled(false);
					setSpinnerAdaptor(
							spServiceType,
							getActivity()
									.getResources()
									.getStringArray(
											R.array.service_type_public_emergency_arrays));

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		etPinCode.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (!isRotate) {
						if (((MaintenanceFormActivity) getActivity()).pager
								.getCurrentItem() == 1) {
							if (gs.validatePinCode(getActivity(), etPinCode)) {

								if (CommonActions.hasConnection(getActivity())) {
									gs.maintenancePinCodeService(getActivity(),
											etPinCode.getText().toString());
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

							} else {
								etPinCode.post(new Runnable() {
									@Override
									public void run() {
										etPinCode.requestFocus();
									}
								});

							}
						}
					}

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
		etArea = (EditText) mainView.findViewById(R.id.et_area);
		etGovernorate = (EditText) mainView.findViewById(R.id.et_governorate);
		etEmail = (EditText) mainView.findViewById(R.id.et_email);
		etDate = (EditText) mainView.findViewById(R.id.et_date);
		lv_attached_files = (ListView) mainView
				.findViewById(R.id.attached_files_listview);
		labelAttachment = (TextView) mainView
				.findViewById(R.id.label_attachments);
		labelServiceError = (TextView) mainView
				.findViewById(R.id.label_service_type_error);
		labelFacilityName = (TextView) mainView
				.findViewById(R.id.label_facility_name_error);
		labelMaintenanceType = (TextView) mainView
				.findViewById(R.id.label_maintenance_type_error);
		labelGovernorateError = (TextView) mainView
				.findViewById(R.id.label_governorate_error);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// etDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
		etDate.setText(android.text.format.DateFormat.format("dd MMMM, yyyy",
				new java.util.Date()));

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

		llServiceTypes = (LinearLayout) mainView
				.findViewById(R.id.ll_service_types);
		llServiceTypes.setFocusableInTouchMode(true);
		spServiceType = (Spinner) mainView.findViewById(R.id.sp_service_type);
		spServiceType.setOnTouchListener(this);
		// spServiceType.setFocusableInTouchMode(true);
		spServiceType.setEnabled(false);
		setSpinnerAdaptor(spServiceType, getActivity().getResources()
				.getStringArray(R.array.service_type_public_emergency_arrays));

		spServiceType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= 0) {
					labelServiceError.setVisibility(View.GONE);

					((TextView) parent.getChildAt(0))
							.setTextColor(getResources()
									.getColor(R.color.black));

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		etFacilityName = (EditText) mainView.findViewById(R.id.et_facility);

		llGovernorate = (LinearLayout) mainView
				.findViewById(R.id.ll_governorate);
		llGovernorate.setFocusableInTouchMode(true);

		spGovernorate = (Spinner) mainView.findViewById(R.id.sp_governorate);
		spGovernorate.setOnTouchListener(this);
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

		llMaintenanceType = (LinearLayout) mainView
				.findViewById(R.id.ll_maintenance_type);
		llMaintenanceType.setFocusableInTouchMode(true);
		spMaintenanceType = (Spinner) mainView
				.findViewById(R.id.sp_maintenance_type);
		spMaintenanceType.setOnTouchListener(this);
		// spMaintenanceType.setFocusableInTouchMode(true);
		/*
		 * if (etEmail.getText().toString() != null) {
		 * spMaintenanceType.setEnabled(true); } else {
		 * spMaintenanceType.setEnabled(false); }
		 */
		spMaintenanceType.setEnabled(false);

		setSpinnerAdaptor(spMaintenanceType, getActivity().getResources()
				.getStringArray(R.array.maintenance_type_array));

		spMaintenanceType.post(new Runnable() {
			public void run() {
				spMaintenanceType
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {

								if (position >= 0) {
									labelMaintenanceType
											.setVisibility(View.GONE);

									((TextView) parent.getChildAt(0))
											.setTextColor(getResources()
													.getColor(R.color.black));

									if (!isRotate) {
										Log.d("isRotate", isRotate + "");

										if (CommonActions
												.hasConnection(getActivity())) {
											String maintenanceType;
											if (position == 0) {
												maintenanceType = "normal";
											} else {
												maintenanceType = "emergency";
											}
											gs.getServiceTypeService1(
													(MaintenanceFormActivity) getActivity(),
													etFacilityName.getText()
															.toString(),
													maintenanceType);

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
									} else {
										isRotate = false;
									}
								} else {
									spServiceType.setEnabled(false);
									setSpinnerAdaptor(
											spServiceType,
											getActivity()
													.getResources()
													.getStringArray(
															R.array.service_type_public_emergency_arrays));

								}

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
							}
						});
			}
		});

		llChooseFile = (LinearLayout) mainView
				.findViewById(R.id.ll_choose_file);
		llChooseFile.setFocusableInTouchMode(true);
		bChooseFile = (Button) mainView.findViewById(R.id.b_chooseFile);
		bChooseFile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// viewFileDialogWithFewOption();

				if (tempPinCode.equals(etPinCode.getText().toString())) {
					llChooseFile.requestFocus();
					viewFileDialogWithFewOption();

				} else {
					if (CommonActions.hasConnection(getActivity())) {
						if (gs.validatePinCode(getActivity(), etPinCode)) {
							gs.maintenancePinCodeService(getActivity(),
									etPinCode.getText().toString());
						} else {
							etPinCode.post(new Runnable() {
								@Override
								public void run() {
									etPinCode.requestFocus();
								}
							});

						}

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

		bSubmit = (Button) mainView.findViewById(R.id.b_submit);
		bSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clubFormItemList = new MaintenanceClub[2];

				storeFormDataIntoModel();

				if (validateForm(clubFormItem)) {

					if (CommonActions.hasConnection(getActivity())) {

						if (tempPinCode.equals(etPinCode.getText().toString())) {
							clubFormItemList[0] = clubFormItem;
							gs.MaintenanceClubFormService(
									(MaintenanceFormActivity) getActivity(),
									clubFormItemList);
						} else {
							gs.maintenancePinCodeService(getActivity(),
									etPinCode.getText().toString());
						}

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

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (!ScreenRotation) {
				if (((MaintenanceFormActivity) getActivity()).isGovernorateEnable) {
					if (governorateList == null) {
						parseGovernorateList();
					}
				} else {
					spGovernorate.setEnabled(false);
				}
				etPinCode.requestFocus();
				// ScreenRotation = false;
			}

			if (!tempPinCode.equals(etPinCode.getText().toString())) {
				// etPinCode.requestFocus();

			}
		} else {

		}
	}

	public void parseGovernorateList() {

		JSONArray jsonGovernorateArray = null;
		JSONObject jsonReader = null;

		try {
			jsonGovernorateArray = new JSONArray(ca.getValueString(
					"Governorate", ""));

			governorateList = new String[jsonGovernorateArray.length()];
		} catch (JSONException e) {
			e.printStackTrace();
		}

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

		if (governorateList != null && governorateList.length > 0) {
			setSpinnerAdaptor(spGovernorate, governorateList);
			spGovernorate.setEnabled(true);
		} else {
			spGovernorate.setEnabled(false);
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

		if (facilitiesList.size() > 0) {
			outState.putParcelableArrayList("emailList", facilitiesList);
		}

		if (maintenanceListModel != null) {
			outState.putParcelableArrayList("maintenanceListModel",
					maintenanceListModel);
		}

		if (governorateList != null) {
			outState.putStringArray("governorateList", governorateList);
			outState.putParcelableArrayList("governateListModel",
					governateListModel);
		}

		if (!tempPinCode.equalsIgnoreCase("")) {
			outState.putString("tempPinCode", tempPinCode);
		}

		if (etEmail.getText().toString() != null) {
			outState.putString("email", etEmail.getText().toString());
			outState.putString("etFacilityName", etFacilityName.getText()
					.toString());
			outState.putString("facilitiesNameId", facilitiesNameId);
		}

		outState.putBoolean("ScreenRotation", true);

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

						if (clubFormItem == null) {
							clubFormItem = new MaintenanceClub();
						}
						clubFormItem.setAttachment(attachmentList);
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
				//
				// attachedFiles.add(selectedImagePath);
				lv_attached_files.setVisibility(View.VISIBLE);

				attachmentList.add(new Attachment(fileName, selectedImagePath));
				labelAttachment.setVisibility(View.GONE);

				if (clubFormItem == null) {
					clubFormItem = new MaintenanceClub();
				}
				clubFormItem.setAttachment(attachmentList);

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

	public boolean validateForm(MaintenanceClub clubFormItem) {
		boolean validate = true;

		// Date
		if (CommonObjects.isEmpty(clubFormItem.getDate())) {
			etDate.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else {
			etDate.setError(null);
		}

		// File Attachment
		if (clubFormItem.getAttachment() == null) {
			labelAttachment.setVisibility(View.VISIBLE);
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
			if (CommonObjects.isEmpty(clubFormItem.getGovernorate())) {
				etGovernorate.requestFocus();
				etGovernorate.setError(getResources().getString(
						(R.string.emptyField)));
				validate = false;
			} else {
				etGovernorate.setError(null);
			}
		}

		// Area
		if (CommonObjects.isEmpty(clubFormItem.getArea())) {
			etArea.requestFocus();
			etArea.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else {
			etArea.setError(null);
		}

		// Block No
		if (CommonObjects.isEmpty(clubFormItem.getBlockNo())) {
			etBlockNo.requestFocus();
			etBlockNo.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} /*else if (!CommonObjects.isEmpty(clubFormItem.getBlockNo())
				&& Pattern.compile(AppConstants.ALPHA_NUMERIC_SPACE)
						.matcher(clubFormItem.getBlockNo()).find()) {
			etBlockNo.setError(getActivity().getResources().getString(
					R.string.onlyCharacterAndNumberAllow));
			etBlockNo.requestFocus();
			validate = false;
		}*/ else {
			etBlockNo.setError(null);
		}

		// Road No
		if (CommonObjects.isEmpty(clubFormItem.getRoadNo())) {
			etRoadNo.requestFocus();
			etRoadNo.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else if (!CommonObjects.isEmpty(clubFormItem.getRoadNo())
				&& Pattern.compile(AppConstants.ALPHA_NUMERIC_SPACE)
						.matcher(clubFormItem.getRoadNo()).find()) {
			etRoadNo.setError(getActivity().getResources().getString(
					R.string.onlyCharacterAndNumberAllow));
			etRoadNo.requestFocus();
			validate = false;
		} else {
			etRoadNo.setError(null);
		}

		// Building No
		if (CommonObjects.isEmpty(clubFormItem.getBuildingNo())) {
			etBuildingNo.requestFocus();
			etBuildingNo.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else {
			etBuildingNo.setError(null);
		}

		if (CommonObjects.isEmpty(clubFormItem.getPlaceRequiredMaintenance())) {
			etPlaceRequiredMaintenance.requestFocus();
			etPlaceRequiredMaintenance.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else {
			etPlaceRequiredMaintenance.setError(null);
		}

		if (CommonObjects.isEmpty(clubFormItem.getJobDescription())) {
			etJobDescription.requestFocus();
			etJobDescription.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else {
			etJobDescription.setError(null);
		}

		if (spServiceType.getSelectedItemPosition() < 0) {
			// spServiceType.requestFocus();
			llServiceTypes.requestFocus();
			labelServiceError.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (spMaintenanceType.getSelectedItemPosition() < 0) {

			// spMaintenanceType.requestFocus();
			llMaintenanceType.requestFocus();
			labelMaintenanceType.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (CommonObjects.isEmpty(clubFormItem.getEmail())) {
			etEmail.requestFocus();
			etEmail.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		}

		if (CommonObjects.isEmpty(clubFormItem.getFacilityName())) {

			etFacilityName.requestFocus();
			labelFacilityName.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (CommonObjects.isEmpty(clubFormItem.getPinCode())) {
			etPinCode.requestFocus();
			etPinCode.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		} else if (Pattern.compile(AppConstants.SYMBOLS_PATTERN)
				.matcher(clubFormItem.getPinCode()).find()) {
			validate = false;
			etPinCode.requestFocus();
			etPinCode.setError(getResources().getString(
					R.string.onlyCharacterAndNumberAllow));
		} else if (clubFormItem.getPinCode().length() < 6) {
			etPinCode.requestFocus();
			etPinCode.setError(getResources().getString(
					R.string.invalidePinCodeLength));
		} else if (clubFormItem.getPinCode().length() > 6) {
			etPinCode.requestFocus();
			etPinCode.setError(getResources().getString(
					R.string.invalidePinCodeLength));
		} else {
			etPinCode.setError(null);
		}

		return validate;
	}

	public void storeFormDataIntoModel() {
		clubFormItem = new MaintenanceClub();

		clubFormItem.setPinCode(etPinCode.getText().toString());
		// clubFormItem.setFacilityName(etFacilityName.getText().toString());
		clubFormItem.setFacilityName(facilitiesNameId);

		clubFormItem.setEmail(etEmail.getText().toString());
		if (spMaintenanceType.getSelectedItemPosition() >= 0) {
			/*
			 * clubFormItem.setMaintenanceType(spMaintenanceType.getSelectedItem(
			 * ) .toString());
			 */
			clubFormItem.setMaintenanceType(getMaintenanceId());
		}
		if (spServiceType.getSelectedItemPosition() >= 0) {
			/*
			 * clubFormItem.setServiceType(spServiceType.getSelectedItem()
			 * .toString());
			 */
			clubFormItem.setServiceType(serviceTypesModel.get(
					spServiceType.getSelectedItemPosition()).getID());
		}
		clubFormItem.setJobDescription(etJobDescription.getText().toString());
		clubFormItem.setPlaceRequiredMaintenance(etPlaceRequiredMaintenance
				.getText().toString());
		clubFormItem.setBuildingNo(etBuildingNo.getText().toString());
		clubFormItem.setRoadNo(etRoadNo.getText().toString());
		clubFormItem.setBlockNo(etBlockNo.getText().toString());
		clubFormItem.setArea(etArea.getText().toString());
		if (spGovernorate.isShown()) {
			if (spGovernorate.getSelectedItemPosition() >= 0) {
				/*
				 * clubFormItem.setGovernorate(spGovernorate.getSelectedItem()
				 * .toString());
				 */
				clubFormItem.setGovernorate(governateListModel.get(
						spGovernorate.getSelectedItemPosition()).getID()
						+ "");
			} else {
				clubFormItem.setGovernorate("");
			}
		} else {
			// clubFormItem.setGovernorate(etGovernorate.getText().toString());
			clubFormItem.setGovernorate(governorateId);
		}

		// clubFormItem.setDate(etDate.getText().toString());
		/*
		 * SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM,yyyy",
		 * Locale.ENGLISH); String formattedDate = dateFormat.format(new
		 * Date());
		 */
		/*
		 * final Calendar c = Calendar.getInstance(); String formattedDate =
		 * c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) +
		 * "/" + c.get(Calendar.YEAR);
		 */
		Date cDate = new Date();
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy",
				Locale.ENGLISH).format(cDate);

		clubFormItem.setDate(formattedDate);

		if (attachmentList.size() > 0) {
			clubFormItem.setAttachment(attachmentList);
		}

		if (ibManual.isSelected()) {
			clubFormItem.setLocationType("Manual");
		} else {
			clubFormItem.setLocationType("GPS");
		}

	}

	public String getMaintenanceId() {
		for (int i = 0; i < maintenanceListModel.size(); i++) {

			if (spMaintenanceType.getSelectedItem().toString()
					.equalsIgnoreCase(maintenanceListModel.get(i).getValue())) {
				return maintenanceListModel.get(i).getID();
			}
		}

		return "-1";
	}

	private void resetFieldsOnPinCodeService() {

		etFacilityName.setText(null);
		etEmail.setText(null);
		spMaintenanceType.setEnabled(false);
		setSpinnerAdaptor(spMaintenanceType, getActivity().getResources()
				.getStringArray(R.array.maintenance_type_array));
		spServiceType.setEnabled(false);
		setSpinnerAdaptor(spServiceType, getActivity().getResources()
				.getStringArray(R.array.service_type_public_emergency_arrays));

	}

	private void clearValuesForEditText(EditText editText) {

		editText.setText("");
		editText.setError(null);
	}

	public void clearForm() {

		clearValuesForEditText(etPinCode);
		clearValuesForEditText(etEmail);
		clearValuesForEditText(etJobDescription);
		clearValuesForEditText(etPlaceRequiredMaintenance);
		clearValuesForEditText(etBuildingNo);
		clearValuesForEditText(etRoadNo);
		clearValuesForEditText(etBlockNo);
		clearValuesForEditText(etArea);
		clearValuesForEditText(etDate);
		clearValuesForEditText(etGovernorate);
		clearValuesForEditText(etFacilityName);

		spGovernorate.setSelection(0);
		setSpinnerAdaptor(spMaintenanceType, getActivity().getResources()
				.getStringArray(R.array.maintenance_type_array));

		spServiceType.setSelection(0);

		attachmentList = new ArrayList<Attachment>();
		lv_attached_files.setVisibility(View.GONE);
		labelAttachment.setVisibility(View.GONE);

		etPinCode.requestFocus();

	}

	@Override
	public void onResponse(int serviceId, String responseObj) {

		if (getActivity() != null && responseObj != null
				&& !responseObj.contains("Error") && !responseObj.equals("")) {
			if (serviceId == AppConstants.MAINTENANCE_CLUB_SERVICE_ID) {

				String msg = getResources().getString(
						R.string.successful_submission_msg)
						+ " " + responseObj;

				// play success submission sound
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

			if (serviceId == AppConstants.MAINTENANCE_VALIDATE_PINCODE_SERVICE_ID) {

				try {
					resetFieldsOnPinCodeService();

					JSONArray jsonArray = new JSONArray(responseObj);
					JSONObject obj = new JSONObject();

					obj = jsonArray.getJSONObject(0);
					boolean isValid = obj.getBoolean("Valid");

					if (isValid) {

						// Parse Response Here

						parseFacilitiesNameJson(jsonArray);

						/* storing a valid pinCode after verified */
						tempPinCode = etPinCode.getText().toString();

						etEmail.setError(null);
						etPinCode.setError(null);

					} else {
						etPinCode.requestFocus();
						etPinCode.setError(getResources().getString(
								(R.string.invalidePinCode)));

						etEmail.setText("");
					}

				} catch (JSONException e) {
				}

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

					e.printStackTrace();
				}
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

	private void parseFacilitiesNameJson(JSONArray jsonArray) {
		JSONObject obj = new JSONObject();

		String facilitiesName = null;

		String emailAddress = null;

		facilitiesList = new ArrayList<Facilities>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {

				obj = jsonArray.getJSONObject(i);
				facilitiesList
						.add(new Facilities(obj.getString("FacilitiesNameEn"),
								obj.getString("FacilitiesNameAr"), obj
										.getString("Email")));

				if (GOYSApplication.getInstance().isEnglishLanguage()) {
					facilitiesName = obj.getString("FacilitiesNameEn");
				} else {
					facilitiesName = obj.getString("FacilitiesNameAr");
				}
				emailAddress = obj.getString("Email");
				facilitiesNameId = obj.getString("FacilitiesID");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		etFacilityName.setText(facilitiesName);
		etEmail.setText(emailAddress);

		spMaintenanceType.setEnabled(true);
		labelFacilityName.setVisibility(View.GONE);

	}

	private void parseServiceTypeJson(String responseObj) {
		JSONArray jsonArray = null, jsonMaintenaceArray = null;
		JSONObject jsonReader = null;
		String[] serviceTypeList = null;

		try {
			jsonReader = new JSONObject(responseObj);
			jsonArray = jsonReader.getJSONArray("ServiceType");

			jsonMaintenaceArray = jsonReader.getJSONArray("MaintenanceType");
			// jsonGovernorateArray = jsonReader.getJSONArray("Governorate");

			serviceTypeList = new String[jsonArray.length()];
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (serviceTypeList != null) {
			serviceTypesModel = new ArrayList<ServiceData>();

			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					// Log.d("JsonARRAY", jsonArray.getString(i));

					jsonReader = jsonArray.getJSONObject(i);

					serviceTypesModel.add(new ServiceData(jsonReader
							.getString("ID"), jsonReader.getString("Value")));

					serviceTypeList[i] = jsonReader.getString("Value");

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			serviceTypes = serviceTypeList;
			spServiceType.setEnabled(true);
			setSpinnerAdaptor(spServiceType, serviceTypeList);

		} else {
			CommonActions.alertDialogShow(getActivity(), getResources()
					.getString(R.string.alert_error_title), getActivity()
					.getResources().getString(R.string.error_service_type));

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

		/*
		 * if (serviceTypeList.length == 0) {
		 * CommonActions.alertDialogShow(getActivity(), "", getActivity()
		 * .getResources().getString(R.string.error_service_type)); } else {
		 * spServiceType.setEnabled(true); }
		 * 
		 * setSpinnerAdaptor(spServiceType, serviceTypeList);
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
					MaintenanceClubUserFragment.this.startActivityForResult(
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
							MaintenanceClubUserFragment.this
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();

		switch (id) {
		case R.id.sp_service_type:
		case R.id.sp_maintenance_type:
		case R.id.sp_governorate:
			hideSoftKeyboard();
			break;

		default:
			break;
		}

		return false;
	}

}
