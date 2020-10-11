package com.mars.ui.skeleton.owners

import com.mars.ui.skeleton.Skeleton

/**
 * 持有当前的 [Skeleton] 真实实例
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/11 - 11:25
 */
interface SkeletonOwner

internal val SkeletonOwner.current get() = this as Skeleton

internal val SkeletonOwner.stackManager get() = current.system.stackManager