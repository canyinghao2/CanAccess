package com.canyinghao.canaccess.activity.set;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.BaseActivity;
import com.canyinghao.canaccess.adapter.AppListAdapter;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canaccess.view.ToolListView;
import com.canyinghao.canhelper.IntentHelper;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yangjian on 15/6/6.
 */
public class AppListActivity extends BaseActivity {
    ToolListView view;
    AppListAdapter adapter;
    private List<AppBean> list;

    boolean isAll;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new ToolListView(context);
        setContentView(view);

        Intent intent = getIntent();

        if (intent.hasExtra("all")) {
            flag = 1;
        }


        list = new ArrayList<>();
        adapter = new AppListAdapter(context, list,flag);
        view.recyclerView.setAdapter(adapter);
        setToolbar(view.toolbar, R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.getInstance().finish(context);
            }
        }, null);
        view.backdrop.setImageResource(R.drawable.bg2);
        view.floatButton.setImageResource(R.mipmap.ic_done_white);
        view.floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAll) {

                    selectNo();


                    isAll = false;

                } else {


                    selectAll();

                    isAll = true;
                }


            }
        });

        view.recyclerView.setLayoutManager(new LinearLayoutManager(view.recyclerView.getContext()));


        view.recyclerView.setEmptyViewProgress(null, null);
        observable();


    }

    private void selectNo() {


        Observable<List<AppBean>> myObservable = Observable.create(
                new Observable.OnSubscribe<List<AppBean>>() {
                    @Override
                    public void call(Subscriber<? super List<AppBean>> sub) {


                        try {
                            App.getInstance().getDbUtils().deleteAll(AppBean.class);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        for (AppBean bean : list) {
                            bean.type = 0;


                        }

                        sub.onNext(null);

                        sub.onCompleted();
                    }
                }
        );

        AppObservable.bindActivity(this, myObservable).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<AppBean>>() {
            @Override
            public void call(List<AppBean> appBeans) {
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void selectAll() {


        Observable<List<AppBean>> myObservable = Observable.create(
                new Observable.OnSubscribe<List<AppBean>>() {
                    @Override
                    public void call(Subscriber<? super List<AppBean>> sub) {


                        try {
                            App.getInstance().getDbUtils().saveAll(list);

                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        for (AppBean bean : list) {
                            bean.type = 1;


                        }

                        sub.onNext(null);

                        sub.onCompleted();
                    }
                }
        );

        AppObservable.bindActivity(this, myObservable).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<AppBean>>() {
            @Override
            public void call(List<AppBean> appBeans) {
                adapter.notifyDataSetChanged();
            }
        });


    }


    private void observable() {
        Observable<List<AppBean>> myObservable = Observable.create(
                new Observable.OnSubscribe<List<AppBean>>() {
                    @Override
                    public void call(Subscriber<? super List<AppBean>> sub) {


                        List<AppBean> list = getAppList();

                        sub.onNext(list);

                        sub.onCompleted();
                    }
                }
        );

        AppObservable.bindActivity(this, myObservable).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<AppBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.recyclerView.setEmptyViewImage(R.mipmap.ic_launcher, null, null);
            }

            @Override
            public void onNext(List<AppBean> appBeans) {


                list.addAll(appBeans);
                adapter.notifyDataSetChanged();

                view.recyclerView.setEmptyViewImage(R.mipmap.ic_launcher, null, null);

            }
        });
    }


    private List<AppBean> getAppList() {

        List<AppBean> apps = new ArrayList<>();


        PackageManager packMan = getPackageManager();


        inst:
        for (ApplicationInfo appInfo : packMan.getInstalledApplications(0)) {
            for (AppBean app : apps) {
                if (app.packageName.equals(appInfo.packageName)) {
                    continue inst;
                }
            }

//            (appInfo.flags != ApplicationInfo.FLAG_SYSTEM)
//                    &&
            if (appInfo.enabled) {
                Intent intent = packMan
                        .getLaunchIntentForPackage(appInfo.packageName);
                if (appInfo.icon != 0 & intent != null) {
                    AppBean app = new AppBean(appInfo.loadIcon(packMan), 0, String.valueOf(appInfo.loadLabel(packMan)), appInfo.packageName, flag);
                    apps.add(app);

                }


            }


        }


        try {
            List<AppBean> list = App.getInstance().getDbUtils().findAll(Selector.from(AppBean.class));


            if (list != null && !list.isEmpty()) {
                if (list.size() >= apps.size()) {
                    isAll = true;
                    for (AppBean bean1 : apps) {


                        bean1.type = 1;

                    }


                    return apps;

                }


                for (AppBean bean : list) {

                    for (AppBean bean1 : apps) {

                        if (bean.packageName.equals(bean1.packageName)) {
                            bean1.type = 1;
                            break;

                        }
                    }


                }

            }

        } catch (DbException e) {
            e.printStackTrace();
        }


        return apps;

    }
}
