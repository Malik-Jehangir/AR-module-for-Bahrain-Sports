package com.goys.android.app.webview;

import java.util.Locale;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity;
import android.os.Bundle;

import com.goys.android.app.R;

public class myslocator extends Activity {
private WebView MyWeb;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myslocator);
		MyWeb = (WebView) findViewById(R.id.myslocatorView);
		MyWeb.getSettings().setJavaScriptEnabled(true);
		MyWeb.setWebViewClient(new WebViewClient());
		MyWeb.loadUrl("http://www.mys.gov.bh/pages/gisapp/myslocator.aspx");
	}
}
