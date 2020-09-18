package com.mars.preference.kotpref.impl.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import android.os.SystemClock
import com.mars.preference.kotpref.KotprefModel
import com.mars.preference.core.execute
import com.mars.preference.core.get
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class StringSetPref(
  val default: Set<String>,
  val key: String?,
  val commitByDefault: Boolean,
  val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> Boolean)? = null,
  val putterImplProvider: ((thisRef: KotprefModel, value: Boolean, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : ReadOnlyProperty<KotprefModel, MutableSet<String>> {

  private var stringSet: MutableSet<String>? = null
  private var lastUpdate: Long = 0L

  override operator fun getValue(
    thisRef: KotprefModel,
    property: KProperty<*>
  ): MutableSet<String> = get(thisRef)

  fun get(thisRef: KotprefModel): MutableSet<String> {
    if (stringSet != null && lastUpdate >= thisRef.kotprefStartEditTime) {
      return stringSet!!
    }
    val prefSet = thisRef.kotprefPreference.getStringSet(key, null)
      ?.let { HashSet(it) }
    stringSet = PrefMutableSet(
      thisRef,
      prefSet ?: default.toMutableSet(),
      key!!
    )
    lastUpdate = SystemClock.uptimeMillis()
    return stringSet!!
  }

  internal inner class PrefMutableSet(
    val kotprefModel: KotprefModel,
    val set: MutableSet<String>,
    val key: String
  ) : MutableSet<String> by set {

    init {
      addAll(set)
    }

    private var transactionData: MutableSet<String>? = null
      get() {
        field = field ?: set.toMutableSet()
        return field
      }

    internal fun syncTransaction() {
      synchronized(this) {
        transactionData?.let {
          set.clear()
          set.addAll(it)
          transactionData = null
        }
      }
    }

    @SuppressLint("CommitPrefEdits")
    override fun add(element: String): Boolean {
      if (kotprefModel.kotprefInEditing) {
        val result = transactionData!!.add(element)
        kotprefModel.kotprefEditor!!.putStringSet(key, this)
        return result
      }
      val result = set.add(element)
      kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
      return result
    }

    @SuppressLint("CommitPrefEdits")
    override fun addAll(elements: Collection<String>): Boolean {
      if (kotprefModel.kotprefInEditing) {
        val result = transactionData!!.addAll(elements)
        kotprefModel.kotprefEditor!!.putStringSet(key, this)
        return result
      }
      val result = set.addAll(elements)
      kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
      return result
    }

    @SuppressLint("CommitPrefEdits")
    override fun remove(element: String): Boolean {
      if (kotprefModel.kotprefInEditing) {
        val result = transactionData!!.remove(element)
        kotprefModel.kotprefEditor!!.putStringSet(key, this)
        return result
      }
      val result = set.remove(element)
      kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
      return result
    }

    @SuppressLint("CommitPrefEdits")
    override fun removeAll(elements: Collection<String>): Boolean {
      if (kotprefModel.kotprefInEditing) {
        val result = transactionData!!.removeAll(elements)
        kotprefModel.kotprefEditor!!.putStringSet(key, this)
        return result
      }
      val result = set.removeAll(elements)
      kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
      return result
    }

    @SuppressLint("CommitPrefEdits")
    override fun retainAll(elements: Collection<String>): Boolean {
      if (kotprefModel.kotprefInEditing) {
        val result = transactionData!!.retainAll(elements)
        kotprefModel.kotprefEditor!!.putStringSet(key, this)
        return result
      }
      val result = set.retainAll(elements)
      kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
      return result
    }

    @SuppressLint("CommitPrefEdits")
    override fun clear() {
      if (kotprefModel.kotprefInEditing) {
        val result = transactionData!!.clear()
        kotprefModel.kotprefEditor!!.putStringSet(key, this)
        return result
      }
      set.clear()
      kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
    }

    override fun contains(element: String): Boolean {
      if (kotprefModel.kotprefInEditing) {
        return element in transactionData!!
      }
      return element in set
    }

    override fun containsAll(elements: Collection<String>): Boolean {
      if (kotprefModel.kotprefInEditing) {
        return transactionData!!.containsAll(elements)
      }
      return set.containsAll(elements)
    }

    override fun iterator(): MutableIterator<String> {
      return if (kotprefModel.kotprefInEditing) {
        kotprefModel.kotprefEditor!!.putStringSet(key, this@PrefMutableSet)
        KotprefMutableIterator(transactionData!!.iterator(), true)
      } else {
        KotprefMutableIterator(set.iterator(), false)
      }
    }

    override val size: Int
      get() {
        if (kotprefModel.kotprefInEditing) {
          return transactionData!!.size
        }
        return set.size
      }

    private inner class KotprefMutableIterator(
      val baseIterator: MutableIterator<String>,
      val inTransaction: Boolean
    ) : MutableIterator<String> by baseIterator {

      @SuppressLint("CommitPrefEdits")
      override fun remove() {
        baseIterator.remove()
        if (!inTransaction) {
          kotprefModel.kotprefPreference.edit().putStringSet(key, set).execute(commitByDefault)
        }
      }
    }
  }
}