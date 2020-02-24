package com.goys.android.app.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goys.android.app.GOYSApplication;
import com.goys.android.app.db.model.Country;

public class CommonObjects {

	public static final String DATA = "data";

	public static final String MESSAGE = "message";

	public static final String ERROR = "error";

	public static final String STATUS = "status";

	public static final String DEVICE_TYPE = "1";

	public static boolean isEmpty(String str) {
		if ((str == null) || str.matches("^\\s*$")) {
			return true;
		} else {
			if (str.equalsIgnoreCase("null")) {
				return true;
			} else if (str.contains("null")) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static String getFormattedString(String str) {
		if (isEmpty(str)) {
			return "--";
		}

		return str;
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}

		return isValid;
	}

	public static boolean isValidUrl(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	public String getRandomNumber(int digCount) {
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder(digCount);
		for (int i = 0; i < digCount; i++)
			sb.append((char) ('0' + rnd.nextInt(10)));
		return sb.toString();

	}

	public static boolean isNumeric(String str) {
		try {
			@SuppressWarnings("unused")
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isNumericFloat(String str) {
		try {
			@SuppressWarnings("unused")
			float f = Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String getGMTDate(String dob) {

		String dateNewFormat;// "2-26-1990";

		String oldFormat = "MM-dd-yyyy";
		String newFormat = "yyyy-MM-dd";

		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat, Locale.ENGLISH);

		try {

			dateNewFormat = sdf2.format(sdf1.parse(dob));

			GoysLog.e("dob_new_format", dateNewFormat);

			SimpleDateFormat date_formater = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
			date_formater.setTimeZone(TimeZone.getTimeZone("GMT"));

			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
					Locale.ENGLISH);
			Date start_date = cal.getTime();

			String time_starts = start_date.getTime() + "";
			time_starts = time_starts.substring(0, 10);

			String date_start_GMT = date_formater.format(start_date);

			GoysLog.e("GMT_date_start", date_start_GMT);

			date_start_GMT = dateNewFormat
					+ date_start_GMT.substring(date_start_GMT.indexOf("T"),
							date_start_GMT.length());

			GoysLog.e("Final dob", date_start_GMT);

			return date_start_GMT;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return e.getMessage();
		}

	}

	public static String isValidDate(String checkDate) {

		Calendar c = Calendar.getInstance();
		int current_year = c.get(Calendar.YEAR);
		int current_month = c.get(Calendar.MONTH);
		int current_day = c.get(Calendar.DAY_OF_MONTH);

		String currentDate = new StringBuilder().append(current_year)
				.append("-").append(current_month + 1).append("-")
				.append(current_day).toString();

		GoysLog.e("Current Sys Date", currentDate);
		GoysLog.e("Check Date", checkDate);

		String dateNewFormat;// "2-26-1990";

		String oldFormat = "yyyy-MM-dd";
		String newFormat = "yyyy-MM-dd";

		String result = "";

		try {

			SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat,
					Locale.ENGLISH);
			SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat,
					Locale.ENGLISH);

			dateNewFormat = sdf2.format(sdf1.parse(checkDate));

			GoysLog.e("dob_new_format", dateNewFormat);

			// Date date1 = sdf.parse("2010-01-31");
			// Date date2 = sdf.parse("2010-01-31");

			Date check_date = sdf2.parse(dateNewFormat);
			Date current_date = sdf2.parse(currentDate);

			System.out.println(sdf2.format(check_date));
			System.out.println(sdf2.format(current_date));

			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(check_date);
			cal2.setTime(current_date);

			if (cal1.after(cal2)) {

				System.out.println("check_date is after current_date");

				result = "after_today";
			}

			if (cal1.before(cal2)) {

				System.out.println("check_date is before current_date");

				result = "before_today";
			}

			if (cal1.equals(cal2)) {

				System.out.println("check_date is equal current_date");

				result = "today";
			}

			return result;

		} catch (ParseException ex) {
			ex.printStackTrace();

			return result;
		}
	}

	public static String checkEndDate(String start_date, String end_date) {

		String start_dateNewFormat;// "2-26-1990";
		String end_dateNewFormat;

		String oldFormat = "MM-dd-yyyy";
		String newFormat = "yyyy-MM-dd";

		String result = "";

		try {

			SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat,
					Locale.ENGLISH);
			SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat,
					Locale.ENGLISH);

			start_dateNewFormat = sdf2.format(sdf1.parse(start_date));
			end_dateNewFormat = sdf2.format(sdf1.parse(end_date));

			GoysLog.e("start_date_new_format", start_dateNewFormat);
			GoysLog.e("end_date_new_format", end_dateNewFormat);

			Date end_date1 = sdf2.parse(end_dateNewFormat);
			Date start_date2 = sdf2.parse(start_dateNewFormat);

			System.out.println(sdf2.format(end_date1));
			System.out.println(sdf2.format(start_date2));

			if (end_date1.after(start_date2)) {

				System.out.println("end_date1 is after start_date2");

				result = "after";
			}

			if (end_date1.before(start_date2)) {

				System.out.println("end_date1 is before start_date2");

				result = "before";
			}

			if (end_date1.equals(start_date2)) {

				System.out.println("end_date1 is equal start_date2");

				result = "today";
			}

			return result;

		} catch (ParseException ex) {
			ex.printStackTrace();

			return result;
		}
	}

