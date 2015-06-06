package com.canyinghao.canaccess.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.bean.AppBean;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/6.
 */
public class AppListAdapter extends NewBaseAdapter {


    public AppListAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(background);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holde, int position) {
        ViewHolder holder= (ViewHolder) holde;

        AppBean bean = (AppBean) list.get(position);

        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(bean
                    .getPackage());
            holder.avatar.setImageDrawable(icon);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        holder.text1.setText(bean.getLabel());
        holder.text2.setText(bean.getPackage());
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.avatar)
        ImageView avatar;
        @InjectView(android.R.id.text1)
        TextView text1;
        @InjectView(android.R.id.text2)
        TextView text2;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);


        }
    }
}
