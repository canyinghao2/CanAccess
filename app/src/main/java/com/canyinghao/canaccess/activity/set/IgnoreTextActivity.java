package com.canyinghao.canaccess.activity.set;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.BaseActivity;
import com.canyinghao.canaccess.adapter.IgnoreAdapter;
import com.canyinghao.canaccess.bean.IgnoreBean;
import com.canyinghao.canaccess.view.ToolListView;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjian on 15/6/7.
 */
public class IgnoreTextActivity extends BaseActivity {
    ToolListView view;
    IgnoreAdapter adapter;
    private List<IgnoreBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new ToolListView(context);
        setContentView(view);

        list = new ArrayList<>();
        adapter = new IgnoreAdapter(context, list);
        view.recyclerView.setAdapter(adapter);
        setToolbar(view.toolbar, R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        }, null);

        view.floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);
          



            }
        });
        view.recyclerView.setLayoutManager(new LinearLayoutManager(view.recyclerView.getContext()));


        view.recyclerView.setEmptyViewProgress(null, null);

        try {
          List<IgnoreBean> list1= App.getInstance().getDbUtils().findAll(IgnoreBean.class);
            if (list1!=null&&!list1.isEmpty()){
                list.addAll(list1);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }



        adapter.notifyDataSetChanged();

        view.recyclerView.setEmptyViewImage(R.mipmap.ic_launcher, null, null);


    }
}
