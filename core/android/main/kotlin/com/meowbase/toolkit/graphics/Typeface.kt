package com.meowbase.toolkit.graphics

import android.content.res.AssetManager
import android.graphics.Typeface
import com.meowbase.toolkit.appContext
import java.io.File

typealias TypefaceName = String

/** 已经加载的 [Typeface] 实例 */
val loadedTypefaces by lazy { hashMapOf<TypefaceName, Typeface>() }

/** 加载多个字体文件到 [loadedTypefaces] 中 */
fun loadTypefaces(vararg typefaces: Pair<TypefaceName, File>) {
  typefaces.forEach {
    loadedTypefaces[it.first] = Typeface.createFromFile(it.second)
  }
}

/** 加载一个字体文件到 [loadedTypefaces] 中并返回字体 */
fun loadTypeface(name: TypefaceName, file: File): Typeface = Typeface.createFromFile(file).also {
  loadedTypefaces[name] = it
}

/** 根据 Assets 中的字体文件路径加载字体到 [loadedTypefaces] 中，并返回字体 */
fun loadTypeface(
  name: TypefaceName,
  path: String,
  assets: AssetManager = appContext.assets
): Typeface = Typeface.createFromAsset(assets, path).also {
  loadedTypefaces[name] = it
}