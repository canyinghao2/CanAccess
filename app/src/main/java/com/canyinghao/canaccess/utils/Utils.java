package com.canyinghao.canaccess.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;

import com.canyinghao.canaccess.Constant;
import com.canyinghao.canaccess.service.CanAccessibilityService;
import com.canyinghao.canhelper.LogHelper;
import com.kale.activityoptions.ActivityCompatICS;
import com.kale.activityoptions.ActivityOptionsCompatICS;
import com.kale.activityoptions.transition.TransitionCompat;

import java.lang.reflect.Field;

/**
 * Created by yangjian on 15/6/4.
 */
public class Utils {


    public static void unInstall(Context context) {

        CanAccessibilityService.INVOKE_TYPE = CanAccessibilityService.TYPE_UNINSTALL_APP;
        Uri packageURI = Uri.parse("package:com.canyinghao.canaccess");
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);

        uninstallIntent.setData(packageURI);
        context.startActivity(uninstallIntent);
    }


    public static void showSnackbar(View v, String message,String actionText, View.OnClickListener listener) {

        Snackbar.make(v, message, Snackbar.LENGTH_SHORT)
                .setAction(actionText, listener).setActionTextColor(Color.WHITE).show();

    }



    public static String setAllComponentsName(Object f, int value) {
        // 获取f对象对应类中的所有属性域
        Field[] fields = f.getClass().getDeclaredFields();
        String name = "";
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();


            try {


                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(f);


                if (!TextUtils.isEmpty(varName)&&varName.startsWith("TYPE")&&o instanceof Integer) {
                    int m = (int) o;
                    if (m == value) {

                        name = varName;
                        break;
                    }


                }

                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
                LogHelper.logi("获取到：" + varName + " = " + o);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        return name;
    }






    public static void startActivityScaleAnim(Activity activity,View view,Intent intent) {
        ActivityOptionsCompatICS options = ActivityOptionsCompatICS.makeScaleUpAnimation(view,
                0, 0, //拉伸开始的坐标
                view.getMeasuredWidth(), view.getMeasuredHeight());//初始的宽高


        ActivityCompatICS.startActivity(activity, intent, options.toBundle());
    }

    public static void startActivityThumbNailScaleAnim(Activity activity,View view,Intent intent) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        ActivityOptionsCompatICS options = ActivityOptionsCompatICS.makeThumbnailScaleUpAnimation(
                view, bitmap, 0, 0);
        // Request the activity be started, using the custom animation options.
        ActivityCompatICS.startActivity(activity, intent, options.toBundle());
//		  view.setDrawingCacheEnabled(false);
    }


    public static void startActivityScreenTransitAnim(Activity activity,View view,Intent intent,int id) {
//		isSceneAnim = true;
        ActivityOptionsCompatICS options = ActivityOptionsCompatICS.
                makeSceneTransitionAnimation(activity, view,id);
        ActivityCompatICS.startActivity(activity, intent, options.toBundle());
    }


    public static void startActivitySceneTransition(Activity activity, View view, Intent intent,String str) {

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, view, str);


        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }


    public static  void startSceneTransition(Activity activity,View view,Intent intent,int id,String str){

        if (Build.VERSION.SDK_INT>20){
            startActivitySceneTransition(activity, view, intent, str);
        }else{
//            startActivityScreenTransitAnim(activity, view, intent, id);


            startActivityThumbNailScaleAnim(activity, view, intent);

//            startActivityScaleAnim(activity, view, intent);

        }


    }


    public static void startTransitionName(Activity activity,int layoutId,View view){
        if (Build.VERSION.SDK_INT>20){
            ViewCompat.setTransitionName(view, Constant.start);
        }else{
            TransitionCompat.startTransition(activity, layoutId);

        }


    }








}
