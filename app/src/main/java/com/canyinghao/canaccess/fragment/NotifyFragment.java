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
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.MainActivity;
import com.canyinghao.canaccess.adapter.ListAdapter;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.view.ToolListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotifyFragment extends BaseFragment {

    public static final String TYPE="type";

    @InjectView(R.id.toolListView)
    ToolListView toolListView;

    List<EventBean> list;
    int flag;

    public static NotifyFragment getInstance(int type) {


        NotifyFragment fragment = new NotifyFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(TYPE,type);
        fragment.setArguments(bundle);


        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_notify, container, false);


        ButterKnife.inject(this, v);
        toolListView.recyclerView.setLayoutManager(new LinearLayoutManager(toolListView.recyclerView.getContext()));

      Bundle bundle=  getArguments();
       flag= bundle.getInt(TYPE);






        initView();
        observable();

        return v;
    }

    private void initView() {


        setToolbar(toolListView.toolbar, R.mipmap.ic_menu_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).drawerLayout.openDrawer(GravityCompat.START);
            }
        }, null);


    }


    private void observable() {
        Observable<List<EventBean>> myObservable = Observable.create(
                new Observable.OnSubscribe<List<EventBean>>() {
                    @Override
                    public void call(Subscriber<? super List<EventBean>> sub) {


                        DbUtils dbUtils = App.getInstance().getDbUtils();

                        try {
                            list = dbUtils.findAll(Selector.from(EventBean.class).where("eventType", "=", AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED).and("text", "!=", "").and("flag", "=", flag).orderBy("eventTime", true).limit(1000));


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
                toolListView.recyclerView.setEmptyViewImage(R.mipmap.ic_launcher, null, null);
            }

            @Override
            public void onNext(List<EventBean> EventBeans) {


                toolListView.recyclerView.setAdapter(new ListAdapter(context,
                        list));

                toolListView.recyclerView.setEmptyViewImage(R.mipmap.ic_launcher, null, null);


            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
