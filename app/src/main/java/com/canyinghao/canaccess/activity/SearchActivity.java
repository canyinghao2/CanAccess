package com.canyinghao.canaccess.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.Constant;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.adapter.ListAdapter;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.view.DividerItemDecoration;
import com.canyinghao.canaccess.view.RecyclerViewEmptySupport;
import com.canyinghao.canhelper.IntentHelper;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangjian on 15/6/13.
 */
public class SearchActivity extends BaseActivity {

    @InjectView(R.id.toolBar)
    Toolbar toolBar;
    @InjectView(R.id.et)
    EditText et;
    @InjectView(R.id.textInput)
    TextInputLayout textInput;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;
    @InjectView(R.id.recyclerView)
    RecyclerViewEmptySupport recyclerView;
    @InjectView(R.id.mainContent)
    CoordinatorLayout mainContent;

    List<EventBean> list;
    ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search);

        ButterKnife.inject(this);


        setToolbar(toolBar, R.mipmap.ic_arrow_back_white, getString(R.string.search), "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        }, null);


        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));



        list=new ArrayList<>();
        adapter=  new ListAdapter(context,
                list);
        recyclerView.setAdapter(adapter);

        recyclerView.setEmptyViewImage(R.mipmap.icon_empty, null, null);
        textInput.setHint(getString(R.string.search_hint));
        textInput.setErrorEnabled(false);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i2, int i3) {

                if (TextUtils.isEmpty(cs.toString())){
                    list.clear();
                    adapter.notifyDataSetChanged();
                    recyclerView.setEmptyViewImage(R.mipmap.icon_empty, null, null);

                }else{

                    observable(cs.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra(Constant.start)) {


            String packName = intent.getStringExtra(Constant.start);

            et.setText(packName);
        }








    }






    private void observable(final String where) {
        recyclerView.setEmptyViewProgress(null, null);
        Observable<List<EventBean>> myObservable = Observable.create(
                new Observable.OnSubscribe<List<EventBean>>() {
                    @Override
                    public void call(Subscriber<? super List<EventBean>> sub) {
                        List<EventBean>    list=null;

                        DbUtils dbUtils = App.getInstance().getDbUtils();

                        try {
                      list = dbUtils.findAll(Selector.from(EventBean.class).where("packageName", "GLOB", "*" + where+"*").or("label", "GLOB", "*" + where+"*").or("eventTimeStr", "GLOB", "*" + where+"*").or("className", "GLOB", "*" + where+"*").or("text", "GLOB", "*" + where+"*").orderBy("eventTime", true).limit(1000));


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

        AppObservable.bindActivity(this, myObservable).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<EventBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                recyclerView.setEmptyViewImage(R.mipmap.icon_empty, null, null);
            }

            @Override
            public void onNext(List<EventBean> eventBeans) {


                list.clear();
                list.addAll(eventBeans);
                adapter.notifyDataSetChanged();


                recyclerView.setEmptyViewImage(R.mipmap.icon_empty, null, null);


            }
        });
    }

    @Override
    public void onBackPressed() {
        IntentHelper.getInstance().finish(this);
    }
}
