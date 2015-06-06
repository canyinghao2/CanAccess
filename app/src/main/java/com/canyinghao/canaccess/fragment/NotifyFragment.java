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

package com.canyinghao.canaccess.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.MainActivity;
import com.canyinghao.canaccess.adapter.NewBaseAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NotifyFragment extends BaseFragment {


    @InjectView(R.id.backdrop)
    ImageView backdrop;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;


    public NewBaseAdapter adapter;

    public static NotifyFragment getInstance(Bundle bundle) {


        NotifyFragment fragment = new NotifyFragment();
        fragment.setArguments(bundle);


        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.tool_list_view, container, false);
        ButterKnife.inject(this, v);
        initView();


        return v;
    }

    private void initView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }

        setToolbar(toolbar, R.mipmap.ic_menu_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).drawerLayout.openDrawer(GravityCompat.START);
            }
        }, null);


    }

    public void setFragmentAdapter(NewBaseAdapter adapter) {

        this.adapter = adapter;


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
