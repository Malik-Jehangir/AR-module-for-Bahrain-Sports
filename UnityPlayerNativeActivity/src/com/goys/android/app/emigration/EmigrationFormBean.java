package com.goys.android.app.emigration;

import java.util.List;

public class EmigrationFormBean {

	private String email;
	private String pinCode;
	private String clubName;
	private String firstName;
	private String middleName;
	private String familyName;
	private int country;
	private String job;
	private String passporNumber;
	private String personalNumber;
	private String selectedRequestType;
	private List<Attachment> attachment;
	private int selectedId;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the pinCode
	 */
	public String getPinCode() {
		return pinCode;
	}

	/**
	 * @param pinCode
	 *            the pinCode to set
	 */
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	/**
	 * @return the clubName
	 */
	public String getClubName() {
		return clubName;
	}

	/**
	 * @param clubName
	 *            the clubName to set
	 */
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName
	 *            the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	/**
	 * @return the country
	 */
	public int getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(int country) {
		this.country = country;
	}

	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}

	/**
	 * @return the passporNumber
	 */
	public String getPassporNumber() {
		return passporNumber;
	}

	/**
	 * @param passporNumber
	 *            the passporNumber to set
	 */
	public void setPassporNumber(String passporNumber) {
		this.passporNumber = passporNumber;
	}

	/**
	 * @return the personalNumber
	 */
	public String getPersonalNumber() {
		return personalNumber;
	}

	/**
	 * @param personalNumber
	 *            the personalNumber to set
	 */
	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	/**
	 * @return the selectedRequestType
	 */
	public String getSelectedRequestType() {
		return selectedRequestType;
	}

	/**
	 * @param selectedRequestType
	 *            the selectedRequestType to set
	 */
	public void setSelectedRequestType(String selectedRequestType) {
		this.selectedRequestType = selectedRequestType;
	}

	/**
	 * @return the selectedId
	 */
	public int getSelectedId() {
		return selectedId;
	}

	/**
	 * @param selectedId
	 *            the selectedId to set
	 */
	public void setSelectedId(int selectedId) {
		this.selectedId = selectedId;
	}

	/**
	 * @return the attachment
	 */
	public List<Attachment> getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment
	 *            the attachment to set
	 */
	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}
}
