package com.canyinghao.canaccess.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.Constant;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.DetailActivity;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.utils.Utils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/6.
 */
public class ListAdapter
        extends NewBaseAdapter {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<EventBean> mValues;

    private boolean isClick;
    public static class ViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.avatar)
        ImageView avatar;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text1)
        TextView text1;
        @InjectView(R.id.text2)
        TextView text2;
        @InjectView(R.id.text3)
        TextView text3;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.inject(this, view);


        }


    }

    public EventBean getValueAt(int position) {
        return mValues.get(position);
    }

    public ListAdapter(Context context, List items) {
        super(context, items);
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holde, final int position) {
        final ViewHolder holder = (ViewHolder) holde;
        final EventBean bean = mValues.get(position);
        holder.title.setText(bean.label);
        holder.text1.setText(bean.eventTypeStr);
        String text=bean.text;
        if (TextUtils.isEmpty(text)){
            text=bean.className;
        }
        holder.text2.setText(text);

        holder.text3.setText(bean.eventTimeStr);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent=new Intent(context,DetailActivity.class);

                intent.putExtra(Constant.start,bean);
                Utils.startSceneTransition((Activity)context,holder.avatar,intent, R.id.backdrop,Constant.start);



            }
        });

        holder.avatar.setImageDrawable(bean.icon);

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle(R.string.is_delete);
                builder.setMessage(R.string.this_delete);
                builder.setPositiveButton(R.string.sure,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.remove(position);
                        notifyItemRemoved(position);
                        holder. view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isClick){
                                    bean.flag=1;
                                    try {
                                        App.getInstance().getDbUtils().saveOrUpdate(bean);
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }


                                }
                                notifyDataSetChanged();
                            }
                        },2000);

                        Utils.showSnackbar(holder. view,context.getString(R.string.delete_success),context.getString(R.string.cancel),new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                isClick=true;
                                list.add(position,bean);
                                notifyItemInserted(position);
                                holder. view.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                    }
                                },500);
                            }
                        });
                    }
                });

                builder.setNegativeButton(R.string.cancel,null);

                builder.show();


                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        if (mValues == null || mValues.isEmpty()) {
            return 0;
        }
        return mValues.size();
    }


}