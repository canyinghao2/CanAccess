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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllFragment extends BaseFragment {


    @InjectView(R.id.toolBar)
    Toolbar toolBar;
    @InjectView(R.id.tabs)
    TabLayout tabs;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.mainContent)
    CoordinatorLayout mainContent;


    public static AllFragment getInstance(Bundle bundle) {


        AllFragment fragment = new AllFragment();
        fragment.setArguments(bundle);


        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_all, container, false);
        ButterKnife.inject(this, v);





        initView();




        return v;
    }


    public void initView() {



        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });


        tabs.setupWithViewPager(viewPager);

        setToolbar(toolBar, R.mipmap.ic_menu_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).drawerLayout.openDrawer(GravityCompat.START);



            }
        }, null);
    }





    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());

       String[] strs= getResources().getStringArray(R.array.time_title);

        adapter.addFragment(AllListFragment.getInstance(0), strs[0]);
        adapter.addFragment(AllListFragment.getInstance(1), strs[1]);
        adapter.addFragment(AllListFragment.getInstance(2), strs[2]);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
