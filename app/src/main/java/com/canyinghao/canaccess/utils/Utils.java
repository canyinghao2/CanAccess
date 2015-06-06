package com.canyinghao.canaccess.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.canyinghao.canaccess.App;
import com.canyinghao.canaccess.service.CanAccessibilityService;
import com.canyinghao.canhelper.LogHelper;

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


    public static void showSnackbar(View v, String message, View.OnClickListener listener) {

        Snackbar.make(v, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", listener).show();
    }


    public static String setAllComponentsName(Object f, int value) {
        // ��ȡf�����Ӧ���е�����������
        Field[] fields = f.getClass().getDeclaredFields();
        String name = "";
        for (int i = 0, len = fields.length; i < len; i++) {
            // ����ÿ�����ԣ���ȡ������
            String varName = fields[i].getName();


            try {


                // ��ȡԭ���ķ��ʿ���Ȩ��
                boolean accessFlag = fields[i].isAccessible();
                // �޸ķ��ʿ���Ȩ��
                fields[i].setAccessible(true);
                // ��ȡ�ڶ���f������fields[i]��Ӧ�Ķ����еı���
                Object o = fields[i].get(f);


                if (!TextUtils.isEmpty(varName)&&varName.startsWith("TYPE")&&o instanceof Integer) {
                    int m = (int) o;
                    if (m == value) {

                        name = varName;
                        break;
                    }


                }

                // �ָ����ʿ���Ȩ��
                fields[i].setAccessible(accessFlag);
                LogHelper.logi("��ȡ����" + varName + " = " + o);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        return name;
    }




    public static int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = App.getInstance().getResources().getDimensionPixelSize(x);
            return sbar;

        } catch (Exception e1) {

            e1.printStackTrace();

        }
        return sbar;

    }


}
