package com.example.myapplicationsqd

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class ListMemeAdapter(
    private val context: Context,
    private val Listmeme: ArrayList<String>
) :
    RecyclerView.Adapter<ListMemeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row_layout, parent, false)
        view.setOnClickListener { view -> onItemClick(view) }
        return ViewHolder(view)
    }

    abstract fun onItemClick(view: View): Boolean

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.txtAndroid.text = Listmeme.get(i).toString()

    }

    override fun getItemCount(): Int {
        return Listmeme.size
    }
    fun getId(i:Int):String{
        return Listmeme.get(i)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtAndroid: TextView

        init {
            txtAndroid = view.findViewById<TextView>(R.id.txt_android)
        }
    }

}