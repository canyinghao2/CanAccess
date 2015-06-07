package com.canyinghao.canaccess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holde, final int position) {
        final ViewHolder holder = (ViewHolder) holde;
        final EventBean bean = mValues.get(position);
        holder.title.setText(bean.getLabel());
        holder.text1.setText(bean.getEventTypeStr());
        String text=bean.getText();
        if (TextUtils.isEmpty(text)){
            text=bean.getClassName();
        }
        holder.text2.setText(text);
        holder.text3.setText(bean.getEventTimeStr());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 notifyItemMoved(position,position+1);
//                notifyItemRemoved(position);

                DetailActivity.launch((BaseActivity) context, holder.avatar, bean);
            }
        });

        holder.avatar.setImageDrawable(bean.getIcon());

    }


    @Override
    public int getItemCount() {
        if (mValues == null || mValues.isEmpty()) {
            return 0;
        }
        return mValues.size();
    }


}