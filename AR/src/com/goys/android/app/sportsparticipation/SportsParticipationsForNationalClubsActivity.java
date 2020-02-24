package com.goys.android.app.sportsparticipation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import com.goys.android.app.emigration.Attachment;
import com.goys.android.app.emigration.EmigrationAndVisaActivity;
import com.goys.android.app.util.AppConstants;
import com.goys.android.app.util.CommonActions;
import com.goys.android.app.util.CommonObjects;
import com.goys.android.app.util.GoysLog;
import com.goys.android.app.util.GoysService;
import com.goys.android.app.util.ImageServices;
import com.goys.android.app.util.ResponseListener;

/*
 * 
 * This class represents Sports Participation Form page screen
 * 
 */
public class SportsParticipationsForNationalClubsActivity extends
		ActionbarActivity implements ResponseListener {
	private static final String TAG = "ActivityEmigrationAndVisa";

	Button submit;
	Button chooseFile;
	EditText et_pinCode;
	EditText et_clubName;
	EditText et_notes;
	EditText et_championship_and_gamename;

	RadioGroup rg_residenciesRadio;

	RadioButton rb_residencies;

	Spinner spin_country;

	Spinner spin_residencies;

	TextView attachement_label_p_date;

	TextView attachement_label_c_concerned;

	TextView attachement_label_p_copy;

	TextView attachement_label_p_list;

	TextView attachement_label_bankAccountDetial;

	TextView attachement_label_t_list;

	//TextView attachement_label_p_budgetForm;

	TextView attachement_label_quotationsSupport;

	//TextView tvParticipationBudgetForm;

	String imgPath;

	private String selectedImagePath = "";

	ArrayList<Attachment> attachedFilesForParticipationDate;
	ListView lv_participationDate;

	ArrayList<Attachment> attachedFilesForConsentOfConcerned;
	ListView lv_consentOfConcerned;

	ArrayList<Attachment> attachedFilesForParticipationCopy;
	ListView lv_participationCopy;

	ArrayList<Attachment> attachedFilesForParticipationList;
	ListView lv_participationList;

	ArrayList<Attachment> attachedFilesForBankAccountDetail;
	ListView lv_bankAccountDetail;

	ArrayList<Attachment> attachedFilesForTournamentList;
	ListView lv_tournamentList;

	/*ArrayList<Attachment> attachedFilesForParticipationBudgetForm;
	ListView lv_participationBudgetForm;
	*/
	
	ArrayList<Attachment> attachedFilesForQuotationsAndSupporting;
	ListView lv_quotationsAndSupporting;

	// Objects for this Activity

	Button chooseFileForParticipationDate;

	Button chooseFileForConsentOfConcerned;

	Button chooseFileForParticipationCopy;

	Button chooseFileForParticipationList;

	Button chooseFileForBankAccountDetail;

	Button chooseFileForTournamentList;

	//Button chooseFileForParticipationBudgetForm;

	Button chooseFileForQuotationsAndSupporting;

	static int buttonId;

	Dialog dialog;

	TextView tvPopup;

	Uri imageUri;

	Button btn_refernce;
	Button btn_clear;
	AttachementListArrayAdapter adapter;

	SportsParticipationFormBean formbean;
	SportsParticipationFormBean[] formBeanList;

	ProgressDialog pdialog;
	GoysService gs;

	String tempPinCode="";

	boolean isActivityAvailable;

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
				R.string.menu_sports_participation));
		setIsParent(false);
		setActionbarColor(getResources().getColor(R.color.action_bar_red));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sports_participations_for_national_clubs);

		gs = new GoysService(this);
		gs.setResponseListener(this);

		initializeView();
		/*
		 * handling saveInstanceState for less then 4.4 OS in which capture
		 * image intent call caller activity onCreate after take picture
		 */
		if (savedInstanceState == null) {

			attachedFilesForParticipationDate = new ArrayList<Attachment>();

			attachedFilesForConsentOfConcerned = new ArrayList<Attachment>();

			attachedFilesForParticipationCopy = new ArrayList<Attachment>();

			attachedFilesForParticipationList = new ArrayList<Attachment>();

			attachedFilesForBankAccountDetail = new ArrayList<Attachment>();

			attachedFilesForTournamentList = new ArrayList<Attachment>();

			//attachedFilesForParticipationBudgetForm = new ArrayList<Attachment>();

			attachedFilesForQuotationsAndSupporting = new ArrayList<Attachment>();

		} else {

			String fileUri = savedInstanceState.getString("file-uri");
			if (!fileUri.equals("")) {
				imageUri = Uri.parse(fileUri);
			}

			attachedFilesForParticipationDate = savedInstanceState
					.getParcelableArrayList("participation_date");

			attachedFilesForConsentOfConcerned = savedInstanceState
					.getParcelableArrayList("consent_of_concerned");

			attachedFilesForParticipationCopy = savedInstanceState
					.getParcelableArrayList("participation_copy");

			attachedFilesForParticipationList = savedInstanceState
					.getParcelableArrayList("participation_list");

			attachedFilesForBankAccountDetail = savedInstanceState
					.getParcelableArrayList("bank_account_detail");

			attachedFilesForTournamentList = savedInstanceState
					.getParcelableArrayList("tournament_list");
			/*
			attachedFilesForParticipationBudgetForm = savedInstanceState
					.getParcelableArrayList("budget_form");
			*/
			attachedFilesForQuotationsAndSupporting = savedInstanceState
					.getParcelableArrayList("quotation_supporting");

		}

		if (attachedFilesForParticipationDate.size() > 0) {
			updateListView(attachedFilesForParticipationDate,
					lv_participationDate);

			formbean.setAttachedFilesForParticipationDate(attachedFilesForParticipationDate
					.get(0));
		}

		if (attachedFilesForConsentOfConcerned.size() > 0) {
			updateListView(attachedFilesForConsentOfConcerned,
					lv_consentOfConcerned);

			formbean.setAttachedFilesForConsentOfConcerned(attachedFilesForConsentOfConcerned
					.get(0));
		}

		if (attachedFilesForParticipationCopy.size() > 0) {
			updateListView(attachedFilesForParticipationCopy,
					lv_participationCopy);

			formbean.setAttachedFilesForParticipationCopy(attachedFilesForParticipationCopy
					.get(0));
		}

		if (attachedFilesForParticipationList.size() > 0) {
			updateListView(attachedFilesForParticipationList,
					lv_participationList);

			formbean.setAttachedFilesForParticipationList(attachedFilesForParticipationList
					.get(0));
		}

		if (attachedFilesForBankAccountDetail.size() > 0) {
			updateListView(attachedFilesForBankAccountDetail,
					lv_bankAccountDetail);

			formbean.setAttachedFilesForBankAccountDetail(attachedFilesForBankAccountDetail
					.get(0));
		}

		if (attachedFilesForTournamentList.size() > 0) {
			updateListView(attachedFilesForTournamentList, lv_tournamentList);

			formbean.setAttachedFilesForTournamentList(attachedFilesForTournamentList
					.get(0));
		}

		/*
		if (attachedFilesForParticipationBudgetForm.size() > 0) {
			updateListView(attachedFilesForParticipationBudgetForm,
					lv_participationBudgetForm);

			formbean.setAttachedFilesForParticipationBudgetForm(attachedFilesForParticipationBudgetForm
					.get(0));
		}
		*/
		if (attachedFilesForQuotationsAndSupporting.size() > 0) {
			updateListView(attachedFilesForQuotationsAndSupporting,
					lv_quotationsAndSupporting);

			formbean.setAttachedFilesForQuotationsAndSupporting(attachedFilesForQuotationsAndSupporting
					.get(0));
		}
		GoysLog.e(TAG, " participant array "
				+ attachedFilesForParticipationDate);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				formBeanList = new SportsParticipationFormBean[2];
				if (formbean == null) {
					formbean = new SportsParticipationFormBean();
				}

				formbean.setChampionship_and_gamename(et_championship_and_gamename
						.getText().toString());
				formbean.setPinCode(et_pinCode.getText().toString());
				formbean.setClubName(et_clubName.getText().toString());
				formbean.setNotes(et_notes.getText().toString());

				if (validateForm(formbean)) {

					if (CommonActions
							.hasConnection(SportsParticipationsForNationalClubsActivity.this)) {

						boolean flag = gs
								.validatePinCode(
										SportsParticipationsForNationalClubsActivity.this,
										et_pinCode);

						if (flag) {

							/*
							 * finally checking the pinCode again for if user
							 * input wrong value for pinCode after verify
							 * pincode
							 */
							if (tempPinCode.equals(et_pinCode.getText()
									.toString())) {

								formBeanList[0] = formbean;
								gs.sportParticipationFormService(
										SportsParticipationsForNationalClubsActivity.this,
										formBeanList);
							} else {

								gs.pinCodeService(
										SportsParticipationsForNationalClubsActivity.this,
										et_pinCode.getText().toString());
							}

						} else {
							et_pinCode.requestFocus();
							et_pinCode.setError(getResources().getString(
									(R.string.invalidePinCode)));
							et_clubName.setText("");
						}
					} else {
						CommonActions
								.alertDialogShow(
										SportsParticipationsForNationalClubsActivity.this,
										getResources().getString(R.string.note),
										getResources()
												.getString(
														R.string.networkConnectionRequired));

					}

				}

			}

		});
	}// End of onCreate

	public void keepLastItem(ArrayList<Attachment> list) {
		if (list.size() > 1) {
			for (int i = 0; i < list.size() - 1; i++) {
				list.remove(i);
			}
		}

	}

	public void initializeView() {

		et_pinCode = (EditText) this.findViewById(R.id.et_pinCode);
		et_clubName = (EditText) this.findViewById(R.id.et_clubName);
		et_championship_and_gamename = (EditText) this
				.findViewById(R.id.et_championship_and_gamename);
		et_notes = (EditText) this.findViewById(R.id.et_notes);

		// Buttons Initialization
		chooseFileForParticipationDate = (Button) this
				.findViewById(R.id.participation_date_location);
		chooseFileForConsentOfConcerned = (Button) this
				.findViewById(R.id.consent_of_concerned_union);
		chooseFileForParticipationCopy = (Button) this
				.findViewById(R.id.participation_copy);
		chooseFileForParticipationList = (Button) this
				.findViewById(R.id.participation_list);
		chooseFileForBankAccountDetail = (Button) this
				.findViewById(R.id.bank_account_details);
		chooseFileForTournamentList = (Button) this
				.findViewById(R.id.tournament_list);
		//chooseFileForParticipationBudgetForm = (Button) this
		//		.findViewById(R.id.participation_budget_form);
		chooseFileForQuotationsAndSupporting = (Button) this
				.findViewById(R.id.quotations_and_supporting_documents);
		submit = (Button) this.findViewById(R.id.submit);

		// ListViews Initialization
		lv_participationDate = (ListView) this
				.findViewById(R.id.attached_participation_date_location_listview);
		lv_consentOfConcerned = (ListView) this
				.findViewById(R.id.attached_consent_of_concerned_union_listview);
		lv_participationCopy = (ListView) this
				.findViewById(R.id.attached_participation_copy_listview);
		lv_participationList = (ListView) this
				.findViewById(R.id.attached_participation_list_listview);
		lv_bankAccountDetail = (ListView) this
				.findViewById(R.id.attached_bank_account_details_listview);
		lv_tournamentList = (ListView) this
				.findViewById(R.id.attached_tournament_list_listview);
		//lv_participationBudgetForm = (ListView) this
		//.findViewById(R.id.attached_participation_budget_form_listview); 
		lv_quotationsAndSupporting = (ListView) this
				.findViewById(R.id.attached_quotations_and_supporting_documents_listview);

		// TODO
		attachement_label_p_date = (TextView) findViewById(R.id.Label_p_date);
		attachement_label_c_concerned = (TextView) findViewById(R.id.Label_c_concerned);
		attachement_label_p_copy = (TextView) findViewById(R.id.Label_p_copy);
		attachement_label_p_list = (TextView) findViewById(R.id.Label_p_list);
		attachement_label_bankAccountDetial = (TextView) findViewById(R.id.Label_b_account);
		attachement_label_t_list = (TextView) findViewById(R.id.Label_t_list);
		//attachement_label_p_budgetForm = (TextView) findViewById(R.id.Label_p_budget);
		attachement_label_quotationsSupport = (TextView) findViewById(R.id.Label_q_suporting);
		
		//tvParticipationBudgetForm = (TextView) findViewById(R.id.p_budget_form); 
		/*
		tvParticipationBudgetForm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
						.parse(getResources().getString(
								R.string.link_participation_budget_form))));
			}
		});
		*/
		
		btn_clear = (Button) this.findViewById(R.id.clear);

		btn_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				clearForm();
			}
		});

		formbean = new SportsParticipationFormBean();

		et_pinCode.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				GoysLog.d(TAG, "onFocusChange " + hasFocus);

				if (!hasFocus) {
					if (gs.validatePinCode(
							SportsParticipationsForNationalClubsActivity.this,
							et_pinCode)) {

						if (CommonActions
								.hasConnection(SportsParticipationsForNationalClubsActivity.this)) {
							gs.pinCodeService(
									SportsParticipationsForNationalClubsActivity.this,
									et_pinCode.getText().toString());
						} else {
							CommonActions
									.alertDialogShow(
											SportsParticipationsForNationalClubsActivity.this,
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
	}

	public void chooseFileOnClick(View v) {
		viewFileDialogWithFewOption();
		buttonId = v.getId();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_CANCELED) {

			if (formbean == null) {
				formbean = new SportsParticipationFormBean();
			}

			if (requestCode == AppConstants.PICK_IMAGE) {

				selectedImagePath = FileUtils.getPath(this, data.getData());

				if (selectedImagePath != null) {

					String fileName = selectedImagePath.substring(
							selectedImagePath.lastIndexOf('/') + 1,
							selectedImagePath.length());
					// Check supported File extensions
					if (!CommonActions.isFileExtensionAllowed(
							SportsParticipationsForNationalClubsActivity.this,
							FileUtils.getExtension(selectedImagePath))) {

						CommonActions
								.showUnsupportedFileError(SportsParticipationsForNationalClubsActivity.this);
					} else {
						GoysLog.d(
								TAG,
								"File Extension "
										+ FileUtils
												.getExtension(selectedImagePath));

						if (buttonId == R.id.participation_date_location) {

							lv_participationDate.requestFocus();

							attachedFilesForParticipationDate
									.add(new Attachment(fileName,
											selectedImagePath));

							formbean.setAttachedFilesForParticipationDate(attachedFilesForParticipationDate
									.get(0));

							attachement_label_p_date.setVisibility(View.GONE);

							updateListView(attachedFilesForParticipationDate,
									lv_participationDate);

						} else if (buttonId == R.id.consent_of_concerned_union) {

							lv_consentOfConcerned.requestFocus();

							attachedFilesForConsentOfConcerned
									.add(new Attachment(fileName,
											selectedImagePath));

							formbean.setAttachedFilesForConsentOfConcerned(attachedFilesForConsentOfConcerned
									.get(0));

							attachement_label_c_concerned
									.setVisibility(View.GONE);
							updateListView(attachedFilesForConsentOfConcerned,
									lv_consentOfConcerned);

						} else if (buttonId == R.id.participation_copy) {

							lv_participationCopy.requestFocus();

							attachedFilesForParticipationCopy
									.add(new Attachment(fileName,
											selectedImagePath));

							formbean.setAttachedFilesForParticipationCopy(attachedFilesForParticipationCopy
									.get(0));

							attachement_label_p_copy.setVisibility(View.GONE);
							updateListView(attachedFilesForParticipationCopy,
									lv_participationCopy);

						} else if (buttonId == R.id.participation_list) {

							lv_participationList.requestFocus();

							attachedFilesForParticipationList
									.add(new Attachment(fileName,
											selectedImagePath));
							formbean.setAttachedFilesForParticipationList(attachedFilesForParticipationList
									.get(0));

							attachement_label_p_list.setVisibility(View.GONE);
							updateListView(attachedFilesForParticipationList,
									lv_participationList);

						} else if (buttonId == R.id.bank_account_details) {

							lv_bankAccountDetail.requestFocus();

							attachedFilesForBankAccountDetail
									.add(new Attachment(fileName,
											selectedImagePath));

							formbean.setAttachedFilesForBankAccountDetail(attachedFilesForBankAccountDetail
									.get(0));

							attachement_label_bankAccountDetial
									.setVisibility(View.GONE);
							updateListView(attachedFilesForBankAccountDetail,
									lv_bankAccountDetail);

						} else if (buttonId == R.id.tournament_list) {

							lv_tournamentList.requestFocus();

							attachedFilesForTournamentList.add(new Attachment(
									fileName, selectedImagePath));

							formbean.setAttachedFilesForTournamentList(attachedFilesForTournamentList
									.get(0));

							attachement_label_t_list.setVisibility(View.GONE);
							updateListView(attachedFilesForTournamentList,
									lv_tournamentList);

						} /* else if (buttonId == R.id.participation_budget_form) {

							lv_participationBudgetForm.requestFocus();

							attachedFilesForParticipationBudgetForm
									.add(new Attachment(fileName,
											selectedImagePath));

							formbean.setAttachedFilesForParticipationBudgetForm(attachedFilesForParticipationBudgetForm
									.get(0));

							attachement_label_p_budgetForm
									.setVisibility(View.GONE);
							updateListView(
									attachedFilesForParticipationBudgetForm,
									lv_participationBudgetForm);

						}*/ else if (buttonId == R.id.quotations_and_supporting_documents) {

							lv_quotationsAndSupporting.requestFocus();

							attachedFilesForQuotationsAndSupporting
									.add(new Attachment(fileName,
											selectedImagePath));

							formbean.setAttachedFilesForQuotationsAndSupporting(attachedFilesForQuotationsAndSupporting
									.get(0));

							 attachement_label_quotationsSupport
									.setVisibility(View.GONE); 
							updateListView(
									attachedFilesForQuotationsAndSupporting,
									lv_quotationsAndSupporting);

						} 
					}
				}

			} else if (requestCode == AppConstants.CAPTURE_IMAGE) {

				if (resultCode == Activity.RESULT_OK) {
					try {
						selectedImagePath = imageUri.getPath().toString();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				String fileName = "";

				fileName = selectedImagePath.substring(
						selectedImagePath.lastIndexOf('/') + 1,
						selectedImagePath.length());

				lv_participationDate.setVisibility(View.VISIBLE);

				if (buttonId == R.id.participation_date_location) {

					attachedFilesForParticipationDate.add(new Attachment(
							fileName, selectedImagePath));

					formbean.setAttachedFilesForParticipationDate(attachedFilesForParticipationDate
							.get(0));

					attachement_label_p_date.setVisibility(View.GONE);
					updateListView(attachedFilesForParticipationDate,
							lv_participationDate);
					lv_participationDate.requestFocus();

				} else if (buttonId == R.id.consent_of_concerned_union) {

					attachedFilesForConsentOfConcerned.add(new Attachment(
							fileName, selectedImagePath));

					formbean.setAttachedFilesForConsentOfConcerned(attachedFilesForConsentOfConcerned
							.get(0));

					attachement_label_c_concerned.setVisibility(View.GONE);
					updateListView(attachedFilesForConsentOfConcerned,
							lv_consentOfConcerned);
					lv_consentOfConcerned.requestFocus();

				} else if (buttonId == R.id.participation_copy) {

					lv_participationCopy.requestFocus();

					attachedFilesForParticipationCopy.add(new Attachment(
							fileName, selectedImagePath));

					formbean.setAttachedFilesForParticipationCopy(attachedFilesForParticipationCopy
							.get(0));

					attachement_label_p_copy.setVisibility(View.GONE);
					updateListView(attachedFilesForParticipationCopy,
							lv_participationCopy);

				} else if (buttonId == R.id.participation_list) {
					lv_participationList.requestFocus();

					attachedFilesForParticipationList.add(new Attachment(
							fileName, selectedImagePath));

					formbean.setAttachedFilesForParticipationList(attachedFilesForParticipationList
							.get(0));

					attachement_label_p_list.setVisibility(View.GONE);
					updateListView(attachedFilesForParticipationList,
							lv_participationList);
				} else if (buttonId == R.id.bank_account_details) {

					lv_bankAccountDetail.requestFocus();

					attachedFilesForBankAccountDetail.add(new Attachment(
							fileName, selectedImagePath));

					formbean.setAttachedFilesForBankAccountDetail(attachedFilesForBankAccountDetail
							.get(0));

					attachement_label_bankAccountDetial
							.setVisibility(View.GONE);
					updateListView(attachedFilesForBankAccountDetail,
							lv_bankAccountDetail);

				} else if (buttonId == R.id.tournament_list) {

					lv_tournamentList.requestFocus();

					attachedFilesForTournamentList.add(new Attachment(fileName,
							selectedImagePath));

					formbean.setAttachedFilesForTournamentList(attachedFilesForTournamentList
							.get(0));

					attachement_label_t_list.setVisibility(View.GONE);
					updateListView(attachedFilesForTournamentList,
							lv_tournamentList);

				}/* else if (buttonId == R.id.participation_budget_form) {

					lv_participationBudgetForm.requestFocus();

					attachedFilesForParticipationBudgetForm.add(new Attachment(
							fileName, selectedImagePath));

					formbean.setAttachedFilesForParticipationBudgetForm(attachedFilesForParticipationBudgetForm
							.get(0));

					attachement_label_p_budgetForm.setVisibility(View.GONE);
					updateListView(attachedFilesForParticipationBudgetForm,
							lv_participationBudgetForm);

				}*/ else if (buttonId == R.id.quotations_and_supporting_documents) {

					lv_quotationsAndSupporting.requestFocus();

					attachedFilesForQuotationsAndSupporting.add(new Attachment(
							fileName, selectedImagePath));

					formbean.setAttachedFilesForQuotationsAndSupporting(attachedFilesForQuotationsAndSupporting
							.get(0));
					
					attachement_label_quotationsSupport
							.setVisibility(View.GONE);
				
					updateListView(attachedFilesForQuotationsAndSupporting,
							lv_quotationsAndSupporting);
				} 

			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}

		}
	}

	private boolean validateForm(SportsParticipationFormBean bean) {

		boolean validate = true;

		if (bean.getAttachedFilesForParticipationDate() == null) {
			attachement_label_p_date.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (bean.getAttachedFilesForConsentOfConcerned() == null) {
			attachement_label_c_concerned.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (bean.getAttachedFilesForParticipationCopy() == null) {
			attachement_label_p_copy.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (bean.getAttachedFilesForParticipationList() == null) {
			buttonId = R.id.participation_list;

			attachement_label_p_list.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (bean.getAttachedFilesForBankAccountDetail() == null) {

			attachement_label_bankAccountDetial.setVisibility(View.VISIBLE);
			validate = false;
		}

		if (bean.getAttachedFilesForTournamentList() == null) {

			attachement_label_t_list.setVisibility(View.VISIBLE);
			validate = false;
		}

		/*if (bean.getAttachedFilesForParticipationBudgetForm() == null) {
			attachement_label_p_budgetForm.setVisibility(View.VISIBLE);
			validate = false;
		}*/
		if (bean.getAttachedFilesForQuotationsAndSupporting() == null) {
			 attachement_label_quotationsSupport.setVisibility(View.VISIBLE); 
			validate = false;
		}

		if (CommonObjects.isEmpty(bean.getChampionship_and_gamename())) {
			et_championship_and_gamename.requestFocus();
			et_championship_and_gamename.setError(getResources().getString(
					(R.string.emptyField)));
			validate = false;
		} else {
			et_championship_and_gamename.setError(null);
		}

		if (CommonObjects.isEmpty(bean.getClubName())) {
			et_clubName.requestFocus();
			et_clubName.setError(getResources()
					.getString((R.string.emptyField)));
			validate = false;
		}

		if (CommonObjects.isEmpty(bean.getPinCode())) {
			et_pinCode.requestFocus();
			et_pinCode
					.setError(getResources().getString((R.string.emptyField)));
			validate = false;
		}

		else if (Pattern.compile(AppConstants.SYMBOLS_PATTERN)
				.matcher(bean.getPinCode()).find()) {
			validate = false;
			et_pinCode.requestFocus();
			et_pinCode.setError(getResources().getString(
					R.string.invalidePinCodeLength));
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

	public void updateListView(ArrayList<Attachment> attachedFilesList,
			ListView listView) {
		if (attachedFilesList.size() > 0) { 
			keepLastItem(attachedFilesList);
			listView.setVisibility(View.VISIBLE);

			adapter = new AttachementListArrayAdapter(listView, this,
					attachedFilesList);

			listView.setAdapter(adapter);
			int heightofListView = listView.getHeight();
			GoysLog.d(TAG, "attached_files_listview height " + heightofListView);

			CommonActions.setListViewHeightBasedOnChildren(listView);

			heightofListView = listView.getHeight();
			GoysLog.d(TAG, "attached_files_listview height after update "
					+ heightofListView);
			listView.setFocusable(true);

		}
	}

	private void clearValuesForEditText(EditText editText) {

		editText.setText("");
		editText.setError(null);
	}

	private void clearAttachmentValues(ArrayList<Attachment> list, ListView v) {
		list.clear();
		v.setVisibility(View.GONE);
	}

	public void clearForm() {

		clearValuesForEditText(et_pinCode);
		clearValuesForEditText(et_clubName);
		clearValuesForEditText(et_championship_and_gamename);
		clearValuesForEditText(et_notes);

		clearAttachmentValues(attachedFilesForBankAccountDetail,
				lv_bankAccountDetail);
		clearAttachmentValues(attachedFilesForConsentOfConcerned,
				lv_consentOfConcerned);

		/* clearAttachmentValues(attachedFilesForParticipationBudgetForm,
				lv_participationBudgetForm);
		*/
		clearAttachmentValues(attachedFilesForParticipationCopy,
				lv_participationCopy);

		clearAttachmentValues(attachedFilesForParticipationDate,
				lv_participationDate);

		clearAttachmentValues(attachedFilesForParticipationList,
				lv_participationList);

		clearAttachmentValues(attachedFilesForQuotationsAndSupporting,
				lv_quotationsAndSupporting);

		clearAttachmentValues(attachedFilesForTournamentList, lv_tournamentList);

		attachement_label_p_date.setVisibility(View.GONE);
		attachement_label_p_list.setVisibility(View.GONE);
		//attachement_label_p_budgetForm.setVisibility(View.GONE);
		attachement_label_p_copy.setVisibility(View.GONE);
		attachement_label_c_concerned.setVisibility(View.GONE);
		attachement_label_bankAccountDetial.setVisibility(View.GONE);
		attachement_label_quotationsSupport.setVisibility(View.GONE);
		attachement_label_t_list.setVisibility(View.GONE);

		formbean = new SportsParticipationFormBean();

		et_pinCode.requestFocus();

	}

	@Override
	public void onResponse(int serviceId, String responseObj) {

		// if (isActivityAvailable) {
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
						et_clubName.setText(clubName);

						/* storing a valid pinCode after verified */
						tempPinCode = et_pinCode.getText().toString();

						et_pinCode.setError(null);
						et_clubName.setError(null);
					} else {
						et_pinCode.requestFocus();
						et_pinCode.setError(getResources().getString(
								(R.string.invalidePinCode)));
					}

				} catch (JSONException e) {
					GoysLog.e(TAG, "Error: " + e.toString());
					CommonActions.showErrorAlertDialog(
							SportsParticipationsForNationalClubsActivity.this,
							0,
							getResources().getString(
									R.string.applicationundermaintenance));

				}

			} else {
				CommonActions.alertDialogShow(
						SportsParticipationsForNationalClubsActivity.this,
						getResources().getString(R.string.alert_error_title),
						getResources().getString(
								R.string.applicationundermaintenance));

			}
		} else if (serviceId == AppConstants.SPORTPARTICIPATION_SERVICE_ID) {

			if (responseObj != null && !responseObj.equals("")
					&& !responseObj.contains("Error")) {

				/* play success submission sound */
				if (GOYSApplication.getInstance().isMusicOn()) {
					MediaPlayer mPlayer2 = MediaPlayer.create(
							getApplicationContext(), R.raw.popup_sound);
					mPlayer2.start();
				}

				CommonActions.showSuccessfulSubmissionAlert(
						this,
						getResources().getString(
								R.string.menu_sports_participation),
						getResources().getString(
								R.string.successful_submission_msg)
								+ " " + responseObj);

			} else {
				CommonActions.showErrorAlertDialog(this, 0, getResources()
						.getString(R.string.applicationundermaintenance));

			}
		}

	}

	@Override
	public void onError(int responseCode, String msg) {

		if (isActivityAvailable) {
			CommonActions.alertDialogShow(
					SportsParticipationsForNationalClubsActivity.this,
					getResources().getString(R.string.alert_error_title), msg);
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

		outState.putParcelableArrayList("participation_date",
				attachedFilesForParticipationDate);

		outState.putParcelableArrayList("consent_of_concerned",
				attachedFilesForConsentOfConcerned);

		outState.putParcelableArrayList("participation_copy",
				attachedFilesForParticipationCopy);

		outState.putParcelableArrayList("participation_list",
				attachedFilesForParticipationList);

		outState.putParcelableArrayList("bank_account_detail",
				attachedFilesForBankAccountDetail);

		outState.putParcelableArrayList("tournament_list",
				attachedFilesForTournamentList);
		
		/*
		outState.putParcelableArrayList("budget_form",
				attachedFilesForParticipationBudgetForm);
		*/
		
		outState.putParcelableArrayList("quotation_supporting",
				attachedFilesForQuotationsAndSupporting);

	}

	/**
	 * This method updates the models for the forms if incase an item is deleted
	 * in adapter.
	 * 
	 * @param lv
	 *            ListView reference to identify the field
	 */
	public void notifyModels(ListView lv) {
		switch (lv.getId()) {
		case R.id.attached_participation_date_location_listview:
			attachedFilesForParticipationDate.clear();
			formbean.setAttachedFilesForParticipationDate(null);
			break;

		case R.id.attached_consent_of_concerned_union_listview:
			attachedFilesForConsentOfConcerned.clear();
			formbean.setAttachedFilesForConsentOfConcerned(null);
			break;

		case R.id.attached_participation_copy_listview:
			attachedFilesForParticipationCopy.clear();
			formbean.setAttachedFilesForConsentOfConcerned(null);
			break;

		case R.id.attached_participation_list_listview:
			attachedFilesForParticipationList.clear();
			formbean.setAttachedFilesForParticipationList(null);
			break;

		case R.id.attached_bank_account_details_listview:
			attachedFilesForBankAccountDetail.clear();
			formbean.setAttachedFilesForBankAccountDetail(null);
			break;

		case R.id.attached_tournament_list_listview:
			attachedFilesForTournamentList.clear();
			formbean.setAttachedFilesForTournamentList(null);
			break;

		case R.id.attached_quotations_and_supporting_documents_listview:
			attachedFilesForQuotationsAndSupporting.clear();
			formbean.setAttachedFilesForQuotationsAndSupporting(null);
			break;
	
		default:
			break;
		}
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
							.getOutputImageFileUri(SportsParticipationsForNationalClubsActivity.this);
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
}
