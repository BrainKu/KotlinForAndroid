package com.github.brainku.kotlinforandroid

import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.brainku.kotlinforandroid.utils.toast
import kotlinx.android.synthetic.main.activity_chap1_recyclerview.*

/**
 * Created by brainku on 17/9/28.
 */

class RecyclerListActivity : AppCompatActivity() {

    val data = listOf<String>(
            "测试字体 ABCDEFGH 1234567890",
            "测试字体 ABCDEFGH 1234567890",
            "测试字体 ABCDEFGH 1234567890",
            "测试字体 ABCDEFGH 1234567890",
            "测试字体 ABCDEFGH 1234567890"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chap1_recyclerview)
        recyclerList.adapter = RecyclerListAdapter(data) { toast("CLICK") }
        recyclerList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.set(5, 5, 5, 5)
            }
        })
    }
}

class RecyclerListAdapter(private val items: List<String>, private val itemClickListener: (String) -> Unit) : RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent?.context), itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(position, items[position])
    }

    class ViewHolder(val view: TextView, val listener: (String) -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            view.textSize = 20f
        }
        fun bindView(pos: Int, text: String) {
            view.text = text
            view.setOnClickListener { listener(text) }
            view.setTypeface(null,
                    if (pos % 2 == 0)
                        Typeface.BOLD
                    else
                        Typeface.NORMAL)
        }
    }
}