package com.canyinghao.canaccess.utils;

import android.content.Intent;

/**
 * Created by yangjian on 15/6/9.
 */


public class APIImpl {


    public static final String API_STATE="API_STATE";
    public static final String API_CLASS="API_CLASS";
    public static final String API_RESULT="API_RESULT";

    public static final int API_FAIL=1;
    public static final int API_COMPELETE=2;
    public static final int API_SUCCESS=3;






    public static Intent getClassIntent(int state,Class cla){

        Intent intent = new Intent();
        intent.putExtra(APIImpl.API_STATE, state);
        intent.putExtra(APIImpl.API_CLASS, cla);
        return intent;

    }

}

