package com.goys.android.app.util;

public interface ResponseListener {
	public void onResponse(int serviceId, String responseObj);

	public void onError(int responseCode, String msg);
}
