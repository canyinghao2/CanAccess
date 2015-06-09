package com.canyinghao.canaccess.activity.set;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.utils.PatternLockUtils;
import com.canyinghao.canaccess.utils.Utils;
import com.canyinghao.canhelper.IntentHelper;
import com.canyinghao.canhelper.SPHepler;
import com.kenumir.materialsettings.MaterialSettingsActivity;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;

/**
 * Created by yangjian on 15/6/7.
 */
public class SetPwdActivity extends MaterialSettingsActivity {
    TextItem deleteItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setToolbarLayout(getText(R.string.setting).toString());
        setToolbar(R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.getInstance().finish(context);

            }
        }, null);


        addPwdSwitcher();


        addPwdEdit();


        addPwdDelete();


        addQuestion();

        for (int i = 0; i < 9; i++) {


            TextItem textItem = new TextItem(getFragment(), "");
            addItem(textItem);


        }


    }

    private void addPwdSwitcher() {
        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.pwd_manage).toString()));

        final CheckboxItem notify1 = new SwitcherItem(getFragment(), "pwd_switch").setTitle(getString(R.string.pwd_switch)).setSubtitle(getString(R.string.pwd_switch_a));
        addItem(notify1);


        int lock = SPHepler.getInstance().getInt("lock");
        if (lock == 1) {
            notify1.updateChecked(true);
        } else {
            notify1.updateChecked(false);
        }


        notify1.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem item, boolean isChecked) {
                if (isChecked) {
                    SPHepler.getInstance().setInt("lock", 1);
                } else {

                    SPHepler.getInstance().setInt("lock", 0);
                }
            }
        });

    }

    private void addPwdEdit() {
        addItem(new DividerItem(getFragment()));


        addItem(new TextItem(getFragment(), "pwd_edit").setTitle(getString(R.string.pwd_edit)).setSubtitle(getString(R.string.pwd_edit_a)).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {


                IntentHelper.getInstance().showIntent(context, PwdSetActivity.class, true);


            }
        }));
    }

    private void addPwdDelete() {
        addItem(new DividerItem(getFragment()));
        deleteItem = new TextItem(getFragment(), "pwd_delete").setTitle(getString(R.string.pwd_delete)).setSubtitle(getString(R.string.pwd_delete_a)).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                PatternLockUtils.clearPattern(context);
                SPHepler.getInstance().setInt("lock", 0);
                deleteItem.setVisibility(View.GONE);
                Utils.showSnackbar(toolbar, getString(R.string.pwd_toast), null);


            }
        });


        addItem(deleteItem);
    }

    private void addQuestion() {
        addItem(new DividerItem(getFragment()));


        addItem(new TextItem(getFragment(), "pwd_question").setTitle(getString(R.string.pwd_ques)).setSubtitle(getString(R.string.pwd_ques_a)).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                showEditDialog();


            }
        }));

    }


    public void showEditDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_ignore, null);
        final TextView title = (TextView) v.findViewById(R.id.title);
        final TextInputLayout textinput1 = (TextInputLayout) v.findViewById(R.id.textInput1);
        final TextInputLayout textinput2 = (TextInputLayout) v.findViewById(R.id.textInput2);
        title.setText(getString(R.string.pwd_ques));
        textinput1.setHint(getString(R.string.ques));

        textinput2.setHint(getString(R.string.answer));
        textinput1.setErrorEnabled(true);
        textinput2.setErrorEnabled(true);
        final EditText et1 = textinput1.getEditText();
        final EditText et2 = textinput2.getEditText();

        String ques = SPHepler.getInstance().getString("ques");
        String answer = SPHepler.getInstance().getString("answer");

        et1.setText(ques);
        et2.setText(answer);

        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && TextUtils.isEmpty(et1.getText().toString())) {
                    textinput1.setError(getString(R.string.ignore_empty));
                }
            }
        });
        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && TextUtils.isEmpty(et2.getText().toString())) {
                    textinput2.setError(getString(R.string.ignore_empty));
                }
            }
        });


        builder.setView(v);

        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String str1 = et1.getText().toString();
                String str2 = et2.getText().toString();
                if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2)) {

                    Utils.showSnackbar(toolbar, getString(R.string.ignore_empty), null);
                } else {

                    SPHepler.getInstance().setString("ques", str1);
                    SPHepler.getInstance().setString("answer", str2);


                }

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PatternLockUtils.hasPattern(context)) {
            deleteItem.setVisibility(View.VISIBLE);
        } else {
            deleteItem.setVisibility(View.GONE);
        }

    }
}
