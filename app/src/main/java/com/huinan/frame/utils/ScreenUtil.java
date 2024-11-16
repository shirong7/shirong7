package com.huinan.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ScreenUtil {
    public static int screenWidth;
    public static int screenHeight;
    public static int navigationHeight = 0;
    public static int statusBarHeight = 0;
    public static void init(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        statusBarHeight = getStatusBarHeight(context);
        navigationHeight = getNavigationBarHeight(context);
        //navigationHeight = getNavigationHeight(context);
    }


    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        try {
            if (Build.VERSION.SDK_INT < 30) {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                return resources.getDimensionPixelSize(x);
            } else {
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                return resources.getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 25;
    }

    /**
     * 获取底部导航栏高度
     *
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getNavigationHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int windowheight = wm.getDefaultDisplay().getHeight(); //获取无导航栏状态栏时窗口高度
        Log.d("无导航栏状态栏时窗口高度", String.valueOf(windowheight));
        int fullheigh = 0;   //窗口总高度
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class klass;
        try {
            klass = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = klass.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            fullheigh = dm.heightPixels;  //获取窗口总高度
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("有导航栏状态栏时窗口高度", String.valueOf(fullheigh));
        if (windowheight == fullheigh) return 0;   //无虚拟导航栏存在
        Log.d("栏状态栏高度", String.valueOf(getStatusBarHeight(context)));
        return fullheigh - windowheight - getStatusBarHeight(context);  //导航栏高度
    }






    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }


    public static void setTransparentStatusBar(Activity activity){
        //5.0及以上
        View decorView = activity.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(option);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }


    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }



    public static void setTransparentStatusBar(Window window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = window.getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    public static void setAndroidNativeLightStatusBar(Window window, boolean dark) {
        View decor = window.getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }





    /**
     * @param activity
     * @param useThemestatusBarColor   是否要状态栏的颜色，不设置则为透明色
     * @param withoutUseStatusBarColor 是否不需要使用状态栏为暗色调
     */
    public static void setStatusBar(Activity activity, boolean useThemestatusBarColor, boolean withoutUseStatusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            if (useThemestatusBarColor) {
                activity.getWindow().setStatusBarColor(Color.WHITE);
            } else {
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !withoutUseStatusBarColor) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //setStatusTextColor(!withoutUseStatusBarColor,activity);
    }



    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private static void processFlyMe(boolean isLightStatusBar, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        try {
            Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
            int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
            Field field = instance.getDeclaredField("meizuFlags");
            field.setAccessible(true);
            int origin = field.getInt(lp);
            if (isLightStatusBar) {
                field.set(lp, origin | value);
            } else {
                field.set(lp, (~value) & origin);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上  lightStatusBar为真时表示黑色字体
     */
    private static void processMIUI(boolean lightStatusBar, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags",int.class,int.class);
            extraFlagField.invoke(activity.getWindow(), lightStatusBar? darkModeFlag : 0, darkModeFlag);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }



    /**
     * 判断手机是否是小米
     * @return
     */
    // TODO: 2018/5/19 实现该方法 
    public static boolean isMIUI() {
//        try {
//            final BuildProperties prop = BuildProperties.newInstance();
//            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
//                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
//                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
//        } catch (final IOException e) {
//            return false;
//        }
        return false;
    }

    /**
     * 判断手机是否是魅族
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 设置状态栏文字色值为深色调
     * @param useDart 是否使用深色调
     * @param activity
     */
    public static void setStatusTextColor(boolean useDart, Activity activity) {
        if (isFlyme()) {
            processFlyMe(useDart, activity);
        } else if (isMIUI()) {
            processMIUI(useDart, activity);
        } else {
            if (useDart) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            //activity.getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, navigationHeight);
        }
    }


    public static int dp2px(float dpValue) {
        final float scale = ContextProvider.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dpToSp(float dp) {
        return (int) (dp2px(dp) / ContextProvider.getContext().getResources().getDisplayMetrics().scaledDensity);
    }
}
