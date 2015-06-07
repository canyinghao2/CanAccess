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
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.bean.EventBean;

public class DetailActivity extends BaseActivity {

    public static final String EXTRA_NAME = "detail_name";
    EventBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bean = new EventBean();





        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NAME)) {
            bean = intent.getParcelableExtra(EXTRA_NAME);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ViewCompat.setTransitionName(findViewById(R.id.backdrop), EXTRA_NAME);

        setToolbar(toolbar,R.mipmap.ic_arrow_back_white,"","",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        },null);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(bean.getLabel());

        loadBackdrop();
    }



    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(bean
                    .getPackageName());
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }




    public static void launch(BaseActivity activity, View transitionView, EventBean bean) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_NAME);
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_NAME, bean);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
