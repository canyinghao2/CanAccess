package com.canyinghao.canaccess.fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.BaseActivity;

/**
 * Created by yangjian on 15/6/4.
 */
public class BaseFragment extends Fragment {

    public BaseActivity context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        context = (BaseActivity)getActivity();




    }


    public void setToolbar(final Toolbar toolbar,int icon,String title,String subTitle,View.OnClickListener navigation,Toolbar.OnMenuItemClickListener menuItemClickListener){
        context.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(icon);
        toolbar.setTitle(title);
        toolbar.setSubtitle(subTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);


        toolbar.setNavigationOnClickListener(navigation);

        if (menuItemClickListener!=null){
            toolbar.setOnMenuItemClickListener(menuItemClickListener);

        }else{

            toolbar.setOnMenuItemClickListener( new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.action_settings:




                            break;
                    }


                    return true;
                }
            });

        }


    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//
//        super.onCreateOptionsMenu(menu, inflater);
//
//        inflater. inflate(R.menu.menu_base, menu);
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
