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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.adapter.ListAdapter;
import com.canyinghao.canaccess.bean.EventBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class AllList1Fragment extends BaseFragment {

    List<EventBean> list;

    public static AllList1Fragment getInstance(Bundle bundle) {


        AllList1Fragment fragment = new AllList1Fragment();
        fragment.setArguments(bundle);


        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_cheese_list, container, false);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        DbUtils dbUtils = App.getInstance().getDbUtils();

        try {
            list = dbUtils.findAll(EventBean.class);
            rv.setAdapter(new ListAdapter(context,
                    list));
        } catch (DbException e) {
            e.printStackTrace();
        }


        return rv;
    }


}
