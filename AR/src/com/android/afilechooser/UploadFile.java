package com.android.afilechooser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.goys.android.app.util.GoysLog;

public class UploadFile extends AsyncTask<String, Void, Integer> {
	private static final String TAG = "UploadFile";

	String sourceFileUri;

	int serverResponseCode = 0;

	private String upLoadServerUri = "http://192.168.133.112:448/FileUpload/index.php";

	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		HttpURLConnection connection = null;

		DataOutputStream outputStream = null;

		String lineEnd = "\r\n";

		String twoHyphens = "--";

		String boundary = "*****";

		String serverResponseMessage;

		int bytesRead, bytesAvailable, bufferSize;

		byte[] buffer;

		int maxBufferSize = 1 * 1024 * 1024;

		File sourceFile = new File(sourceFileUri);

		String fileName = sourceFileUri.substring(
				sourceFileUri.lastIndexOf('/') + 1, sourceFileUri.length());

		if (!sourceFile.isFile()) {
			return 0;
		} else {
			try {
				FileInputStream fileInputStream = new FileInputStream(new File(
						sourceFileUri));
				URL url = new URL(upLoadServerUri);
				connection = (HttpURLConnection) url.openConnection();

				// Allow Inputs &amp; Outputs.
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);

				// Set HTTP method to POST.
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				outputStream = new DataOutputStream(
						connection.getOutputStream());

				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream
						.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
								+ fileName + "\"" + lineEnd);
				outputStream.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// Read file
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens
						+ lineEnd);

				// Responses from the server (code and message)

				serverResponseCode = connection.getResponseCode();

				serverResponseMessage = connection.getResponseMessage();

				fileInputStream.close();

				outputStream.flush();

				outputStream.close();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

				}

			} catch (MalformedURLException ex) {
				ex.printStackTrace();
				GoysLog.d(TAG,
						"MalformedURLException Exception : check script url.");
				GoysLog.e("Upload file to server", "error: " + ex.getMessage(),
						ex);

			} catch (Exception e) {
				e.printStackTrace();
				GoysLog.d(TAG, "Got Exception : see logcat ");
				GoysLog.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}

			return serverResponseCode;

		}

	}
}
