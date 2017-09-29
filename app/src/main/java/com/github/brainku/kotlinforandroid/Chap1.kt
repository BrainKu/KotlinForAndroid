package com.github.brainku.kotlinforandroid

import java.util.*

/**
 * Created by brainku on 17/9/27.
 */

data class Artist(
        var id: Long,
        var name: String,
        var url: String,
        var mbid: String
)


public class Person() {
    var name: String = ""
        get() = field.toUpperCase().isEmpty().toString()
    set(value) {
        field = "Name: $value"
    }

    val age: Int
        get() = 2222
}

open class Animal(name: String)

data class Forecast(val date: Date, val temperature: Float, val details: String)