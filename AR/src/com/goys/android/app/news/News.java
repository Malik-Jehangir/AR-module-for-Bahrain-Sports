package com.goys.android.app.news;

import org.json.JSONException;
import org.json.JSONObject;

import com.goys.android.app.util.GoysLog;

public class News {

	private static final String TAG = "News Model";
	private String arabicBody;
	private String arabicShortDescription;
	private String arabicTitle;
	private String englishTitle;
	private String englishBody;
	private String englishShortDescription;
	private String imageUrl;
	private int Id;
	private String isPublished;
	private String order;
	private String startDate;

	public News() {
	}

	public News(JSONObject obj) {

		try {
			if (!obj.isNull("ArabicBody")) {
				this.arabicBody = obj.getString("ArabicBody");
			} else {
				this.arabicBody = "";
			}

			if (!obj.isNull("ArabicShortDescription")) {
				this.arabicShortDescription = obj
						.getString("ArabicShortDescription");
			} else {
				this.arabicShortDescription = "";
			}

			if (!obj.isNull("ArabicTitle")) {
				this.arabicTitle = obj.getString("ArabicTitle");
			} else {
				this.arabicTitle = "";
			}

			if (!obj.isNull("EnglishBody")) {
				this.englishBody = obj.getString("EnglishBody");
			} else {
				this.englishBody = "";
			}

			if (!obj.isNull("EnglishShortDescription")) {
				this.englishShortDescription = obj
						.getString("EnglishShortDescription");
			} else {
				this.englishShortDescription = "";
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

			if (!obj.isNull("Order")) {
				this.order = obj.getString("Order");
			} else {
				this.order = "";
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

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the arabicBody
	 */
	public String getArabicBody() {
		return arabicBody;
	}

	/**
	 * @param arabicBody
	 *            the arabicBody to set
	 */
	public void setArabicBody(String arabicBody) {
		this.arabicBody = arabicBody;
	}

	/**
	 * @return the arabicShortDescription
	 */
	public String getArabicShortDescription() {
		return arabicShortDescription;
	}

	/**
	 * @param arabicShortDescription
	 *            the arabicShortDescription to set
	 */
	public void setArabicShortDescription(String arabicShortDescription) {
		this.arabicShortDescription = arabicShortDescription;
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
	 * @return the englishBody
	 */
	public String getEnglishBody() {
		return englishBody;
	}

	/**
	 * @param englishBody
	 *            the englishBody to set
	 */
	public void setEnglishBody(String englishBody) {
		this.englishBody = englishBody;
	}

	/**
	 * @return the englishShortDescription
	 */
	public String getEnglishShortDescription() {
		return englishShortDescription;
	}

	/**
	 * @param englishShortDescription
	 *            the englishShortDescription to set
	 */
	public void setEnglishShortDescription(String englishShortDescription) {
		this.englishShortDescription = englishShortDescription;
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
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
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

}
