package com.goys.android.app.webview;

import java.util.Locale;

import com.goys.android.app.R;
import com.goys.android.app.util.GoysLog;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;
import android.os.Bundle;

public class happyform extends Activity {

private WebView MyWeb;
String language=Locale.getDefault().getLanguage();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_happyform);
		MyWeb = (WebView) findViewById(R.id.happyformview);
		MyWeb.getSettings().setJavaScriptEnabled(true);
		MyWeb.setWebViewClient(new WebViewClient());
		  if(language.equals("ar")){
			  MyWeb.loadUrl("http://www.mys.gov.bh/Pages/Feedback.aspx?lang=ar");
		  }else{
			  MyWeb.loadUrl("http://www.mys.gov.bh/Pages/Feedback.aspx?lang=en");
		  }
	}
}
