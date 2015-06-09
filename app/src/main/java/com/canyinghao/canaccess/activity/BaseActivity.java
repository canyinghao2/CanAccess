package com.canyinghao.canaccess.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.canyinghao.canaccess.R;


public class BaseActivity extends AppCompatActivity {
    public AppCompatActivity context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        context = this;




    }


    public void setToolbar(final Toolbar toolbar,int icon,String title,String subTitle,View.OnClickListener navigation,Toolbar.OnMenuItemClickListener menuItemClickListener){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(icon);
        toolbar.setTitle(title);
        toolbar.setSubtitle(subTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);


        toolbar.setNavigationOnClickListener(navigation);

        if (menuItemClickListener!=null){
            toolbar.setOnMenuItemClickListener(menuItemClickListener);

        }


    }






//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.sample_actions, menu);
//        return true;
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context=null;
    }




    public void onBackPressed(boolean flag){

        super.onBackPressed();
        overridePendingTransition(R.anim.activity_switch_push_left_in,
                R.anim.activity_switch_push_left_out);
    }


    public void  finish(boolean flag){
        overridePendingTransition(R.anim.activity_switch_push_left_in,
                R.anim.activity_switch_push_left_out);
    }
}
