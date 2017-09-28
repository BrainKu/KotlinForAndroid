package com.github.brainku.kotlinforandroid

/**
 * Created by brainku on 17/9/27.
 */

data class Artist(
        var id: Long,
        var name: String,
        var url: String,
        var mbid: String
)

class Person(name: String, age: Int): Animal(name)

open class Animal(name: String)

fun add(x: Int, y: Int): Int {
    return x + y
}

fun addI(x: Int, y: Int) = x + y
