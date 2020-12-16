package com.meowbase.preference.kotpref

/**
 * Batch editing properties in model
 * @param synchronous save to sharedPref file instantly
 */
inline fun <T : KotprefModel> T.batchEdit(synchronous: Boolean = false, block: T.() -> Unit) {
  beginBatchEdit()
  try {
    block()
  } catch (e: Exception) {
    cancelBatchEdit()
    throw e
  }
  if (synchronous) commitBatchEdit() else applyBatchEdit()
}