package com.keyhua.renameyourself.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

@SuppressLint("Instantiatable")
public class NoScrollView {
	public class NoScrollGridView extends GridView {

		public NoScrollGridView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public NoScrollGridView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
		}

		public NoScrollGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int mExpandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, mExpandSpec);
		}
	}

	public class NoScrollListView extends ListView {

		public NoScrollListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public NoScrollListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int mExpandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, mExpandSpec);
		}
	}

}
