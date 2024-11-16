package com.huinan.frame.ui.dialog.load

import android.os.Bundle
import android.view.View
import com.huinan.frame.base.BaseDialog
import com.huinan.frame.BR
import com.huinan.frame.R
import com.huinan.frame.databinding.ViewLoadingBinding

/**
 * @author       Tyler
 * @describe     加载dialog
 */
class LoadDialog: BaseDialog<LoadViewModel, ViewLoadingBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.view_loading
    }


    override fun isCancelableOnTouchOutside(): Boolean {
        return false
    }

    override fun initVariableId(): Int {
        return BR.VM
    }

    override fun initView(view: View) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initObserve() {

    }

    override fun cancelable(): Boolean {
        return false
    }

}