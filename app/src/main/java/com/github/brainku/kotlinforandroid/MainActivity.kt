package com.github.brainku.kotlinforandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.github.brainku.kotlinforandroid.utils.logD
import com.github.brainku.kotlinforandroid.utils.toast
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<TextView>(R.id.tvTest)
        view.setOnClickListener {
            toast(tvTest.text)
            testRxForLoop()
        }
//        testPerson()
//        testCopy()
//        testOperatorOverload()
    }

    private fun testRxForLoop() {
        Observable.create<String> {
            for (i in 0..10) {
                it.onNext(i.toString())
                if (i == 4) {
                    return@create
                }
            }
            it.onComplete()
        }.subscribe({
            logD(info = "info" + it)
        }, { t -> logD(info = t.toString())}, {
            logD(info ="onComplete")
        })
    }

    private fun testPerson() {
        val p = Person("name", age = 11)
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


