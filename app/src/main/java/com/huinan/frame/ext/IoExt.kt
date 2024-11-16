package com.huinan.frame.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun <T> ioExecute(func: suspend () -> T):T = withContext(Dispatchers.Default) {
    func.invoke()
}