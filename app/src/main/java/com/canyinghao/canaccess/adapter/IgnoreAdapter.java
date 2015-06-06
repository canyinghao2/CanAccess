package com.canyinghao.canaccess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canaccess.bean.IgnoreBean;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/6.
 */
public class IgnoreAdapter extends NewBaseAdapter {


    public IgnoreAdapter(Context context, List list) {
        super(context, list);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_app_item, parent, false);
        view.setBackgroundResource(background);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holde, final int position) {
        final ViewHolder holder= (ViewHolder) holde;

        final IgnoreBean bean = (IgnoreBean) list.get(position);
         holder.avatar.setVisibility(View.GONE);
        holder.cb.setVisibility(View.GONE);
        holder.title.setText(bean.getTitle());
        holder.text1.setText(bean.getText());








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
