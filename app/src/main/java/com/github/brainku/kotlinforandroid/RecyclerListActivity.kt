package com.github.brainku.kotlinforandroid

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.brainku.kotlinforandroid.utils.debug
import com.github.brainku.kotlinforandroid.utils.logD
import com.github.brainku.kotlinforandroid.utils.supportAPI25
import com.github.brainku.kotlinforandroid.utils.toast
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

    val peopels = listOf<Person>(Person("name"),
            Person("name"),
            Person("name"),
            Person("name"),
            Person("name"),
            Person("name"),
            Person("name")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chap1_recyclerview)
        recyclerList.adapter = RecyclerListAdapter(peopels) { toast(it.name) }

        recyclerList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.set(10, 10, 0, 10)
            }
        })
        supportAPI25 {
            logD(info = "Hello API 25")
        }
        debug {
            logD(info = "I'm debug")
        }
    }
}

class RecyclerListAdapter(private val items: List<Person>, private val itemClickListener: (Person) -> Unit) : RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent?.context), itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder, {
            bindView(items[position])
            updateTextColor(Color.BLACK)
            updateTextSize(items[position].age)
        })
    }

    class ViewHolder(private val view: TextView, private val listener: (Person) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindView(person: Person) {
            view.text = person.name
            view.setOnClickListener {
//                Toast.makeText(App.instance(), "", Toast.LENGTH_SHORT).show();
                listener(person)
            }
        }

        fun updateTextColor(color: Int) {
            view.setTextColor(color)
        }

        fun updateTextSize(size: Int) {
            view.textSize = size.toFloat()
        }
    }
}
