package com.goys.android.app.eventcalendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.goys.android.app.util.GoysLog;

public class EventCalendar {

	private static final String TAG = "EventCalendar Model";
	private String arabicTitle;
	private String englishTitle;
	private String startDate;
	private String endDate;
	private String location;
	private String imageUrl;
	private int Id;
	private String isPublished;
	private String month;
	private String arabicshortdescription;
	private String englishshortdescription;
	
	public EventCalendar() {
	}

	public EventCalendar(JSONObject obj) {

		try {
			
			if (!obj.isNull("ArabicTitle")) {
				this.arabicTitle = obj.getString("ArabicTitle");
			} else {
				this.arabicTitle = "";
			}

			if (!obj.isNull("EnglishTitle")) {
				this.englishTitle = obj.getString("EnglishTitle");
			} else {
				this.englishTitle = "";
			}

			if (!obj.isNull("Id")) {
				GoysLog.d(TAG, obj.getString("Id"));
				this.Id = Integer.parseInt(obj.getString("Id"));
			} else {
				this.Id = 0;
			}

			if (!obj.isNull("IsPublished")) {
				this.isPublished = obj.getString("IsPublished");
			} else {
				this.isPublished = "";
			}


			if (!obj.isNull("ImageUrl")) {
				this.imageUrl = obj.getString("ImageUrl");
			} else {
				this.imageUrl = "";
			}

			if (!obj.isNull("StartDate")) {
				this.startDate = obj.getString("StartDate");
			} else {
				this.startDate = "";
			}
			
			if (!obj.isNull("EndDate")) {
				this.endDate = obj.getString("EndDate");
			} else {
				this.endDate = "";
			}
			
			if (!obj.isNull("Location")) {
				this.location = obj.getString("Location");
			} else {
				this.location = "";
			}
			
			if (!obj.isNull("Month")) {
				this.month = obj.getString("Month");
			} else {
				this.month = "";
			}
			if (!obj.isNull("ArabicShortDescription")) {
				this.arabicshortdescription = obj.getString("ArabicShortDescription");
			} else {
				this.arabicshortdescription = "";
			}
			if (!obj.isNull("EnglishShortDescription")) {
				this.englishshortdescription = obj.getString("EnglishShortDescription");
			} else {
				this.englishshortdescription = "";
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


	/**
	 * @return the arabicTitle
	 */
	public String getArabicTitle() {
		return arabicTitle;
	}

	/**
	 * @param arabicTitle
	 *            the arabicTitle to set
	 */
	public void setArabicTitle(String arabicTitle) {
		this.arabicTitle = arabicTitle;
	}

	/**
	 * @return the englishTitle
	 */
	public String getEnglishTitle() {
		return englishTitle;
	}

	/**
	 * @param englishTitle
	 *            the englishTitle to set
	 */
	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		Id = id;
	}
	
	/**
	 * @return the isPublished
	 */
	public String getIsPublished() {
		return isPublished;
	}

	/**
	 * @param isPublished
	 *            the isPublished to set
	 */
	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}
	
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the Location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	
	/**
	 * @return the Arabic Short Description
	 */
	public String getArabicShortDescription() {
		return arabicshortdescription;
	}

	/**
	 * @param arabicshortdescription
	 *            the Arabic Short Description
	 */
	public void setArabicShortDescription(String ArabicShortDescription) {
		this.arabicshortdescription = ArabicShortDescription;
	}
	
	/**
	 * @return the English Short Description
	 */
	public String getEnglishShortDescription() {
		return englishshortdescription;
	}

	/**
	 * @param arabicshortdescription
	 *            the English Short Description
	 */
	public void setEnglishShortDescription(String EnglishShortDescription) {
		this.englishshortdescription = EnglishShortDescription;
	}
	
	
}
