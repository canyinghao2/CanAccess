package com.canyinghao.canaccess.activity.set;

import android.os.Bundle;
import android.view.View;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);







        setToolbarLayout(getText(R.string.setting).toString());
        setToolbar(R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        }, null);


        addPwdSwitcher();




        addPwdEdit();

        if (PatternLockUtils.hasPattern(context)){
            addPwdDelete();


        }







    }

    private void addPwdSwitcher() {
        addItem(new HeaderItem(getFragment()).setTitle(getText(R.string.pwd_manage).toString()));

        final CheckboxItem notify1=   new SwitcherItem(getFragment(), "pwd_switch").setTitle(getText(R.string.pwd_switch).toString());
        addItem(notify1);

       int lock= SPHepler.getInstance().getInt("lock");
        if (lock==1){
            notify1.updateChecked(true);
        }else{
            notify1.updateChecked(false);
        }


        notify1.setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem item, boolean isChecked) {
                    if (isChecked){
                        SPHepler.getInstance().setInt("lock",1);
                    }else{

                        SPHepler.getInstance().setInt("lock",0);
                    }
            }
        });

    }

    private void addPwdEdit() {
        addItem(new DividerItem(getFragment()));



        addItem(new TextItem(getFragment(), "pwd_edit").setTitle(getText(R.string.pwd_edit).toString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {


                IntentHelper.getInstance().showIntent(context,PwdSetActivity.class);


            }
        }));
    }

    private void addPwdDelete() {
        addItem(new DividerItem(getFragment()));



        addItem(new TextItem(getFragment(), "pwd_delete").setTitle(getText(R.string.pwd_delete).toString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {

                PatternLockUtils.clearPattern(context);
                Utils.showSnackbar(toolbar,getString(R.string.pwd_toast),null);



            }
        }));
    }
}
