package com.huinan.frame.module.local


/**
 * @author Tyler
 * @version 1.0
 * description：mmkv存取
 */
object Preferences {
    internal var isFirst: Boolean by Preference("isFirst",true)
}