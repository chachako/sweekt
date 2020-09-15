package com.mars.toolkit.other

import com.mars.toolkit.appClassLoader

/** 更安全的解析并返回 [Class] */
fun resolveClass(
  className: String,
  initializeClass: Boolean = false,
  classLoader: ClassLoader = appClassLoader
): Class<*> = when (className) {
  "java.lang.Void" -> Void.TYPE
  else -> try {
    Class.forName(className, initializeClass, classLoader)
  } catch (_: ClassNotFoundException) {
    // 试试另一种方式提高容错率，因为某些情况下可能会导致 inner class 无法找到
    try {
      val resolved = className.substringBeforeLast(".") + "\$" + className.substringAfterLast(".")
      Class.forName(resolved, initializeClass, classLoader)
    } catch (e: ClassNotFoundException) {
      e.printStackTrace()
      error(e.message ?: "Class $className 查找失败。")
    }
  }
}