	public static int getDaysDifferece(String start_date, String end_date) {

		String start_dateNewFormat;// "2-26-1990";
		String end_dateNewFormat;

		String oldFormat = "MM-dd-yyyy";
		String newFormat = "yyyy-MM-dd";

		int days = 0;

		try {

			SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat,
					Locale.ENGLISH);
			SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat,
					Locale.ENGLISH);

			start_dateNewFormat = sdf2.format(sdf1.parse(start_date));
			end_dateNewFormat = sdf2.format(sdf1.parse(end_date));

			GoysLog.e("start_date_new_format", start_dateNewFormat);
			GoysLog.e("end_date_new_format", end_dateNewFormat);

			Date end_date1 = sdf2.parse(end_dateNewFormat);
			Date start_date2 = sdf2.parse(start_dateNewFormat);

			days = (int) ((end_date1.getTime() - start_date2.getTime()) / (1000 * 60 * 60 * 24));

			GoysLog.e(
					"Days",
					""
							+ (int) ((end_date1.getTime() - start_date2
									.getTime()) / (1000 * 60 * 60 * 24)));

			return days;

		} catch (ParseException ex) {
			ex.printStackTrace();

			return days;
		}
	}

	public static String getGMTParsedDate(String serverDate) {

		String birthdate = serverDate;

		birthdate = birthdate.substring(0, birthdate.indexOf("T"));

		String oldFormat = "yyyy-MM-dd";
		String newFormat = "MM-dd-yyyy";

		SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat, Locale.ENGLISH);

		try {

			birthdate = sdf2.format(sdf1.parse(birthdate));

			System.out.println(sdf2.format(sdf1.parse(birthdate)));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GoysLog.e("dob", birthdate);

		return birthdate;

	}

	public static String formatDate(int i, Date date) {

		String dateString = null;

		if (i == 1) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH);
			dateString = dateFormatter.format(date);

		} else if (i == 2) {
			// SimpleDateFormat dateFormatter = new
			// SimpleDateFormat("dd MMM,yyyy", Locale.ENGLISH);
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"dd MMM,yyyy", Locale.ENGLISH);
			dateString = dateFormatter.format(date);
		} else if (i == 3) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"dd MMM,yyyy HH:mm", Locale.ENGLISH);
			dateString = dateFormatter.format(date);
		}

		return dateString;
	}

	public static String getCropURL(String url, String width) {
		String urlFinal = "http://www.uaefa.ae/api/image_resize.php?url=" + url
				+ "&width=" + width;
		return urlFinal;
	}

	public static String getImageURL(String type, String id) {
		return "https://fanet.ae/uaefa/LoadImage.aspx?type=" + type + "&id="
				+ id;
	}

	public static Date convertToDate(String dateString, String format) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(format,
				Locale.ENGLISH);
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertedDate;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	/**/
	public static ArrayList<Country> getCountriesList() {
		ArrayList<Country> list = new ArrayList<Country>();

		if (GOYSApplication.getInstance().isEnglishLanguage()) {

			list.add(new Country(213, "Afghanistan"));
			list.add(new Country(214, "Albania"));
			list.add(new Country(215, "Algeria"));
			list.add(new Country(216, "Andorra"));
			list.add(new Country(217, "Angola"));
			list.add(new Country(218, "Argentina"));
			list.add(new Country(219, "Armenia"));
			list.add(new Country(220, "Australia"));
			list.add(new Country(221, "Austria"));
			list.add(new Country(222, "Azerbaijan"));
			list.add(new Country(223, "Bahamas"));
			list.add(new Country(224, "Bahrain"));
			list.add(new Country(225, "Bangladesh"));
			list.add(new Country(226, "Barbados"));
			list.add(new Country(227, "Belarus"));
			list.add(new Country(228, "Belgium"));
			list.add(new Country(229, "Belize"));
			list.add(new Country(230, "Benin"));
			list.add(new Country(231, "Bermuda"));
			list.add(new Country(232, "Bhutan"));
			list.add(new Country(233, "Bolivia"));
			list.add(new Country(234, "Bosnia and Herzegovina"));
			list.add(new Country(235, "Botswana"));
			list.add(new Country(236, "British Virgin Islands"));
			list.add(new Country(237, "Brunei"));
			list.add(new Country(238, "Bulgaria"));
			list.add(new Country(239, "Burkina Faso"));
			list.add(new Country(240, "Burundi"));
			list.add(new Country(241, "Cambodia"));
			list.add(new Country(242, "Cameroon"));
			list.add(new Country(243, "Canada"));
			list.add(new Country(244, "Cape Verde"));
			list.add(new Country(245, "Cayman Islands"));
			list.add(new Country(246, "Central African Republic"));
			list.add(new Country(247, "Chad"));
			list.add(new Country(248, "Chile"));
			list.add(new Country(249, "China"));
			list.add(new Country(250, "Colombia"));
			list.add(new Country(251, "Congo-Brazzaville"));
			list.add(new Country(252, "Congo-Kinshasa"));
			list.add(new Country(253, "Costa Rica"));
			list.add(new Country(254, "Croatia"));
			list.add(new Country(255, "Cuba"));
			list.add(new Country(256, "Cyprus"));
			list.add(new Country(257, "Czech Republic"));
			list.add(new Country(258, "Denmark"));
			list.add(new Country(259, "Djibouti"));
			list.add(new Country(260, "Dominica"));
			list.add(new Country(261, "Dominican Republic"));
			list.add(new Country(262, "East Timor"));
			list.add(new Country(263, "Ecuador"));
			list.add(new Country(264, "Egypt"));
			list.add(new Country(265, "El Salvador"));
			list.add(new Country(266, "Equatorial Guinea"));
			list.add(new Country(267, "Eritrea"));
			list.add(new Country(268, "Estonia"));
			list.add(new Country(269, "Ethiopia"));
			list.add(new Country(270, "Falkland Islands"));
			list.add(new Country(271, "Fiji"));
			list.add(new Country(272, "Finland"));
			list.add(new Country(273, "France"));
			list.add(new Country(274, "French Guiana"));
			list.add(new Country(275, "French Polynesia"));
			list.add(new Country(276, "Gabon"));
			list.add(new Country(277, "Gambia"));
			list.add(new Country(278, "Georgia"));
			list.add(new Country(279, "Germany"));
			list.add(new Country(280, "Ghana"));
			list.add(new Country(281, "Gibraltar"));
			list.add(new Country(282, "Greece"));
			list.add(new Country(283, "Greenland"));
			list.add(new Country(284, "Grenada"));
			list.add(new Country(285, "Guadeloupe"));
			list.add(new Country(286, "Guam"));
			list.add(new Country(287, "Guatemala"));
			list.add(new Country(288, "Guernsey"));
			list.add(new Country(289, "Guinea"));
			list.add(new Country(290, "Guinea-Bissau"));
			list.add(new Country(291, "Guyana"));
			list.add(new Country(292, "Haiti"));
			list.add(new Country(293, "Honduras"));
			list.add(new Country(294, "Hong Kong"));
			list.add(new Country(295, "Hungary"));
			list.add(new Country(296, "Iceland"));
			list.add(new Country(297, "India"));
			list.add(new Country(298, "Indonesia"));
			list.add(new Country(299, "Iran"));
			list.add(new Country(300, "Iraq"));
			list.add(new Country(301, "Ireland"));
			list.add(new Country(302, "Isle of Man"));
			list.add(new Country(303, "Italy"));
			list.add(new Country(304, "Ivory Coast"));
			list.add(new Country(305, "Jamaica"));
			list.add(new Country(306, "Japan"));
			list.add(new Country(307, "Jersey"));
			list.add(new Country(308, "Jordan"));
			list.add(new Country(309, "Kazakhstan"));
			list.add(new Country(310, "Kenya"));
			list.add(new Country(311, "Kingdom of Saudi Arabia"));
			list.add(new Country(312, "Kiribati"));
			list.add(new Country(313, "Kosovo"));
			list.add(new Country(314, "Kuwait"));
			list.add(new Country(315, "Kyrgyzstan"));
			list.add(new Country(316, "Laos"));
			list.add(new Country(317, "Latvia"));
			list.add(new Country(318, "Lebanon"));
			list.add(new Country(319, "Lesotho"));
			list.add(new Country(320, "Liberia"));
			list.add(new Country(321, "Libya"));
			list.add(new Country(322, "Liechtenstein"));
			list.add(new Country(323, "Lithuania"));
			list.add(new Country(324, "Luxembourg"));
			list.add(new Country(325, "Macau"));
			list.add(new Country(326, "Macedonia"));
			list.add(new Country(327, "Madagascar"));
			list.add(new Country(328, "Malawi"));
			list.add(new Country(329, "Malaysia"));
			list.add(new Country(330, "Maldives"));
			list.add(new Country(331, "Mali"));
			list.add(new Country(332, "Malta"));
			list.add(new Country(333, "Marshall Islands"));
			list.add(new Country(334, "Martinique"));
			list.add(new Country(335, "Mauritania"));
			list.add(new Country(336, "Mauritius"));
			list.add(new Country(337, "Mexico"));
			list.add(new Country(338, "Micronesia"));
			list.add(new Country(339, "Moldova"));
			list.add(new Country(340, "Monaco"));
			list.add(new Country(341, "Mongolia"));
			list.add(new Country(342, "Montenegro"));
			list.add(new Country(343, "Montserrat"));
			list.add(new Country(344, "Morocco"));
			list.add(new Country(345, "Mozambique"));
			list.add(new Country(346, "Myanmar (Burma)"));
			list.add(new Country(347, "Namibia"));
			list.add(new Country(348, "Nauru"));
			list.add(new Country(349, "Nepal"));
			list.add(new Country(350, "Netherlands"));
			list.add(new Country(351, "New Caledonia"));
			list.add(new Country(352, "New Zealand"));
			list.add(new Country(353, "Nicaragua"));
			list.add(new Country(354, "Niger"));
			list.add(new Country(355, "Nigeria"));
			list.add(new Country(356, "North Korea"));
			list.add(new Country(357, "Norway"));
			list.add(new Country(358, "Oman"));
			list.add(new Country(359, "Pakistan"));
			list.add(new Country(360, "Palestine"));
			list.add(new Country(361, "Panama"));
			list.add(new Country(362, "Papua New Guinea"));
			list.add(new Country(363, "Paraguay"));
			list.add(new Country(364, "Peru"));
			list.add(new Country(365, "Philippines"));
			list.add(new Country(366, "Poland"));
			list.add(new Country(367, "Portugal"));
			list.add(new Country(368, "Puerto Rico"));
			list.add(new Country(369, "Qatar"));
			list.add(new Country(370, "Reunion"));
			list.add(new Country(371, "Romania"));
			list.add(new Country(372, "Russia"));
			list.add(new Country(373, "Rwanda"));
			list.add(new Country(374, "Samoa"));
			list.add(new Country(375, "San Marino"));
			list.add(new Country(376, "Sao Tome and Principe"));
			list.add(new Country(377, "Senegal"));
			list.add(new Country(378, "Serbia"));
			list.add(new Country(379, "Seychelles"));
			list.add(new Country(380, "Sierra Leone"));
			list.add(new Country(381, "Singapore"));
			list.add(new Country(382, "Slovakia"));
			list.add(new Country(383, "Slovenia"));
			list.add(new Country(384, "Solomon Islands"));
			list.add(new Country(385, "Somalia"));
			list.add(new Country(386, "South Africa"));
			list.add(new Country(387, "South Korea"));
			list.add(new Country(388, "Spain"));
			list.add(new Country(389, "Sri Lanka"));
			list.add(new Country(390, "Sudan"));
			list.add(new Country(391, "Suriname"));
			list.add(new Country(392, "Swaziland"));
			list.add(new Country(393, "Sweden"));
			list.add(new Country(394, "Switzerland"));
			list.add(new Country(395, "Syria"));
			list.add(new Country(396, "Taiwan"));
			list.add(new Country(397, "Tajikistan"));
			list.add(new Country(398, "Tanzania"));
			list.add(new Country(399, "Thailand"));
			list.add(new Country(400, "Togo"));
			list.add(new Country(401, "Tonga"));
			list.add(new Country(402, "Trinidad and Tobago"));
			list.add(new Country(403, "Tunisia"));
			list.add(new Country(404, "Turkey"));
			list.add(new Country(405, "Turkmenistan"));
			list.add(new Country(406, "Tuvalu"));
			list.add(new Country(407, "Uganda"));
			list.add(new Country(408, "Ukraine"));
			list.add(new Country(409, "United Arab Emirates"));
			list.add(new Country(410, "United Kingdom"));
			list.add(new Country(411, "United States"));
			list.add(new Country(412, "United States Virgin Islands"));
			list.add(new Country(413, "Uruguay"));
			list.add(new Country(414, "Uzbekistan"));
			list.add(new Country(415, "Vanuatu"));
			list.add(new Country(416, "Vatican City"));
			list.add(new Country(417, "Venezuela"));
			list.add(new Country(418, "Vietnam"));
			list.add(new Country(419, "Wallis and Futuna"));
			list.add(new Country(420, "Western Sahara"));
			list.add(new Country(421, "Yemen"));
			list.add(new Country(422, "Zambia"));
			list.add(new Country(423, "Zimbabwe"));
		} else {

			list.add(new Country(269, "أثيوبيا"));
			list.add(new Country(222, "أذربيجان"));
			list.add(new Country(219, "أرمينيا"));
			list.add(new Country(267, "إريتريا"));
			list.add(new Country(388, "إسبانيا"));
			list.add(new Country(220, "أستراليا"));
			list.add(new Country(268, "استونيا"));
			list.add(new Country(213, "أفغانستان"));
			list.add(new Country(218, "الأرجنتين"));
			list.add(new Country(308, "الأردن"));
			list.add(new Country(263, "الاكوادور"));
			list.add(new Country(409, "الأمارات العربية المتحدة"));
			list.add(new Country(214, "ألبانيا"));
			list.add(new Country(224, "البحرين"));
			list.add(new Country(367, "البرتغال"));
			list.add(new Country(234, "البوسنة والهرسك"));
			list.add(new Country(342, "الجبل الأسود"));
			list.add(new Country(215, "الجزائر"));
			list.add(new Country(412, "الجزر العذراء الأمريكية"));
			list.add(new Country(258, "الدنمارك"));
			list.add(new Country(244, "الرأس الأخضر"));
			list.add(new Country(265, "السلفادور"));
			list.add(new Country(377, "السنغال"));
			list.add(new Country(393, "السويد"));
			list.add(new Country(420, "الصحراء الغربية"));
			list.add(new Country(385, "الصومال"));
			list.add(new Country(249, "الصين"));
			list.add(new Country(300, "العراق"));
			list.add(new Country(276, "الغابون"));
			list.add(new Country(365, "الفلبين"));
			list.add(new Country(242, "الكاميرون"));
			list.add(new Country(251, "الكونغو برازافيل"));
			list.add(new Country(314, "الكويت"));
			list.add(new Country(279, "ألمانيا"));
			list.add(new Country(344, "المغرب"));
			list.add(new Country(337, "المكسيك"));
			list.add(new Country(311, "المملكة العربية السعودية"));
			list.add(new Country(410, "المملكة المتحدة"));
			list.add(new Country(357, "النرويج"));
			list.add(new Country(221, "النمسا"));
			list.add(new Country(354, "النيجر"));
			list.add(new Country(297, "الهند"));
			list.add(new Country(411, "الولايات المتحدة"));
			list.add(new Country(306, "اليابان"));
			list.add(new Country(421, "اليمن"));
			list.add(new Country(216, "أندورا"));
			list.add(new Country(298, "أندونيسيا"));
			list.add(new Country(217, "أنغولا"));
			list.add(new Country(413, "أوروغواي"));
			list.add(new Country(414, "أوزبكستان"));
			list.add(new Country(407, "أوغندا"));
			list.add(new Country(408, "أوكرانيا"));
			list.add(new Country(299, "ايران"));
			list.add(new Country(301, "ايرلندا"));
			list.add(new Country(296, "أيسلندا"));
			list.add(new Country(303, "إيطاليا"));
			list.add(new Country(362, "بابوا غينيا الجديدة"));
			list.add(new Country(363, "باراغواي"));
			list.add(new Country(359, "باكستان"));
			list.add(new Country(226, "بربادوس"));
			list.add(new Country(231, "برمودا"));
			list.add(new Country(237, "بروناي"));
			list.add(new Country(228, "بلجيكا"));
			list.add(new Country(238, "بلغاريا"));
			list.add(new Country(229, "بليز"));
			list.add(new Country(361, "بناما"));
			list.add(new Country(225, "بنغلاديش"));
			list.add(new Country(230, "بنين"));
			list.add(new Country(232, "بوتان"));
			list.add(new Country(235, "بوتسوانا"));
			list.add(new Country(368, "بورتوريكو"));
			list.add(new Country(239, "بوركينا فاسو"));
			list.add(new Country(240, "بوروندي"));
			list.add(new Country(366, "بولندا"));
			list.add(new Country(233, "بوليفيا"));
			list.add(new Country(275, "بولينيزيا الفرنسية"));
			list.add(new Country(364, "بيرو"));
			list.add(new Country(399, "تايلاند"));
			list.add(new Country(396, "تايوان"));
			list.add(new Country(405, "تركمانستان"));
			list.add(new Country(404, "تركيا"));
			list.add(new Country(402, "ترينيداد وتوباغو"));
			list.add(new Country(247, "تشاد"));
			list.add(new Country(398, "تنزانيا"));
			list.add(new Country(400, "توغو"));
			list.add(new Country(406, "توفالو"));
			list.add(new Country(403, "تونس"));
			list.add(new Country(401, "تونغا"));
			list.add(new Country(262, "تيمور الشرقية"));
			list.add(new Country(305, "جامايكا"));
			list.add(new Country(281, "جبل طارق"));
			list.add(new Country(223, "جزر البهاما"));
			list.add(new Country(330, "جزر المالديف"));
			list.add(new Country(384, "جزر سليمان"));
			list.add(new Country(270, "جزر فوكلاند"));
			list.add(new Country(236, "جزر فيرجن البريطانية"));
			list.add(new Country(245, "جزر كايمان"));
			list.add(new Country(333, "جزر مارشال"));
			list.add(new Country(302, "جزيرة آيل أوف مان"));
			list.add(new Country(246, "جمهورية أفريقيا الوسطى"));
			list.add(new Country(257, "جمهورية التشيك"));
			list.add(new Country(261, "جمهورية الدومينيكان"));
			list.add(new Country(252, "جمهورية الكونغو الديمقراطية"));
			list.add(new Country(386, "جنوب أفريقيا"));
			list.add(new Country(285, "جواديلوب"));
			list.add(new Country(278, "جورجيا"));
			list.add(new Country(259, "جيبوتي"));
			list.add(new Country(307, "جيرسي"));
			list.add(new Country(288, "جيرنزي"));
			list.add(new Country(260, "دومينيكا"));
			list.add(new Country(373, "رواندا"));
			list.add(new Country(372, "روسيا"));
			list.add(new Country(227, "روسيا البيضاء"));
			list.add(new Country(371, "رومانيا"));
			list.add(new Country(370, "ريونيون"));
			list.add(new Country(422, "زامبيا"));
			list.add(new Country(423, "زيمبابوي"));
			list.add(new Country(304, "ساحل العاج"));
			list.add(new Country(374, "ساموا"));
			list.add(new Country(375, "سان مارينو"));
			list.add(new Country(376, "ساو تومي وبرينسيبي"));
			list.add(new Country(389, "سري لانكا"));
			list.add(new Country(382, "سلوفاكيا"));
			list.add(new Country(383, "سلوفينيا"));
			list.add(new Country(381, "سنغافورة"));
			list.add(new Country(392, "سوازيلاند"));
			list.add(new Country(390, "سودان"));
			list.add(new Country(395, "سوريا"));
			list.add(new Country(391, "سورينام"));
			list.add(new Country(394, "سويسرا"));
			list.add(new Country(380, "سيراليون"));
			list.add(new Country(379, "سيشيل"));
			list.add(new Country(248, "شيلي"));
			list.add(new Country(378, "صربيا"));
			list.add(new Country(397, "طاجيكستان"));
			list.add(new Country(358, "عمان"));
			list.add(new Country(277, "غامبيا"));
			list.add(new Country(280, "غانا"));
			list.add(new Country(284, "غرينادا"));
			list.add(new Country(283, "غرينلاند"));
			list.add(new Country(287, "غواتيمالا"));
			list.add(new Country(286, "غوام"));
			list.add(new Country(291, "غيانا"));
			list.add(new Country(274, "غيانا الفرنسية"));
			list.add(new Country(289, "غينيا"));
			list.add(new Country(266, "غينيا الاستوائية"));
			list.add(new Country(290, "غينيا بيساو"));
			list.add(new Country(415, "فانواتو"));
			list.add(new Country(273, "فرنسا"));
			list.add(new Country(360, "فلسطين"));
			list.add(new Country(417, "فنزويلا"));
			list.add(new Country(272, "فنلندا"));
			list.add(new Country(418, "فيتنام"));
			list.add(new Country(271, "فيجي"));
			list.add(new Country(256, "قبرص"));
			list.add(new Country(369, "قطر"));
			list.add(new Country(315, "قيرغيزستان"));
			list.add(new Country(309, "كازاخستان"));
			list.add(new Country(351, "كاليدونيا الجديدة"));
			list.add(new Country(254, "كرواتيا"));
			list.add(new Country(241, "كمبوديا"));
			list.add(new Country(243, "كندا"));
			list.add(new Country(255, "كوبا"));
			list.add(new Country(387, "كوريا الجنوبية"));
			list.add(new Country(356, "كوريا الشمالية"));
			list.add(new Country(253, "كوستاريكا"));
			list.add(new Country(313, "كوسوفو"));
			list.add(new Country(250, "كولومبيا"));
			list.add(new Country(312, "كيريباتي"));
			list.add(new Country(310, "كينيا"));
			list.add(new Country(317, "لاتفيا"));
			list.add(new Country(316, "لاوس"));
			list.add(new Country(318, "لبنان"));
			list.add(new Country(324, "لوكسمبورغ"));
			list.add(new Country(321, "ليبيا"));
			list.add(new Country(320, "ليبيريا"));
			list.add(new Country(323, "ليتوانيا"));
			list.add(new Country(322, "ليختنشتاين"));
			list.add(new Country(319, "ليسوتو"));
			list.add(new Country(334, "مارتينيك"));
			list.add(new Country(325, "ماكاو"));
			list.add(new Country(332, "مالطا"));
			list.add(new Country(331, "مالي"));
			list.add(new Country(329, "ماليزيا"));
			list.add(new Country(327, "مدغشقر"));
			list.add(new Country(416, "مدينة الفاتيكان"));
			list.add(new Country(264, "مصر"));
			list.add(new Country(326, "مقدونيا"));
			list.add(new Country(328, "ملاوي"));
			list.add(new Country(341, "منغوليا"));
			list.add(new Country(335, "موريتانيا"));
			list.add(new Country(336, "موريشيوس"));
			list.add(new Country(345, "موزمبيق"));
			list.add(new Country(339, "مولدافيا"));
			list.add(new Country(340, "موناكو"));
			list.add(new Country(343, "مونتسيرات"));
			list.add(new Country(346, "ميانمار (بورما)"));
			list.add(new Country(338, "ميكرونيزيا"));
			list.add(new Country(347, "ناميبيا"));
			list.add(new Country(348, "ناورو"));
			list.add(new Country(349, "نيبال"));
			list.add(new Country(355, "نيجيريا"));
			list.add(new Country(353, "نيكاراغوا"));
			list.add(new Country(352, "نيوزيلندا"));
			list.add(new Country(292, "هايتي"));
			list.add(new Country(293, "هندوراس"));
			list.add(new Country(295, "هنغاريا"));
			list.add(new Country(350, "هولندا"));
			list.add(new Country(294, "هونغ كونغ"));
			list.add(new Country(419, "واليس وفوتونا"));
			list.add(new Country(282, "يونان"));
		}

		return list;
	}

}
