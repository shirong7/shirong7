package com.huinan.frame


import android.app.Application
import com.didi.drouter.api.DRouter
import com.huinan.frame.utils.ContextProvider
import com.huinan.frame.utils.ScreenUtil
import com.huinan.frame.utils.ToastUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FrameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ScreenUtil.init(this)
        ContextProvider.init(this)
        ToastUtils.init(this)
        initSdk()

    }

    private fun initSdk() {
        MMKV.initialize(this)
        DRouter.init(this)
        LiveEventBus
            .config()
            .autoClear(true)
            .lifecycleObserverAlwaysActive(true)
    }

}