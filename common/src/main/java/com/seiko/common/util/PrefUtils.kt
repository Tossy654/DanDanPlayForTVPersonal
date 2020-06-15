package com.seiko.common.util

import android.app.Application
import androidx.preference.PreferenceDataStore
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal fun Application.initPrefs() {
    MMKV.initialize(this)
}

fun createPreferenceDataStore(name: String): BasePreferenceDataStore {
    val mmkv = MMKV.mmkvWithID(name)
    return MmkvPreferenceDataStore(mmkv)
}

abstract class BasePreferenceDataStore : PreferenceDataStore() {
    abstract fun allKeys(): Array<String>?
    abstract fun remove(key: String?)
}

private class MmkvPreferenceDataStore(private val prefs: MMKV) : BasePreferenceDataStore() {
    override fun allKeys(): Array<String>? = prefs.allKeys()
    override fun remove(key: String?) = prefs.removeValueForKey(key)
    override fun putInt(key: String?, value: Int) { prefs.putInt(key, value) }
    override fun getInt(key: String?, defValue: Int): Int = prefs.getInt(key, defValue)
    override fun putBoolean(key: String?, value: Boolean) { prefs.putBoolean(key, value) }
    override fun getBoolean(key: String?, defValue: Boolean): Boolean = prefs.getBoolean(key, defValue)
    override fun putFloat(key: String?, value: Float) { prefs.putFloat(key, value) }
    override fun getFloat(key: String?, defValue: Float): Float = prefs.getFloat(key, defValue)
    override fun putLong(key: String?, value: Long) { prefs.putLong(key, value) }
    override fun getLong(key: String?, defValue: Long): Long = prefs.getLong(key, defValue)
    override fun putString(key: String?, value: String?) { prefs.putString(key, value) }
    override fun getString(key: String?, defValue: String?): String? = prefs.getString(key, defValue)
    override fun putStringSet(key: String?, values: MutableSet<String>?) { prefs.putStringSet(key, values) }
    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? = prefs.getStringSet(key, defValues)
}

fun PreferenceDataStore.int(key: String, defValue: Int = 0): ReadWriteProperty<Any, Int> {
    return object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int = getInt(key, defValue)
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) { putInt(key, value) }
    }
}

fun PreferenceDataStore.long(key: String, defValue: Long = 0): ReadWriteProperty<Any, Long> {
    return object : ReadWriteProperty<Any, Long> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Long = getLong(key, defValue)
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) { putLong(key, value) }
    }
}

fun PreferenceDataStore.float(key: String, defValue: Float = 0f): ReadWriteProperty<Any, Float> {
    return object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float = getFloat(key, defValue)
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) { putFloat(key, value) }
    }
}

fun PreferenceDataStore.boolean(key: String, defValue: Boolean = false): ReadWriteProperty<Any, Boolean> {
    return object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean = getBoolean(key, defValue)
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) { putBoolean(key, value) }
    }
}

fun PreferenceDataStore.string(key: String, defValue: String = ""): ReadWriteProperty<Any, String> {
    return object : ReadWriteProperty<Any, String> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String = getString(key, defValue)!!
        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) { putString(key, value) }
    }
}

fun PreferenceDataStore.stringSet(key: String, defValue: Set<String> = emptySet()): ReadWriteProperty<Any, Set<String>> {
    return object : ReadWriteProperty<Any, Set<String>> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Set<String> = getStringSet(key, defValue)!!
        override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>) { putStringSet(key, value) }
    }
}