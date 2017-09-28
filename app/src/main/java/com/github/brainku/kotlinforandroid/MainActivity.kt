package com.github.brainku.kotlinforandroid

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testSomething()
        val view = findViewById<TextView>(R.id.tvTest)
        view.setOnClickListener{
            toast(tvTest.text)
            toast(MainActivity::class.java.simpleName)
        }
        tvTest.text = addI(1, 3).toString()
    }

    private fun testSomething() {
        var notNullArtist: Artist? = null
        Log.d(TAG, "artist $notNullArtist")
        toast("artist $notNullArtist")
        Toast.makeText(this@MainActivity, "artist $notNullArtist", Toast.LENGTH_SHORT).show()
        val name = notNullArtist?.name ?: "empty"
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

val TAG: String = "MainActivity"


