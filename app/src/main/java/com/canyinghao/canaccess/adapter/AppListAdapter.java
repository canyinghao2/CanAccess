package com.canyinghao.canaccess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.Constant;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.SearchActivity;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canhelper.IntentHelper;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/6.
 */
public class AppListAdapter extends NewBaseAdapter {

    private int type;

    public AppListAdapter(Context context, List list,int type) {
        super(context, list);

        this.type=type;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_app, parent, false);
        view.setBackgroundResource(background);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holde, final int position) {
        final ViewHolder holder= (ViewHolder) holde;

        final AppBean bean = (AppBean) list.get(position);
         holder.avatar.setImageDrawable(bean.icon);
        holder.title.setText(bean.label);
        holder.text1.setText(bean.packageName);
        if (bean.type!=0){

            holder.cb.setChecked(true);

        }else{
            holder.cb.setChecked(false);



        }

        if (type==0){

            holder.cb.setVisibility(View.VISIBLE);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.cb.isChecked()){
                        holder.cb.setChecked(false);
                        bean.type=0;
                        deleteItem(bean);
                    }else{
                        holder.cb.setChecked(true);
                        bean.type=1;
                        saveItem(bean);
                    }




                }
            });





            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.cb.isChecked()){
                        bean.type=1;
                        saveItem(bean);
                    }else{
                        bean.type=0;
                        deleteItem(bean);
                    }
                }
            });

        }else{



            holder.cb.setVisibility(View.GONE);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    IntentHelper.getInstance().showIntent(context, SearchActivity.class,new String[]{Constant.start},new String[]{bean.packageName},true);




                }
            });

        }







    }

    private void deleteItem(AppBean bean) {
        try {
            App.getInstance().getDbUtils().delete(AppBean.class, WhereBuilder.b("packageName", "=", bean.packageName));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void saveItem(AppBean bean) {
        try {
          long count=  App.getInstance().getDbUtils().count(Selector.from(AppBean.class).where("packageName", "=", bean.packageName));

            if (count==0){
                App.getInstance().getDbUtils().saveBindingId(bean);
            }



        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppBean bean;
        @InjectView(R.id.avatar)
        ImageView avatar;
        @InjectView(android.R.id.title)
        TextView title;
        @InjectView(android.R.id.text1)
        TextView text1;
        @InjectView(R.id.cb)
        CheckBox cb;
        View view;
        public ViewHolder(View view) {
            super(view);
            this.view=view;
            ButterKnife.inject(this, view);


        }
    }
}
