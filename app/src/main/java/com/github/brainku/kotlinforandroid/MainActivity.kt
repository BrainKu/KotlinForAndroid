package com.github.brainku.kotlinforandroid

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.github.brainku.kotlinforandroid.utils.launchActivity
import com.github.brainku.kotlinforandroid.utils.logD
import com.github.brainku.kotlinforandroid.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<TextView>(R.id.tvTest)
        view.setOnClickListener {
            toast(tvTest.text)
            launchActivity(SimpleLandActivity::class)
        }
        testPerson()
        testCopy()
        testOperatorOverload()
        tvTest.text = getScaledDensity().toString()
    }

    private fun getScaledDensity(): Float {
        return resources.displayMetrics.scaledDensity
    }

    private fun testPerson() {
        val p = Person("name", 11)
        p.logD(info = "person age: ${p.age}, name: ${p.name}")
        val map = mapOf<String, String>(Pair("A", "B"), Pair("B", "C"))
        for ((key, value) in map) {
            map.logD(info = "key:$key, value:$value")
        }
    }

    private fun testCopy() {
        val f1 = Forecast(Date(), 11.2f, "help!")
        val f2 = f1.copy(temperature = 11.4f)
        f2.logD(info = f2.toString())
    }

    private fun testOperatorOverload() {
        val person = Person(name = "Hello", age = 1)
        val person2 = Person(name = " world", age = 22);
        logD(tag = "Operator", info = (person + person2).toString())
    }

    private fun testSomething() {
        var notNullArtist: Artist? = null
        Log.d(aTAG, "artist $notNullArtist")
        toast("artist $notNullArtist")
        Toast.makeText(this@MainActivity, "artist $notNullArtist", Toast.LENGTH_SHORT).show()
        val name = notNullArtist?.name ?: "empty"
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val APP_ID = ""
        private val URL = ""
        private val COMPLETE_URL = "$URL&APPID=$APP_ID&q="
    }
}

val aTAG: String = "MainActivity"


