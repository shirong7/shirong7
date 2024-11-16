package com.huinan.frame.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.Observable

/**
 * @author       Tyler
 * @createTime  2024/8/8 11:01
 * @describe
 */


fun <T> Observable<T>.obs(owner: LifecycleOwner, call:(data:T)->Unit){
    observe(owner, Observer {
        call.invoke(it)
    })
}

fun <T> Observable<T>.obsSticky(owner: LifecycleOwner, call:(data:T)->Unit){
    observeSticky(owner, Observer {
        call.invoke(it)
    })
}
fun <T> postLiveEventBus(key: String, value: T) {
    LiveEventBus.get<T>(key).post(value)
}