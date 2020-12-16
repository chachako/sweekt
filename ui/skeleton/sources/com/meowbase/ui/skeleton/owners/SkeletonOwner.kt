package com.meowbase.ui.skeleton.owners

import com.meowbase.ui.skeleton.Skeleton

/**
 * 持有当前的 [Skeleton] 真实实例
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/11 - 11:25
 */
interface SkeletonOwner

internal val SkeletonOwner.current get() = this as Skeleton

internal val SkeletonOwner.stackManager get() = current.system.stackManager