package com.huinan.frame.utils;

import android.app.Application;
import android.util.DisplayMetrics;


public class ContextProvider {
    private static Application context;
    public static void init(Application context){
        ContextProvider.context = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public static int screenWidth;

    public static int screenHeight;


    public static Application getContext(){
        return context;
    }

}
