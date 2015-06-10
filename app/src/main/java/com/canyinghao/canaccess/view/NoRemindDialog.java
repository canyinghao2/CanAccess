package com.canyinghao.canaccess.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.view.wheel.ArrayWheelAdapter;
import com.canyinghao.canaccess.view.wheel.WheelVerticalView;
import com.canyinghao.canhelper.SPHepler;

public class NoRemindDialog extends Dialog {

	View.OnClickListener leftclick;
	View.OnClickListener rightclick;

	public NoRemindDialog(final Context context,
                          View.OnClickListener leftclick, View.OnClickListener rightclick) {
		super(context, R.style.CustomDialog);

		this.leftclick = leftclick;
		this.rightclick = rightclick;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_custom_no_remind);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		setCanceledOnTouchOutside(true);

		TextView tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);

		final TextView dialog_left = (TextView) findViewById(R.id.dialog_left);
		TextView dialog_right = (TextView) findViewById(R.id.dialog_right);
		final WheelVerticalView start_time = (WheelVerticalView) findViewById(R.id.start_time);
		final WheelVerticalView end_time = (WheelVerticalView) findViewById(R.id.end_time);
		start_time.setCyclic(true);
		end_time.setCyclic(true);
		String[] strs = new String[24];
		for (int i = 0; i < 24; i++) {
			strs[i] = i + getContext().getString(R.string.houre);

		}

		
		ArrayWheelAdapter adapter = new ArrayWheelAdapter(getContext(), strs);
		adapter.setTextColor(getContext().getResources().getColor(android.R.color.tertiary_text_dark));

		start_time.setViewAdapter(adapter);
		end_time.setViewAdapter(adapter);

		int start_time1 = SPHepler.getInstance().getInt("start_time");
		int end_time1 = SPHepler.getInstance().getInt("end_time");
		start_time.setCurrentItem(start_time1);
		end_time.setCurrentItem(end_time1);
		View.OnClickListener clickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == dialog_left) {
					int start = start_time.getCurrentItem();
					int end = end_time.getCurrentItem();
					

                    SPHepler.getInstance().setInt("start_time",start);
                    SPHepler.getInstance().setInt("end_time",end);

					if (leftclick != null) {
						leftclick.onClick(v);
					}

				} else {
					if (rightclick != null) {
						rightclick.onClick(v);
					}
				}
				dismiss();
			}
		};

		dialog_left.setOnClickListener(clickListener);
		dialog_right.setOnClickListener(clickListener);

	}

}
