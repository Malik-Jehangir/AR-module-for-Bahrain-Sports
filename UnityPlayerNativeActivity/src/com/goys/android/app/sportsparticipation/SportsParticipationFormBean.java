package com.goys.android.app.sportsparticipation;

import com.goys.android.app.emigration.Attachment;

public class SportsParticipationFormBean {

	private Attachment attachedFilesForParticipationDate;

	private Attachment attachedFilesForConsentOfConcerned;

	private Attachment attachedFilesForParticipationCopy;

	private Attachment attachedFilesForParticipationList;

	private Attachment attachedFilesForBankAccountDetail;

	private Attachment attachedFilesForTournamentList;

	//private Attachment attachedFilesForParticipationBudgetForm;

	private Attachment attachedFilesForQuotationsAndSupporting;

	private String pinCode;

	private String clubName;

	private String comments;

	private String notes;

	public String getChampionship_and_gamename() {
		return championship_and_gamename;
	}

	public void setChampionship_and_gamename(String championship_and_gamename) {
		this.championship_and_gamename = championship_and_gamename;
	}

	private String championship_and_gamename;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Attachment getAttachedFilesForParticipationDate() {
		return attachedFilesForParticipationDate;
	}

	public void setAttachedFilesForParticipationDate(
			Attachment attachedFilesForParticipationDate) {
		this.attachedFilesForParticipationDate = attachedFilesForParticipationDate;
	}

	public Attachment getAttachedFilesForConsentOfConcerned() {
		return attachedFilesForConsentOfConcerned;
	}

	public void setAttachedFilesForConsentOfConcerned(
			Attachment attachedFilesForConsentOfConcerned) {
		this.attachedFilesForConsentOfConcerned = attachedFilesForConsentOfConcerned;
	}

	public Attachment getAttachedFilesForParticipationCopy() {
		return attachedFilesForParticipationCopy;
	}

	public void setAttachedFilesForParticipationCopy(
			Attachment attachedFilesForParticipationCopy) {
		this.attachedFilesForParticipationCopy = attachedFilesForParticipationCopy;
	}

	public Attachment getAttachedFilesForParticipationList() {
		return attachedFilesForParticipationList;
	}

	public void setAttachedFilesForParticipationList(
			Attachment attachedFilesForParticipationList) {
		this.attachedFilesForParticipationList = attachedFilesForParticipationList;
	}

	public Attachment getAttachedFilesForBankAccountDetail() {
		return attachedFilesForBankAccountDetail;
	}

	public void setAttachedFilesForBankAccountDetail(
			Attachment attachedFilesForBankAccountDetail) {
		this.attachedFilesForBankAccountDetail = attachedFilesForBankAccountDetail;
	}

	public Attachment getAttachedFilesForTournamentList() {
		return attachedFilesForTournamentList;
	}

	public void setAttachedFilesForTournamentList(
			Attachment attachedFilesForTournamentList) {
		this.attachedFilesForTournamentList = attachedFilesForTournamentList;
	}

/*	public Attachment getAttachedFilesForParticipationBudgetForm() {
		return attachedFilesForParticipationBudgetForm;
	}

	public void setAttachedFilesForParticipationBudgetForm(
			Attachment attachedFilesForParticipationBudgetForm) {
		this.attachedFilesForParticipationBudgetForm = attachedFilesForParticipationBudgetForm;
	}
*/
	public Attachment getAttachedFilesForQuotationsAndSupporting() {
		return attachedFilesForQuotationsAndSupporting;
	}

	public void setAttachedFilesForQuotationsAndSupporting(
			Attachment attachedFilesForQuotationsAndSupporting) {
		this.attachedFilesForQuotationsAndSupporting = attachedFilesForQuotationsAndSupporting;
	}

}
