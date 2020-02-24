package com.goys.android.app.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class GOYSTextView extends TextView {

	public GOYSTextView(Context context) {
		super(context);
		TypefaceCache.setCustomTypeface(context, this, null);
	}

	public GOYSTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypefaceCache.setCustomTypeface(context, this, attrs);
	}

	public GOYSTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypefaceCache.setCustomTypeface(context, this, attrs);
	}

}
