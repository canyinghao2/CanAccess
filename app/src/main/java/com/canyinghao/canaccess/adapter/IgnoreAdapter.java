package com.canyinghao.canaccess.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.set.IgnoreTextActivity;
import com.canyinghao.canaccess.bean.AppBean;
import com.canyinghao.canaccess.bean.IgnoreBean;
import com.canyinghao.canaccess.utils.Utils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/6.
 */
public class IgnoreAdapter extends NewBaseAdapter {


    private boolean isClick;

    public IgnoreAdapter(Context context, List list) {
        super(context, list);


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
        final ViewHolder holder = (ViewHolder) holde;

        final IgnoreBean bean = (IgnoreBean) list.get(position);
        holder.avatar.setVisibility(View.GONE);
        holder.cb.setVisibility(View.GONE);
        holder.title.setText(bean.title);
        holder.text1.setText(bean.text);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(context.getResources().getStringArray(R.array.ignore_array), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i) {


                            case 0:
                                list.remove(position);
                                notifyItemRemoved(position);
                                view.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isClick){

                                            try {
                                                App.getInstance().getDbUtils().delete(bean);
                                            } catch (DbException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        notifyDataSetChanged();
                                    }
                                }, 2000);



                                Utils.showSnackbar(holder. view,context.getString(R.string.delete_success),context.getString(R.string.cancel),new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        list.add(position,bean);
                                        notifyItemInserted(position);
                                        isClick=true;
                                        holder. view.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {



                                                notifyDataSetChanged();
                                            }
                                        },500);
                                    }
                                });


                                         break;
                                         case 1:

                                         IgnoreTextActivity activity = (IgnoreTextActivity) context;

                                         activity.showEditDialog(bean);

                                         break;
                                     }

                                 }
                        });


                builder.show();


            }
        });


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
            this.view = view;
            ButterKnife.inject(this, view);


        }
    }
}
