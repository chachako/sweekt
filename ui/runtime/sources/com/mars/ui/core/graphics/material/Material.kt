package com.mars.ui.core.graphics.material


/*
 * author: 凛
 * date: 2020/8/9 12:51 PM
 * github: https://github.com/oh-Rin
 * description: 一些显示的材质
 */
interface Material {
  /** 用于主题系统分辨此材质是否是主题中的材质，并决定是否可更新 */
  var id: Int

  /** 合并两个材质并生成新的材质 */
  fun merge(other: Material): Material

  /** 创建一个材质副本并传入给定的 Id 值 */
  fun new(id: Int): Material
}

/*
 * TODO 默认谷歌的材质设计
 * reference: https://material.io/
 * class GoogleMaterial {}
 */
