@file:Suppress("PackageDirectoryMismatch")

/*
 * author: 凛
 * date: 2020/9/6 下午12:18
 * github: https://github.com/oh-Rin
 * description: 标记 Mars 内部使用的 api，因为这些特定的 api 只有内部才能使用，不应该公开调用
 */
@RequiresOptIn(message = "这是 Mars 内部项目使用的 Api, 此 Api 并不是为了公用而设计的，但也可能只是暂时未公开。")
annotation class InternalMarsProjectApi
