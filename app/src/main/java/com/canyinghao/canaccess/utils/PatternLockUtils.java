/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.canyinghao.canaccess.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.canyinghao.canaccess.activity.set.PwdConfirmActivity;
import com.canyinghao.canaccess.activity.set.PwdSetActivity;

import java.util.List;

import me.zhanghai.patternlock.PatternUtils;
import me.zhanghai.patternlock.PatternView;

public class PatternLockUtils {

    public static final int REQUEST_CODE_CONFIRM_PATTERN = 208;

    public static void setPattern(List<PatternView.Cell> pattern, Context context) {
        PreferenceUtils.putString(PreferenceContract.KEY_PATTERN_SHA1,
                PatternUtils.patternToSha1String(pattern), context);
    }

    private static String getPatternSha1(Context context) {
        return PreferenceUtils.getString(PreferenceContract.KEY_PATTERN_SHA1,
                PreferenceContract.DEFAULT_PATTERN_SHA1, context);
    }

    public static boolean hasPattern(Context context) {
        return !TextUtils.isEmpty(getPatternSha1(context));
    }

    public static boolean isPatternCorrect(List<PatternView.Cell> pattern, Context context) {
        return TextUtils.equals(PatternUtils.patternToSha1String(pattern), getPatternSha1(context));
    }

    public static void clearPattern(Context context) {
        PreferenceUtils.remove(PreferenceContract.KEY_PATTERN_SHA1, context);
    }

    public static void setPatternByUser(Context context) {
        context.startActivity(new Intent(context, PwdSetActivity.class));
    }

    // NOTE: Should only be called when there is a pattern for this account.
    public static void confirmPattern(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PwdConfirmActivity.class),
                requestCode);
    }

    public static void confirmPattern(Activity activity) {
        confirmPattern(activity, REQUEST_CODE_CONFIRM_PATTERN);
    }

    public static void confirmPatternIfHas(Activity activity) {
        if (hasPattern(activity)) {
            confirmPattern(activity, REQUEST_CODE_CONFIRM_PATTERN);
        }
    }

    public static boolean checkConfirmPatternResult(Activity activity, int requestCode,
                                                    int resultCode) {
        if (requestCode == REQUEST_CODE_CONFIRM_PATTERN && resultCode != Activity.RESULT_OK) {
            activity.finish();
            return true;
        } else {
            return false;
        }
    }

    private PatternLockUtils() {}
}