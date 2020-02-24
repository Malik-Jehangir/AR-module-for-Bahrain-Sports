package com.goys.android.app.db.model;

import java.util.List;

import com.goys.android.app.emigration.Attachment;

public class MaintenancePublic {

	private String serviceType;
	private String jobDescription;
	private String facilityName;
	private String placeRequiredMaintenance;
	private String buildingNo;
	private String roadNo;
	private String blockNo;
	private String area;
	private String governorate;
	private String locationType;
	private List<Attachment> attachment;
	private String email;
	private String mobileNo;
	private String date;
	private String maintenanceType;

	public MaintenancePublic() {

	}

	
	public String getMaintenanceType() {
		return maintenanceType;
	}


	public void setMaintenanceType(String maintenanceType) {
		this.maintenanceType = maintenanceType;
	}


	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getPlaceRequiredMaintenance() {
		return placeRequiredMaintenance;
	}

	public void setPlaceRequiredMaintenance(String placeRequiredMaintenance) {
		this.placeRequiredMaintenance = placeRequiredMaintenance;
	}

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public String getRoadNo() {
		return roadNo;
	}

	public void setRoadNo(String roadNo) {
		this.roadNo = roadNo;
	}

	public String getBlockNo() {
		return blockNo;
	}

	public void setBlockNo(String blockNo) {
		this.blockNo = blockNo;
	}

	public String getGovernorate() {
		return governorate;
	}

	public void setGovernorate(String governorate) {
		this.governorate = governorate;
	}

	public List<Attachment> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "servicestype: " + this.getServiceType() + "jobDescription: "
				+ this.getJobDescription() + "facilityName: "
				+ this.getFacilityName() + " Building No : "
				+ this.getBuildingNo() + "roadNo " + this.getRoadNo();

	}
}
