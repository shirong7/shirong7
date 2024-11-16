package com.huinan.frame.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.huinan.frame.ext.getVmClazz
import com.huinan.frame.ui.dialog.load.LoadDialog
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope


/**
 * create time：2024/8/ 13:45
 *
 * @author Tyler
 * @version 1.0
 * description：
 */
abstract class BaseFragment<VM : BaseViewModel, V : ViewDataBinding>: Fragment(), CoroutineScope by MainScope(){

    protected lateinit var mDataBinding: V
     lateinit var mViewModel: VM

    private val mViewModelId by lazy {
        initVariableId()
    }

    private val mLoadingDialog : LoadDialog by lazy {
        LoadDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initData(savedInstanceState)
        initObserve()
        initLiveEventBus()
    }

    open fun initView(view: View){
        getRefreshLayout()?.let {
            it.setRefreshHeader(ClassicsHeader(requireContext()))
            it.setEnableRefresh(true)
            it.setOnRefreshListener(getOnRefreshListener())
            it.setRefreshFooter(ClassicsFooter(requireContext()))
            it.setEnableLoadMore(setEnableLoadMore())
            it.setEnableAutoLoadMore(setEnableLoadMore())
            it.setOnLoadMoreListener(getOnLoadMoreListener())

        }
    }


    /**
     * 观察者监听
     */
    open fun initObserve(){
        observe(mViewModel.finishRefresh){
            getRefreshLayout()?.finishRefresh(it.delayed, it.success, it.noMoreData)
        }
        observe(mViewModel.finishLoadMore){
            getRefreshLayout()?.finishLoadMore(it.delayed, it.success, it.noMoreData)
        }
        observe(mViewModel.finish){
            requireActivity().finish()
        }

    }

    abstract fun getLayoutId(): Int
    abstract fun initVariableId(): Int
    abstract fun initData(savedInstanceState: Bundle?)
    abstract fun initLiveEventBus()
    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    /**
     * 显示加载中弹窗
     */
     fun showProgress() {
        mLoadingDialog.show(childFragmentManager,"mLoadingDialog")
    }
    /**
     * 关闭加载中弹窗
     */
     fun dismissProgress() {
        if (mLoadingDialog.isAdded)
            mLoadingDialog.dismiss()
    }

    /**
     * 是否启动加载更多
     */
    open fun setEnableLoadMore(): Boolean = true
    /**
     * 刷新控件
     */
    open fun getRefreshLayout(): RefreshLayout? = null
    /**
     * 执行刷新操作
     */
    open fun getOnRefreshListener(): OnRefreshListener? = null

    /**
     * 执行加载更多操作
     */
    open fun getOnLoadMoreListener(): OnLoadMoreListener? = null

    /**
     *
     * @return 返回true表示自己处理返回事件
     */
    open fun onBackPressed(): Boolean {
        return false
    }


    fun <VM: BaseViewModel> observe(viewmodel:VM){
        observe(viewmodel.dialogLoading){
            if (it) {
                showProgress()
            } else {
                dismissProgress()
            }
        }
    }
    fun <T> observe(livedata: LiveData<T>, call:(data:T)->Unit){
        livedata.observe(viewLifecycleOwner) {
            call(it)
        }
    }



}