package com.canyinghao.canaccess.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.canyinghao.canaccess.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/6.
 */
public class ToolListView extends FrameLayout {
    @InjectView(R.id.backdrop)
  public   ImageView backdrop;
    @InjectView(R.id.toolbar)
    public  Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    public CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    public AppBarLayout appbar;
    @InjectView(R.id.recyclerView)
    public RecyclerViewEmptySupport recyclerView;
    @InjectView(R.id.main_content)
    public  CoordinatorLayout mainContent;

    @InjectView(R.id.floatButton)
    public FloatingActionButton floatButton;

    public ToolListView(Context context) {
        super(context);
        initView();
    }


    public ToolListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {

        View v = LayoutInflater.from(getContext()).inflate(
                R.layout.tool_list_view, this);
        ButterKnife.inject(this);



        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

    }




}
