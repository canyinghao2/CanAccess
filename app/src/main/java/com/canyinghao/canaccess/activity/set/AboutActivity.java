package com.canyinghao.canaccess.activity.set;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.BaseActivity;
import com.canyinghao.canhelper.DateHelper;
import com.canyinghao.canhelper.IntentHelper;
import com.canyinghao.canhelper.PhoneHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/11.
 */
public class AboutActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.toolbarLayout)
    CollapsingToolbarLayout toolbarLayout;
    @InjectView(R.id.tv_have)
    TextView tvHave;
    @InjectView(R.id.tv_version)
    TextView tvVersion;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        ButterKnife.inject(this);

        setToolbar(toolbar, R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.getInstance().finish(context);
            }
        }, null);

        toolbarLayout.setTitle(getString(R.string.about));

        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        toolbarLayout.setExpandedTitleColor(getResources().getColor(com.kenumir.materialsettings.R.color.ms_white));


        tvVersion.setText(getString(R.string.about_version)+ PhoneHelper.getInstance().getVersion());
        tvHave.setText(getString(R.string.about_have) +
                "2015"+ getString(R.string.sub)+DateHelper.getInstance().getCurrentYear());
    }
}
