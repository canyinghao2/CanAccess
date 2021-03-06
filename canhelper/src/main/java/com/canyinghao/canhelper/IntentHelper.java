package com.canyinghao.canhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

/**
 * 传值、跳转工具类
 * @author canyinghao
 *
 */
public class IntentHelper {

	private static IntentHelper util;

	synchronized static public IntentHelper getInstance() {

		if (util == null) {
			util = new IntentHelper();

		}
		return util;

	}

	private IntentHelper() {
		super();
	}

	/**
	 * 页面跳转
	 * 
	 * @param context
	 * @param c
	 */
	public void showIntent(final Context context, final Class c) {
		showIntent(context, c, null, null,false);
	}

    /**
	 * 页面跳转
	 *
	 * @param context
	 * @param c
	 */
	public void showIntent(final Context context, final Class c,boolean flag) {
		showIntent(context, c, null, null,flag);
	}

	/**
	 * 传数据的页面跳转
	 * 
	 * @param context
	 * @param c
	 * @param keys
	 * @param values
	 */
	public void showIntent(final Context context, final Class c, String[] keys,
			Serializable[] values,boolean flag) {
		Intent intent = new Intent();
		intent.setClass(context, c);
		if (null != keys) {
			int i = 0;
			for (String key : keys) {
				intent.putExtra(key, values[i]);
				i++;
			}
		}

		context.startActivity(intent);
        if (flag&&context instanceof Activity){
            ((Activity)context).overridePendingTransition(R.anim.activity_switch_push_left_in,
                    R.anim.activity_switch_push_left_out);
        }


	}

	public void showIntentForResult(final Activity context, final Class c,
			String[] keys, Serializable[] values, int result) {
		Intent intent = new Intent();
		intent.setClass(context, c);
		if (null != keys) {
			int i = 0;
			for (String key : keys) {
				intent.putExtra(key, values[i]);
				i++;
			}
		}

		context.startActivityForResult(intent, result);

		// context.overridePendingTransition(
		// R.anim.appear_top_left_in,
		// R.anim.disappear_bottom_right_out);

	}

	/**
	 * 用动画效果的页面关闭
	 * 
	 * @param activity
	 */
	public void finish(Activity activity) {
		activity.finish();
        activity.overridePendingTransition(R.anim.activity_switch_push_right_in,R.anim.activity_switch_push_right_out
        );

	}
}
