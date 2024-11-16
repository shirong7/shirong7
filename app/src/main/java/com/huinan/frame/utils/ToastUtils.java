package com.huinan.frame.utils;

import android.content.Context;
import android.text.TextUtils;



public class ToastUtils {
    private static BetterToast toast;

    public static void init(Context context) {
        toast = new BetterToast((context));
    }


    public static void show(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        toast.show(msg);
    }
    public static void show(int msg) {
        toast.show(msg);
    }


//    public static void show(){
//        Toast toast = Toast.makeText(app,"",100);
//        toast.setView();
//    }

    public static void showSuccess(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        toast.show(text);
//        Toast toast = new Toast(app);
//        View view = LayoutInflater.from(app).inflate(R.layout.better_view_toast, null);
//        toast.setView(view);
//        TextView textView = view.findViewById(R.id.tv_text);
//        textView.setText(text);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.show();
    }

    public static void showSuccess(int textRes) {
//        Toast toast = new Toast(app);
//        View view = LayoutInflater.from(app).inflate(R.layout.better_view_toast, null);
//        toast.setView(view);
//        TextView textView = view.findViewById(R.id.tv_text);
//        textView.setText(textRes);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.show();
        toast.show(textRes);
    }

    public static void showWarning(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
//        Toast toast = new Toast(app);
//        View view = LayoutInflater.from(app).inflate(R.layout.better_view_toast, null);
//        toast.setView(view);
//        TextView textView = view.findViewById(R.id.tv_text);
//        textView.setText(text);
//        ImageView imageView = view.findViewById(R.id.icon);
//        imageView.setImageResource(R.drawable.icon_warning);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.show();
        toast.show(text);
    }

    public static void showWarning(int textRes) {
//        Toast toast = new Toast(app);
//        View view = LayoutInflater.from(app).inflate(R.layout.better_view_toast, null);
//        toast.setView(view);
//        TextView textView = view.findViewById(R.id.tv_text);
//        textView.setText(textRes);
//        ImageView imageView = view.findViewById(R.id.icon);
//        imageView.setImageResource(R.drawable.icon_warning);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.show();
        toast.show(textRes);
    }
}
