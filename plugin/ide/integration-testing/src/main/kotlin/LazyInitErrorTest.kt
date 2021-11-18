package com.meowool.sweekt

class LazyInitErrorTest {
    @LazyInit val a = ""
    @LazyInit val b: String
        get() = 0

    @LazyInit lateinit var c: String

    fun main() {
        10.resetLazyValue()
        resetLazyValues(a, true, c)
    }
}