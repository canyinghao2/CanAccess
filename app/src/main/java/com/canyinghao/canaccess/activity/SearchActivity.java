package com.canyinghao.canaccess.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.utils.Utils;
import com.canyinghao.canaccess.view.RecyclerViewEmptySupport;
import com.kale.activityoptions.transition.TransitionCompat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yangjian on 15/6/13.
 */
public class SearchActivity extends BaseActivity {

    @InjectView(R.id.toolBar)
    Toolbar toolBar;
    @InjectView(R.id.et)
    EditText et;
    @InjectView(R.id.textInput)
    TextInputLayout textInput;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;
    @InjectView(R.id.recyclerView)
    RecyclerViewEmptySupport recyclerView;
    @InjectView(R.id.mainContent)
    CoordinatorLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search);

        ButterKnife.inject(this);


        setToolbar(toolBar, R.mipmap.ic_arrow_back_white, "", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        }, null);





        Utils.startTransitionName(this, R.layout.activity_search, appBar);




    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT>20){
            super.onBackPressed();
        }else{
            TransitionCompat.finishAfterTransition(this);
        }
    }
}
