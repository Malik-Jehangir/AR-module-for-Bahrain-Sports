package com.goys.android.app.db.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Facilities implements Parcelable {

	private String facilityNameEn;
	private String facilityNameAr;
	private String email;
	
	public Facilities(String facilityNameEn, String facilityNameAr, String email) {
		super();
		this.facilityNameEn = facilityNameEn;
		this.facilityNameAr = facilityNameAr;
		this.email = email;
	}

	public String getFacilityNameEn() {
		return facilityNameEn;
	}

	public void setFacilityNameEn(String facilityNameEn) {
		this.facilityNameEn = facilityNameEn;
	}

	public String getFacilityNameAr() {
		return facilityNameAr;
	}

	public void setFacilityNameAr(String facilityNameAr) {
		this.facilityNameAr = facilityNameAr;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	

    protected Facilities(Parcel in) {
        facilityNameEn = in.readString();
        facilityNameAr = in.readString();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(facilityNameEn);
        dest.writeString(facilityNameAr);
        dest.writeString(email);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Facilities> CREATOR = new Parcelable.Creator<Facilities>() {
        @Override
        public Facilities createFromParcel(Parcel in) {
            return new Facilities(in);
        }

        @Override
        public Facilities[] newArray(int size) {
            return new Facilities[size];
        }
    };
}

/*public class Facilities {

	private String facilityNameEn;
	private String facilityNameAr;
	private String email;
	
	public Facilities(String facilityNameEn, String facilityNameAr, String email) {
		super();
		this.facilityNameEn = facilityNameEn;
		this.facilityNameAr = facilityNameAr;
		this.email = email;
	}

	public String getFacilityNameEn() {
		return facilityNameEn;
	}

	public void setFacilityNameEn(String facilityNameEn) {
		this.facilityNameEn = facilityNameEn;
	}

	public String getFacilityNameAr() {
		return facilityNameAr;
	}

	public void setFacilityNameAr(String facilityNameAr) {
		this.facilityNameAr = facilityNameAr;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	
}
*/

