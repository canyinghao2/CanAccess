package com.canyinghao.canaccess.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.R;
import com.canyinghao.canaccess.activity.set.AboutActivity;
import com.canyinghao.canaccess.activity.set.SetActivity;
import com.canyinghao.canaccess.bean.EventBean;
import com.canyinghao.canaccess.fragment.AllFragment;
import com.canyinghao.canaccess.fragment.BaseFragment;
import com.canyinghao.canaccess.fragment.NotifyFragment;
import com.canyinghao.canhelper.DateHelper;
import com.canyinghao.canhelper.IntentHelper;
import com.canyinghao.canhelper.SPHepler;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity {


    @InjectView(R.id.frame)
    FrameLayout frame;
    @InjectView(R.id.nav_view)
    NavigationView navView;
    @InjectView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;


    List<BaseFragment> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        addFragment();
        setupDrawerContent(navView);


        String date = DateHelper.getInstance().getDataString_2(new Date());

        SPHepler.getInstance().setString("date_answer", date);


        try {

            int set_trash1 = SPHepler.getInstance().getInt("set_trash1");

            if (set_trash1 == 0) {

                long day = getdayLong();
                int set_trash2 = SPHepler.getInstance().getInt("set_trash2");
                if (set_trash2 == 0) {

                    App.getInstance().getDbUtils().delete(EventBean.class, WhereBuilder.b("eventTime", "<", day));
                } else {
                    App.getInstance().getDbUtils().delete(EventBean.class, WhereBuilder.b("flag", "=", 1).and("eventTime", "<", day));
                }


            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    private void showFragment(int p) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < list.size(); i++) {

            BaseFragment fragment = list.get(i);

            transaction.hide(fragment);

        }

        transaction.show(list.get(p));

        transaction.commit();

    }


    private void replaceFragmet(int p) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        BaseFragment baseFragment = list.get(p);



        transaction.replace(R.id.frame, baseFragment);

        transaction.commit();


    }


    private void addFragment() {
        list = new ArrayList<>();

        list.add(NotifyFragment.getInstance(0));
        list.add(NotifyFragment.getInstance(2));
        list.add(AllFragment.getInstance(null));
        list.add(NotifyFragment.getInstance(1));

        replaceFragmet(0);


    }


    private void setupDrawerContent(NavigationView navigationView) {


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {


                            case R.id.rd_notify:
                                replaceFragmet(0);
                                break;

                            case R.id.rd_action:
                                replaceFragmet(1);
                                break;
                            case R.id.rd_all:

                                replaceFragmet(2);
                                break;

                            case R.id.rd_trash:
                                replaceFragmet(3);
                                break;

                            case R.id.set:


                                IntentHelper.getInstance().showIntent(context, SetActivity.class, true);


                                break;

                            case R.id.about:

                                IntentHelper.getInstance().showIntent(context, AboutActivity.class, true);
                                break;

                        }


                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }


    private long getdayLong() {

        int set_trash3 = SPHepler.getInstance().getInt("set_trash3");
        if (set_trash3 == 0) {
            set_trash3 = 7;
        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -set_trash3);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }


}
