package com.grid.hdsyt.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯  TextView
 * @author PC
 *
 */
public class MarqueeTextView extends TextView {
	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused)
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (hasWindowFocus)
			super.onWindowFocusChanged(hasWindowFocus);
	}
	@Override
	public boolean isFocused() {
		return true;
	}
}