/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.canyinghao.canaccess.activity.set;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.MainActivity;
import com.canyinghao.canaccess.utils.PatternLockUtils;
import com.canyinghao.canaccess.utils.PreferenceContract;
import com.canyinghao.canaccess.utils.PreferenceUtils;
import com.canyinghao.canhelper.IntentHelper;
import com.canyinghao.canhelper.SPHepler;

import java.util.List;

import me.zhanghai.patternlock.PatternView;


public class PwdConfirmActivity extends me.zhanghai.patternlock.ConfirmPatternActivity {
    Class cls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra("class")) {
            cls = (Class) intent.getSerializableExtra("class");
        }

        if (SPHepler.getInstance().getInt("lock") != 1 && cls == null) {

            IntentHelper.getInstance().showIntent(this, MainActivity.class);
            finish();
        }


    }

    @Override
    protected boolean isStealthModeEnabled() {
        return !PreferenceUtils.getBoolean(PreferenceContract.KEY_PATTERN_VISIBLE,
                PreferenceContract.DEFAULT_PATTERN_VISIBLE, this);
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        return PatternLockUtils.isPatternCorrect(pattern, this);
    }

    @Override
    public void onForgotPassword() {

//        startActivity(new Intent(this, ResetPatternActivity.class));


        showEditDialog();


    }

    @Override
    public void onCancel() {
        super.onCancel();
    }

    @Override
    public void onConfirmed() {
        if (cls != null) {
            IntentHelper.getInstance().showIntent(this, cls);
        } else {

            IntentHelper.getInstance().showIntent(this, MainActivity.class);

        }

        finish();
    }


    public void showEditDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_ignore, null);
        AppCompatTextView tv= (AppCompatTextView) v.findViewById(R.id.title);
        final TextInputLayout textinput1 = (TextInputLayout) v.findViewById(R.id.textInput1);
        final TextInputLayout textinput2 = (TextInputLayout) v.findViewById(R.id.textInput2);
        textinput1.setHint(getString(R.string.pwd_question));


        textinput2.setVisibility(View.GONE);
        textinput1.setErrorEnabled(true);

        final EditText et1 = textinput1.getEditText();


        tv.setText(getString(R.string.pwd_forget));

        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && TextUtils.isEmpty(et1.getText().toString())) {
                    textinput1.setError(getString(R.string.ignore_empty));
                }
            }
        });



        builder.setView(v);

        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (true){
                    onConfirmed();
                }


            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }
}

