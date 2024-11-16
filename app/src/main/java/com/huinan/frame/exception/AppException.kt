package com.huinan.frame.exception
import android.text.TextUtils
import com.huinan.frame.BuildConfig
import com.huinan.frame.R
import com.huinan.frame.utils.ContextProvider
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

open class AppException : RuntimeException, IErrorCode {
    constructor(throwable: Throwable?) : super(throwable) {
        errorCode = if (throwable == null) {
            -1
        } else {
            when (throwable) {
                is ConnectException -> IErrorCode.STATE_ERROR_NO_NET
                is SocketTimeoutException -> IErrorCode.STATE_ERROR_TIME_OUT
                is CancellationException -> IErrorCode.STATE_ERROR_CANCEL
                is OutOfMemoryError -> IErrorCode.STATE_ERROR_OOM
                is AppException -> throwable.errorCode
                else -> -1
            }
        }

    }

    var errorCode = 0
        private set



    constructor(message: String?, errorCode: Int) : super(message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: Int) : super() {
        this.errorCode = errorCode
    }

    constructor(message: String?) : super(message) {
        errorCode = IErrorCode.STATE_ERROR_THROW_BY_USER
    }


    override val message: String?
        get() {
            var temp = super.message
            if(TextUtils.isEmpty(temp)){
//                temp = ContextProvider.getContext().resources.getString(R.string.better_error_serer)
                temp = "服务错误"
            }

            return when (errorCode) {
                IErrorCode.STATE_ERROR_NO_NET -> ContextProvider.getContext().getString(R.string.network_error)
                IErrorCode.STATE_EMPTY_DATA -> ContextProvider.getContext().getString(R.string.no_data_error)
                IErrorCode.STATE_ERROR_FAILED -> ContextProvider.getContext().getString(R.string.request_failed_error)
                IErrorCode.STATE_ERROR_TIME_OUT -> ContextProvider.getContext().getString(R.string.time_out_error)
                IErrorCode.STATE_ERROR_OOM -> ContextProvider.getContext().getString(R.string.oom_error)
                IErrorCode.STATE_ERROR_CANCEL -> ""
                IErrorCode.STATE_ERROR_THROW_BY_USER -> temp
                else -> {
                    var msg = if(BuildConfig.DEBUG) super.message else ""
                    if(TextUtils.isEmpty(msg)){
//                        msg = ContextProvider.getContext().resources.getString(R.string.better_error_serer)
                        msg= ContextProvider.getContext().getString(R.string.request_failed_error)
                    }
                    return msg
                }
            }
        }

}