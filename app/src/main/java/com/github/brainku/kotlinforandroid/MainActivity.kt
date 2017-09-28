package com.github.brainku.kotlinforandroid

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.github.brainku.kotlinforandroid.utils.launchActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<TextView>(R.id.tvTest)
        view.setOnClickListener{
            toast(tvTest.text)
            launchActivity(RecyclerListActivity::class)
        }
    }

    private fun testSomething() {
        var notNullArtist: Artist? = null
        Log.d(aTAG, "artist $notNullArtist")
        toast("artist $notNullArtist")
        Toast.makeText(this@MainActivity, "artist $notNullArtist", Toast.LENGTH_SHORT).show()
        val name = notNullArtist?.name ?: "empty"
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

val aTAG: String = "MainActivity"


