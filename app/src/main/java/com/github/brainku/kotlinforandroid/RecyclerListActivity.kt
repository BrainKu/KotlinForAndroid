package com.github.brainku.kotlinforandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_chap1_recyclerview.*

/**
 * Created by brainku on 17/9/28.
 */

class RecyclerListActivity : AppCompatActivity() {

    val data = listOf<String>(
            "A - Test",
            "B - Test",
            "C - Test",
            "D - Test",
            "E - Test"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chap1_recyclerview)
        recyclerList.adapter = RecyclerListAdapter(data)
    }
}

class RecyclerListAdapter(private val items: List<String>) : RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent?.context))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        @SuppressLint("SetTextI18n")
        holder.view.text = "$position Info: " + items[position]
    }

    class ViewHolder(val view: TextView): RecyclerView.ViewHolder(view)
}