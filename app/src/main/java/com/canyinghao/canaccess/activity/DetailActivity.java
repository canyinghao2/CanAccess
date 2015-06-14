/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canyinghao.canaccess.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.canyinghao.canaccess.Constant;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.utils.Utils;
import com.canyinghao.canhelper.IntentHelper;
import com.kale.activityoptions.transition.TransitionCompat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailActivity extends BaseActivity {


    EventBean bean;
    @InjectView(R.id.backdrop)
    ImageView backdrop;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.tv_label)
    TextView tvLabel;
    @InjectView(R.id.tv_packgeName)
    TextView tvPackgeName;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_action)
    TextView tvAction;
    @InjectView(R.id.tv_className)
    TextView tvClassName;
    @InjectView(R.id.tv_text)
    TextView tvText;
    @InjectView(R.id.tv_datail)
    TextView tvDatail;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        bean = new EventBean();


        Intent intent = getIntent();
        if (intent.hasExtra(Constant.start)) {
            bean = intent.getParcelableExtra(Constant.start);
        }






        setToolbar(toolbar, R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();

            }
        }, null);


        collapsingToolbar.setTitle(bean.label);

        loadBackdrop();

        setDetail();

        Utils.startTransitionName(this,R.layout.activity_detail,backdrop);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.getInstance().showIntent(context, SearchActivity.class,new String[]{Constant.start},new String[]{bean.packageName},true);
            }
        });
    }


    private void loadBackdrop() {

        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(bean
                    .packageName);
            backdrop.setImageDrawable(icon);
            fab.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setDetail(){
        tvAction.setText(bean.eventTypeStr);
        tvClassName.setText(bean.className);
        tvDatail.setText(bean.datail);
        tvLabel.setText(bean.label);
        tvPackgeName.setText(bean.packageName);
        tvText.setText(bean.text);
        tvTime.setText(bean.eventTimeStr);





    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT>20){
            super.onBackPressed();
        }else{
            TransitionCompat.finishAfterTransition(this);
        }
    }
}
