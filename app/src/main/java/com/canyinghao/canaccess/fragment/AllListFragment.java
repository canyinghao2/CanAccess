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

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.adapter.ListAdapter;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.view.DividerItemDecoration;
import com.canyinghao.canaccess.view.RecyclerViewEmptySupport;
import com.canyinghao.canhelper.LogHelper;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllListFragment extends BaseFragment {
    public static final String TYPE = "type";
    public static int today = 0;
    public static int yesterday = 1;
    public static int other = 2;


    List<EventBean> list;
    private int type;

    @InjectView(R.id.recyclerview)
    RecyclerViewEmptySupport recyclerview;

    public static AllListFragment getInstance(int type) {


        AllListFragment fragment = new AllListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        fragment.setArguments(bundle);


        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view);


        type = getArguments().getInt(TYPE);

        recyclerview.setLayoutManager(new LinearLayoutManager(recyclerview.getContext()));
        recyclerview.setEmptyViewProgress(null, null);
        //设置Item增加、移除动画
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST);
        recyclerview.addItemDecoration(itemDecoration);

        observable();


        return view;
    }


    private void observable() {
        Observable<List<EventBean>> myObservable = Observable.create(
                new Observable.OnSubscribe<List<EventBean>>() {
                    @Override
                    public void call(Subscriber<? super List<EventBean>> sub) {


                        runList();

                        sub.onNext(list);

                        sub.onCompleted();
                    }


                }
        );

        AppObservable.bindFragment(this, myObservable).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<EventBean>>() {
            @Override
            public void onCompleted() {


            }

            @Override
            public void onError(Throwable e) {

                recyclerview.setEmptyViewImage(R.mipmap.icon_empty, null, null);

            }

            @Override
            public void onNext(List<EventBean> EventBeans) {


                recyclerview.setAdapter(new ListAdapter(context,
                        list));

                recyclerview.setEmptyViewImage(R.mipmap.icon_empty, null, null);


            }
        });
    }

    private void runList() {
        DbUtils dbUtils = App.getInstance().getDbUtils();

        try {
            long l1 = getTodayLong();
            long l2 = getYesterdayLong();

            LogHelper.loge(l1 + "getTodayLong()");
            LogHelper.loge(l2 + "getYesterdayLong()");

            switch (type) {

                case 0:


                    list = dbUtils.findAll(Selector.from(EventBean.class).where("eventTime", ">", l1).and("flag", "=", 0).orderBy("eventTime", true).limit(1000));

                    break;
                case 1:


                    list = dbUtils.findAll(Selector.from(EventBean.class).where("eventTime", ">", l2).and("eventTime", "<", l1).and("flag", "=", 0).orderBy("eventTime", true).limit(1000));
                    break;
                case 2:
                    list = dbUtils.findAll(Selector.from(EventBean.class).where("eventTime", "<", l2).and("flag", "=", 0).orderBy("eventTime", true).limit(1000));
                    break;

            }


            for (EventBean bean : list) {

                String pkg = bean.packageName;
                ApplicationInfo info = context.getPackageManager().getApplicationInfo(pkg, 0);
                bean.icon = info.loadIcon(context.getPackageManager());

            }


        } catch (DbException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private long getYesterdayLong() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    private long getTodayLong() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
