package com.huinan.frame.base
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huinan.frame.bean.RefreshConfig
import com.huinan.frame.exception.AppException
import com.huinan.frame.exception.IErrorCode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {
    private val _dialogLoading = MutableLiveData<Boolean>()
    private val _pageLoading = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String>()
    private val _finishRefresh: MutableLiveData<RefreshConfig> = MutableLiveData()
    private val _finishLoadMore: MutableLiveData<RefreshConfig> = MutableLiveData()
    private val _finish: MutableLiveData<Boolean> = MutableLiveData()
    val finish: MutableLiveData<Boolean> = _finish
    val dialogLoading: LiveData<Boolean> = _dialogLoading
    val pageLoading: LiveData<Boolean> = _pageLoading
    val toastMessage: LiveData<String> = _toastMessage
    val finishRefresh: MutableLiveData<RefreshConfig> = _finishRefresh
    val finishLoadMore: MutableLiveData<RefreshConfig> = _finishLoadMore
    val toastMessageRes = MutableLiveData<Int>()


    var onBackground: View.OnClickListener = View.OnClickListener {
        _finish.postValue(true)
    }

    //执行刷新
    fun finishRefresh(delayed: Int = 0, success:Boolean, noMoreData: Boolean){
        finishRefresh.postValue(RefreshConfig(delayed, success, noMoreData))
    }
    //执行加载更多
    fun finishLoadMore(delayed: Int = 0, success:Boolean, noMoreData: Boolean){
        finishLoadMore.postValue(RefreshConfig(delayed, success, noMoreData))
    }

    fun showLoading() {
        _dialogLoading.postValue(true)
    }

    fun hideLoading() {
        _dialogLoading.postValue(false)
    }


    fun toast(message: String? = "") {
        message?.let {
            _toastMessage.postValue(it)
        }
    }

    fun toastRes(messageRes:Int){
        toastMessageRes.postValue(messageRes)
    }

    fun postToastRes(messageRes: Int) {
        toastMessageRes.postValue(messageRes)
    }

    val handler = CoroutineExceptionHandler { _, ex ->
        toast(AppException(ex).message)
    }


    fun launch(
        context: CoroutineContext = handler,
        scope: CoroutineScope = viewModelScope,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return scope.launch(context, block = block)
    }

    suspend fun <T> call(
        loading: Boolean = false,
        toastType: ToastType = ToastType.ERROR,
        func: suspend () -> T
    ): Promise<T> {
        if (loading) {
            showLoading()
        }
        return try {
            val data = func()
            if (loading) {
                hideLoading()
            }
            Promise(data, null)
        } catch (e: Throwable) {
            e.printStackTrace()
            hideLoading()
            val exception = if (e is AppException) e else AppException(e)
            when (toastType) {
                ToastType.ALL, ToastType.ERROR -> {
                    toast(exception.message)
                }
                else -> {
                }
            }

            Promise(null, exception)

        }
    }



}




class Promise<T> (var data:T?, private val error: AppException? = null){
    fun <K> then(func:(data:T)->K) : Promise<K> {
        if(error!=null){
            return Promise(null,error)
        }
        return try {
            if(data!=null)  Promise(func(data!!),null) else Promise(null, AppException(IErrorCode.STATE_EMPTY_DATA))
        }catch (e:Throwable){
            Promise(null, AppException(e))
        }
    }


    fun catch(handlerError:((e: AppException) -> Unit)? = null){
        error?.let {
            handlerError?.invoke(it)
        }

    }


}


enum class ToastType{
    NONE,ERROR,ALL
}


