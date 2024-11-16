package com.huinan.frame.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.huinan.frame.ext.getVmClazz
import com.huinan.frame.ui.dialog.load.LoadDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@Suppress("DEPRECATION")
abstract class BaseActivity<VM : BaseViewModel, V : ViewDataBinding>: AppCompatActivity(), CoroutineScope by MainScope(){

    protected lateinit var mDataBinding: V
    lateinit var mViewModel:VM

    private val mViewModelId by lazy {
        initVariableId()
    }
    private val mLoadingDialog : LoadDialog by lazy {
        LoadDialog()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar(this)
        initContentView()
        initView()
        initObserve()
        initLiveEventBus()
        initData()
    }

    open fun initObserve(){
        observe(mViewModel.finish){
            finish()
        }
    }
    private fun setTransparentStatusBar(activity: Activity) {
        val decor = activity.window.decorView
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    private fun initContentView() {
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewModel = createViewModel()
        mDataBinding.setVariable(mViewModelId, mViewModel)
        mDataBinding.lifecycleOwner = this
        lifecycle.addObserver(mViewModel)
    }
    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return  ViewModelProvider(this)[(getVmClazz(this))]
    }
    abstract fun getLayoutId(): Int
    abstract fun initData()
    abstract fun initView()
    abstract fun initVariableId(): Int
    abstract fun initLiveEventBus()

    //loading监听（可选）
    fun <VM: BaseViewModel> observe(viewmodel:VM){
        viewmodel.dialogLoading.observe(this) {
            if (it) {
                showProgress()
            } else {
                dismissProgress()
            }
        }
    }

    //observe封装
    fun <T> observe(livedata: LiveData<T>, call:(data:T)->Unit){
        livedata.observe(this) {
            call(it)
        }
    }

     fun showProgress() {
        Log.d("TAG", "showProgress() called")
        if (!mLoadingDialog.isAdded)
            mLoadingDialog.show(this)
    }


     fun dismissProgress() {
        Log.d("TAG", "dismissProgress() called")
        if (mLoadingDialog.isAdded)
            mLoadingDialog.dismiss()
    }

}