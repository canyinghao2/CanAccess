package com.canyinghao.canaccess;

import android.app.Application;
import android.content.Context;

import com.canyinghao.canhelper.CanHelper;
import com.canyinghao.canhelper.FileHelper;
import com.canyinghao.canhelper.LogHelper;
import com.github.anrwatchdog.ANRWatchDog;
import com.lidroid.xutils.DbUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import butterknife.ButterKnife;

public class App extends Application implements Thread.UncaughtExceptionHandler {

	private static App app;



    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;


	@Override
	public void onCreate() {



        super.onCreate();


		app = this;
        refWatcher = LeakCanary.install(this);

		ButterKnife.setDebug(BuildConfig.DEBUG);



		ANRWatchDog anrWatchDog = new ANRWatchDog(2000);
		anrWatchDog.start();
        CanHelper.init(this);


		File file = new File(FileHelper.getInstance().getExternalStorePath(),
				getText(R.string.can).toString());

		
		if (!file.exists()) {
			file.mkdirs();
		}
		Thread.setDefaultUncaughtExceptionHandler(this);

	}

	public static App getInstance() {
		if (app == null) {
			app = new App();
		}
		return app;

	}



	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		
		String eStr = getCrashReport(ex);
		LogHelper.loge(Constant.ERROR, eStr);
		try {
			File file = new File(FileHelper.getInstance()
					.getExternalStorePath(), getText(R.string.fail_text).toString());
			FileOutputStream f = new FileOutputStream(file);
			f.write(eStr.getBytes());
			f.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}

	private String getCrashReport(Throwable ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String expcetionStr = sw.toString();
		try {
			sw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.close();
		return expcetionStr;
	}






    private DbUtils dbUtils;

    public DbUtils getDbUtils() {
        if (null == dbUtils) {
            dbUtils = DbUtils.create(this,getText(R.string.can).toString());
        }
        return dbUtils;
    }






}
