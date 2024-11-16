package com.huinan.frame.okhttp

import com.huinan.frame.BuildConfig


object RequestPath {
    const val IP_API = "http://ip-api.com/json"
    fun BASE_API_URL(): String{
        return if(BuildConfig.DEBUG){
            "https://api-test.hivexreward.com/"
        }else{
            "https://api.playsfreely.com/"
        }
    }

}