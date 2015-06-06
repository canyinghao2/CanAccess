package com.canyinghao.canaccess.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.canyinghao.canhelper.LogHelper;

/**
 * Created by yangjian on 15/6/6.
 */
public class RecyclerViewEmptySupport extends RecyclerView {
    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                    LogHelper.loge("Visibility");
                } else {
                    emptyView.setVisibility(View.GONE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);

                    LogHelper.loge("GONe");
                }
            }

        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {

        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }


    private LinearLayout getProgressView(ProgressBar bar,
                                         String str) {

        LinearLayout ll1 = new LinearLayout(getContext());
        ll1.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ll1.setGravity(Gravity.CENTER);
        ll1.setLayoutParams(params);
        if (bar == null) {
            bar = new ProgressBar(getContext());
        }

        TextView textView = new TextView(getContext());

        textView.setText(str);
        textView.setGravity(Gravity.CENTER);

        ll1.addView(bar);
        ll1.addView(textView);
        return ll1;

    }


    private LinearLayout getEmptyView(Integer rid, String str,
                                      OnClickListener click) {

        LinearLayout ll2 = new LinearLayout(getContext());
        ll2.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ll2.setGravity(Gravity.CENTER);
        ll2.setLayoutParams(params);
        if (click != null) {
            ll2.setOnClickListener(click);
        }

        if (rid != null) {

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(rid);
            ll2.addView(imageView);
        }
        TextView textView = new TextView(getContext());
        textView.setText(str);
        textView.setGravity(Gravity.CENTER);

        ll2.addView(textView);

        return ll2;

    }


    private void removeItem(ViewGroup parent,int index) {
        Object tag = getTag(index);
        if (tag != null && tag instanceof View) {
            View s = (View) tag;
            parent.removeView(s);
            setTag(index, null);
        }

    }

    public void setEmptyViewImage(
            Integer rid, String message, OnClickListener listener) {

        ViewGroup parentView = (ViewGroup) getParent();

        removeItem(parentView,-10321);
        removeItem(parentView,-10322);

        LinearLayout inflate = getEmptyView(rid, message, listener);

        parentView.addView(inflate);
        setTag(-10321, inflate);
        setEmptyView(inflate);

        if (getAdapter()!=null&&getAdapter().getItemCount()>0){

            inflate.setVisibility(View.GONE);
        }



    }

    public void setEmptyViewProgress(
            ProgressBar bar, String str) {
//        setVisibility(View.GONE);
        ViewGroup parentView = (ViewGroup) getParent();

        removeItem(parentView,-10321);
        removeItem(parentView,-10322);

        LinearLayout wait = getProgressView(bar, str);

        parentView.addView(wait);
        setTag(-10322, wait);
        setEmptyView(wait);

    }
}