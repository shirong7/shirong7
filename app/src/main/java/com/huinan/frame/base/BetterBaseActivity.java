package com.huinan.frame.base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.huinan.frame.BuildConfig;
import com.huinan.frame.R;


public abstract class BetterBaseActivity extends AppCompatActivity{

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar(this);
        onCreateView(savedInstanceState);
    }


    protected boolean handlerStatusBar(){
        return true;
    }

    protected abstract Fragment onCreateFragment();

    protected void onCreateView(@Nullable Bundle savedInstanceState){
        setContentView(R.layout.better_base_activity);
        if(savedInstanceState==null){
            Fragment fragment = onCreateFragment();
            if(fragment==null){
                return;
            }
            replaceFragment(fragment);
        }
    }




    protected void replaceFragment(Fragment fragment){
        replaceFragment(fragment, R.id.activity_container);
    }

    protected void replaceFragment(Fragment fragment , int containerId){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId,fragment);
        transaction.commitAllowingStateLoss();
    }

    public void setTransparentStatusBar(Activity activity) {
        // 获取DecorView
        View decorView = activity.getWindow().getDecorView();

        // 设置状态栏透明
        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(visibility);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_container);
        if(fragment!=null){
            mHandler.post(() -> fragment.onActivityResult(requestCode,resultCode,data));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if(BuildConfig.DEBUG){
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_container);
            String name = "activity";
            if(fragment!=null){
               name = fragment.getClass().getSimpleName();
            }else {
                name = getClass().getSimpleName();
            }
            Log.d("TAG", "onDestroy() called ${}"+name);
        }

    }




    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_container);
        if (fragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) fragment;
            if (baseFragment.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }


    public void closeFragment(){
        onBackPressed();
    }



}
