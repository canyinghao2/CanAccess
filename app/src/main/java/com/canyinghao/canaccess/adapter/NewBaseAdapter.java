package com.canyinghao.canaccess.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.canyinghao.canaccess.R;

import java.util.List;

/**
 * Created by yangjian on 15/6/6.
 */
public abstract class NewBaseAdapter extends RecyclerView.Adapter {
    private final TypedValue typedValue = new TypedValue();
    public int background;
    public List list;
    public Context context;


    public NewBaseAdapter(Context context, List list) {
        super();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        background = typedValue.resourceId;
        this.context=context;
        this.list=list;


    }





    @Override
    public int getItemCount() {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.size();
    }
}
