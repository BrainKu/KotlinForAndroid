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


class Person(var name: String, val age: Int = 11) {
    operator fun plus(person: Person) :Person = Person(this.name + person.name, this.age)
}

open class Animal(name: String)

data class Forecast(val date: Date, val temperature: Float, val details: String)

class BasicGraphic(val name: String) {
    inner class Line(val x1: Int, val y1: Int) {
        fun draw() {
            println("draw line (x1, y1) : ($x1, $y1)")
        }
    }

    fun draw() {
        println("draw something: $name")
    }
}