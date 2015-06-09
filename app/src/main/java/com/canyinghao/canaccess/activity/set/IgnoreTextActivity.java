package com.canyinghao.canaccess.activity.set;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.BaseActivity;
import com.canyinghao.canaccess.adapter.IgnoreAdapter;
import com.canyinghao.canaccess.bean.IgnoreBean;
import com.canyinghao.canaccess.utils.Utils;
import com.canyinghao.canaccess.view.ToolListView;
import com.canyinghao.canhelper.IntentHelper;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjian on 15/6/7.
 */
public class IgnoreTextActivity extends BaseActivity {
    ToolListView view;
    IgnoreAdapter adapter;
    private List<IgnoreBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new ToolListView(context);
        setContentView(view);

        list = new ArrayList<>();
        adapter = new IgnoreAdapter(context, list);
        view.recyclerView.setAdapter(adapter);
        setToolbar(view.toolbar, R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.getInstance().finish(context);
            }
        }, null);

        view.floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog(null);


            }
        });
        view.recyclerView.setLayoutManager(new LinearLayoutManager(view.recyclerView.getContext()));


        view.recyclerView.setEmptyViewProgress(null, null);

        adapterNotify();

        view.recyclerView.setEmptyViewImage(R.mipmap.ic_launcher, null, null);


    }

    private void adapterNotify() {
        try {
            List<IgnoreBean> list1 = App.getInstance().getDbUtils().findAll(IgnoreBean.class);
            if (list1 != null && !list1.isEmpty()) {
                list.clear();
                list.addAll(list1);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();
    }


    public void showEditDialog(IgnoreBean bean) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_ignore, null);
        final TextInputLayout textinput1 = (TextInputLayout) v.findViewById(R.id.textInput1);
        final TextInputLayout textinput2 = (TextInputLayout) v.findViewById(R.id.textInput2);
        textinput1.setHint(getString(R.string.ignore_hint1));

        textinput2.setHint(getString(R.string.ignore_hint2));
        textinput1.setErrorEnabled(true);
        textinput2.setErrorEnabled(true);
        final EditText et1 = textinput1.getEditText();
        final EditText et2 = textinput2.getEditText();

        if (bean != null) {
            et1.setText(bean.title);
            et2.setText(bean.text);
        }else{
            bean=new IgnoreBean();
        }
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
        final IgnoreBean finalBean = bean;
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String str1 = et1.getText().toString();
                String str2 = et2.getText().toString();
                if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2)) {

                    Utils.showSnackbar(view, getString(R.string.ignore_empty), null);
                } else {

                    finalBean.title=str1;
                    finalBean.text=str2;


                    try {
                        App.getInstance().getDbUtils().saveOrUpdate(finalBean);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }


                    adapterNotify();
                }

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }
}
