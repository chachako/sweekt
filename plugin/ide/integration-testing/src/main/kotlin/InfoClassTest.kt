package com.meowool.sweekt

@Info(joinPrivateProperties = true)
class InfoClassTest {
    constructor(string: String)

    private val boolean: Boolean = false
    val abc: Int = 0

    fun main() {
        val boolean: Boolean = component1()
        val abc: Int = component2()
    }
}