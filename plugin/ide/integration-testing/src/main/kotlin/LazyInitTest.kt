package com.meowool.sweekt

class LazyInitTest {
    @LazyInit val a = "lazy init"

    fun main() {
        a.resetLazyValue()
    }
}