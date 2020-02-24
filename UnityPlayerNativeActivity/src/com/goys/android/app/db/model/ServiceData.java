package com.goys.android.app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceData implements Parcelable {

	private String ID;
	private String Value;

	public ServiceData() {
		// TODO Auto-generated constructor stub
	}

	public ServiceData(String id, String val) {
		this.ID = id;
		this.Value = val;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return Value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		Value = value;
	}

	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}


    protected ServiceData(Parcel in) {
        ID = in.readString();
        Value = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Value);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ServiceData> CREATOR = new Parcelable.Creator<ServiceData>() {
        @Override
        public ServiceData createFromParcel(Parcel in) {
            return new ServiceData(in);
        }

        @Override
        public ServiceData[] newArray(int size) {
            return new ServiceData[size];
        }
    };
}