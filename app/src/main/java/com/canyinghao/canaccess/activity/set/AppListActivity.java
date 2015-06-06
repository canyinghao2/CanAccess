package com.canyinghao.canaccess.activity.set;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.BaseActivity;
import com.canyinghao.canaccess.adapter.AppListAdapter;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canaccess.view.ToolListView;
import com.canyinghao.canhelper.PhoneHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangjian on 15/6/6.
 */
public class AppListActivity extends BaseActivity {
    ToolListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view= new ToolListView(context);
        setContentView(view);


        setToolbar(view.toolbar, R.mipmap.ic_arrow_back_white,"","",null,null);

       view. recyclerView.setLayoutManager(new LinearLayoutManager(view.recyclerView.getContext()));


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

            }

            @Override
            public void onNext(List<AppBean> appBeans) {

                AppListAdapter adapter = new AppListAdapter(context, appBeans);

                PhoneHelper.getInstance().show(appBeans.size()+"  this is list");

                view.recyclerView.setAdapter(adapter);


            }
        });


    }


    private List<AppBean> getAppList() {

        List<AppBean> apps = new ArrayList<>();


        PackageManager packMan = getPackageManager();


        inst:
        for (ApplicationInfo appInfo : packMan.getInstalledApplications(0)) {
            for (AppBean app : apps) {
                if (app.getPackage().equals(appInfo.packageName)) {
                    continue inst;
                }
            }

            if ((appInfo.flags != ApplicationInfo.FLAG_SYSTEM)
                    && appInfo.enabled) {
                Intent intent = packMan
                        .getLaunchIntentForPackage(appInfo.packageName);
                if (appInfo.icon != 0 & intent != null) {
                    AppBean app = new AppBean(appInfo.packageName, String.valueOf(appInfo.loadLabel(packMan)), true);
                    apps.add(app);

                }
            }


        }
        return apps;

    }
}
