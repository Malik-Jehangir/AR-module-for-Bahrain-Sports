package com.goys.android.app.emigration;

import android.os.Parcel;
import android.os.Parcelable;

public class Attachment implements Parcelable {

	String fileName;
	String filePath;

	public Attachment(String filename, String filepath) {
		this.fileName = filename;
		this.filePath = filepath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	protected Attachment(Parcel in) {
		fileName = in.readString();
		filePath = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fileName);
		dest.writeString(filePath);
	}

	public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
		@Override
		public Attachment createFromParcel(Parcel in) {
			return new Attachment(in);
		}

		@Override
		public Attachment[] newArray(int size) {
			return new Attachment[size];
		}
	};
}