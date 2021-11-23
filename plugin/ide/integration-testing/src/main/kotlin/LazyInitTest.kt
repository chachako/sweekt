package com.meowool.sweekt

class LazyInitTest {
    @LazyInit val a = "lazy init"

    fun main() {
        a.resetLazyValue()
    }
}

abstract class LeakingLazyInitTest {
    abstract val x: Int

    @LazyInit
    val f = x
}