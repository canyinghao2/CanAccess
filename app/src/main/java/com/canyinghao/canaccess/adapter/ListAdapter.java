package com.canyinghao.canaccess.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.BaseActivity;
import com.canyinghao.canaccess.activity.DetailActivity;
import com.canyinghao.canaccess.bean.EventBean;

import java.util.List;

/**
 * Created by yangjian on 15/6/6.
 */
public class ListAdapter
        extends NewBaseAdapter {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<EventBean> mValues;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
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
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holde, int position) {
        final ViewHolder holder = (ViewHolder) holde;
        final EventBean bean = mValues.get(position);
        holder.mTextView.setText(bean.getLabel());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DetailActivity.launch((BaseActivity) context, holder.mImageView, bean);
            }
        });

        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(bean
                    .getPackageName());

            ((ViewHolder) holde).mImageView.setImageDrawable(icon);




        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



    }


    @Override
    public int getItemCount() {
        if (mValues == null || mValues.isEmpty()) {
            return 0;
        }
        return mValues.size();
    }


}