package com.goys.android.app.util;


public class AppConstants {

	/* DATABASE TABLE AND COLUMNS */
	public static final String COLUMN_ID = "_id";

	public static final String COLUMN_COUNTRY = "country";

	public static final String TABLE_COUNTRIES = "country";

	public static final int PICK_IMAGE = 1;

	public static final int CAPTURE_IMAGE = 2;

	public static final String APP_NAME = "goys";

	public static final int RANDOM_GENERATE_NUM_RANGE = 10000;
	
	public static final String ALPHA_NUM_PATTERN = "^[a-zA-Z0-9]*$";

	public static final String ALPHA_PATTERN = "[^\\p{L}]";
	
	public static final String NUMERIC_PATTERN = "[^\\p{Nd}]";

	public static final String ALPHA_NUMERIC_SPACE= "[^\\p{L}\\p{Nd}\\s]+";
	
	public static final String SYMBOLS_PATTERN = "[^\\p{L}\\p{Nd}]+";
	
	public static final String SPECIAL_CHARACTER_PATTERN = "[$&+,:()!%^*+;=?@#|]";

	/* BASE RESTFUL SERVICES URL OF GOYS */
	 public static final String SERVICE_BASE_URL_TEST =
	 "http://89.31.194.167:2255/Service.svc";
	 
	 /*public static final String SERVICE_BASE_URL =
	 "http://spt-srv.khi.systemsltd.com:2255/Service.svc";
	*/
	 
	 /*public static final String SERVICE_BASE_URL =
			 "http://sefsp:2255/service.svc";
	*/
	/* public static final String SERVICE_BASE_URL =
			 "http://goys-services.systemsltd.com/Service.svc";
	*/
	 
/*	public static final String SERVICE_BASE_URL =
			"http://spt-srv:2255/Service.svc";
*/
	//public static final String SERVICE_BASE_URL = "http://89.31.194.167:2255/Service.svc";
	 
	public static final String SERVICE_BASE_URL = "http://89.31.194.167:2255/Service.svc";
	public static final String EVENT_CALENDAR_BASE_URL = "http://89.31.194.167:7070/Service.svc";

	public static final String NEWS_SERVICE = SERVICE_BASE_URL + "/GetNews";
	public static final String EVENT_CALENDAR_SERVICE = EVENT_CALENDAR_BASE_URL + "/GetEvents";

	public static final String VALIDATE_PINCODE_SERVICE_URL = SERVICE_BASE_URL
			+ "/ValidatePinCode";

	public static final String EMIGRATION_VISA_SERVICE_URL = SERVICE_BASE_URL
			+ "/PostVisa";
	public static final String SPORTS_PARTICIPATION_SERVICE_URL = SERVICE_BASE_URL
			+ "/PostSportsParticipation";
	
	public static final String MAINTENANCE_SERVICE_URL = SERVICE_BASE_URL + "/PostEmergencyMaintenance";
	public static final String MAINTENANCE_GET_SERVICE_TYPE_URL = SERVICE_BASE_URL + "/GetServiceTypes";
	public static final String MAINTENANCE_VALIDATE_PINCODE_SERVICE_URL = SERVICE_BASE_URL + "/ValidateFacilitiesPinCode";
	public static final String MAINTENANCE_GET_LOCATION_SERVICE_URL = SERVICE_BASE_URL + "/GetLocation";
	public static final String MAINTENANCE_GET_FACILITIES_BY_SERVICE_TYPE_URL = SERVICE_BASE_URL + "/getFacilitiesByServiceType";
	
	
	public static final String REGISTER_PUSH_NOTIFICATION_URL = SERVICE_BASE_URL + "/PushNotification";
	
	
	public static final int langType = 0;

	public static final int NEWS_SERVICE_ID = 100;
	public static final int VALIDATE_PINCODE_SERVICE_ID = 101;
	public static final int EMIGRATIONANDVISA_SERVICE_ID = 102;
	public static final int SPORTPARTICIPATION_SERVICE_ID = 103;
	public static final int MAINTENANCE_PUBLIC_SERVICE_ID = 104;
	public static final int MAINTENANCE_CLUB_SERVICE_ID = 105;
	public static final int MAINTENANCE_GET_SERVICE_TYPE_ID = 106;
	public static final int MAINTENANCE_VALIDATE_PINCODE_SERVICE_ID = 107;
	public static final int MAINTENANCE_GET_LOCATION_SERVICE_ID = 108;
	public static final int REGISTER_PUSH_NOTIFICATION_ID = 109;
	public static final int APP_LINKING_SERVICE_ID = 110;
	public static final int MAINTENANCE_GET_FACILITIES_BY_SERVICE_TYPE = 111;
	public static final int EVENTCALENDAR_SERVICE_ID = 112;
	

}
