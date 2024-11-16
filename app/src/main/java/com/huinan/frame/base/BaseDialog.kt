package com.huinan.frame.base
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.huinan.frame.ext.getVmClazz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * create time：2024/8/2 14:06
 *
 * @author Tyler
 * @version 1.0
 * description：BaseDialog for data binding and ViewModel support.
 */
abstract class BaseDialog<VM : BaseViewModel, V : ViewDataBinding> : DialogFragment(), CoroutineScope by MainScope() {

     lateinit var mDataBinding: V
     lateinit var mViewModel: VM

    private val mViewModelId by lazy {
        initVariableId()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mViewModel = createViewModel()
        mDataBinding.setVariable(mViewModelId, mViewModel)
        mDataBinding.lifecycleOwner = this
        lifecycle.addObserver(mViewModel)
        return mDataBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            configureDialog(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initData(savedInstanceState)
        initObserve()
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    abstract fun getLayoutId(): Int
    abstract fun initVariableId(): Int
    abstract fun initView(view: View)
    abstract fun initData(savedInstanceState: Bundle?)
    abstract fun initObserve()

    /**
     * 配置Dialog的行为，如点击外部区域是否取消对话框等。
     * 子类可以重写此方法来定制Dialog的行为。
     */
    open fun configureDialog(dialog: Dialog) {
        dialog.setCanceledOnTouchOutside(isCancelableOnTouchOutside())
        dialog.setOnCancelListener(onCancelListener())
        dialog.setOnDismissListener(onDismissListener())
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                true
            } else false
        }
        dialog.window?.apply {
            isCancelable = cancelable()
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(layoutWidth(), layoutHeight())
            setGravity(layoutGravity())
            decorView.systemUiVisibility = systemUiVisibility()
        }
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, this::class.java.toString())
    }

    override fun onStop() {
        super.onStop()
        dialog?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.let {
            it.dismiss()
        }
    }


    /**
     * 是否在点击外部区域时取消对话框，默认为true。
     */
    open fun isCancelableOnTouchOutside(): Boolean = true

    /**
     * 是否允许返回键，默认为true。
     */
    open fun cancelable(): Boolean = true

    /**
     * 对话框取消时的监听器，默认为空。
     */
    open fun onCancelListener(): DialogInterface.OnCancelListener? = null

    /**
     * 对话框消失时的监听器，默认为空。
     */
    open fun onDismissListener(): DialogInterface.OnDismissListener? = null

    /**
     * 对话框的宽度，默认为匹配屏幕宽度。
     */
    open fun layoutWidth(): Int = ViewGroup.LayoutParams.MATCH_PARENT

    /**
     * 对话框的高度，默认为根据内容自动调整。
     */
    open fun layoutHeight(): Int = ViewGroup.LayoutParams.WRAP_CONTENT

    /**
     * 对话框位置，默认居中。
     */
    open fun layoutGravity(): Int = Gravity.CENTER;
    /**
     * 对话框的系统UI可见性，默认为显示所有UI组件。
     */
    open fun systemUiVisibility(): Int = View.SYSTEM_UI_FLAG_VISIBLE

}
