package com.huinan.frame.module.local

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import kotlin.reflect.KProperty
/**
 * @author Tyler
 * @version 1.0
 * description：业务逻辑基础viewModel
 */
class Preference<T : Any>(val name:String,  val default:T) {

    val prefs: MMKV by lazy {
        val mmkv: MMKV = MMKV.mmkvWithID("Base_MMKV")
        mmkv
    }

    val gson: Gson by lazy {
        GsonBuilder().create()
    }



    inline operator fun <reified T:Any> getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (default) {
            is Long -> prefs.getLong(name, default) as T
            is String -> prefs.getString(name, default) as T
            is Int -> prefs.getInt(name, default) as T
            is Boolean -> prefs.getBoolean(name, default) as T
            is Float -> prefs.getFloat(name, default) as T
            else -> {
                Log.i("TAG", "getValue: "+prefs.contains(name))
                if (!prefs.contains(name)) {
                    default as T
                } else {
                    val json = prefs.getString(name, gson.toJson(default))
                    gson.fromJson(json!!) as T
                }
            }
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharedPreferences(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putSharedPreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, jsonToString(value))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T : Any> getSharedPreferences(name: String, default: T): T = with(prefs) {
        val res: Any? = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else ->  gson.fromJson(getString(name, jsonToString(default))!!)
        }

        return res as T
    }

    /**
     * 删除全部数据
     */
    fun clearPreference() {
        prefs.edit().clear().apply()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        prefs.edit().remove(key).apply()
    }


    fun <A> jsonToString(obj: A): String {
        return gson.toJson(obj)
    }


    inline fun <reified T : Any> parseJson(str: String): T {
        return gson.fromJson(str)
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    fun getAll(): Map<String, *> {
        return prefs.all
    }

    inline fun <reified T : Any> Gson.fromJson(json: String): T =
        fromJson(json, object : TypeToken<T>() {}.type)
}