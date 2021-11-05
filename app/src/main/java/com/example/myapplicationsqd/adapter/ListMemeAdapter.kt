package com.example.myapplicationsqd.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationsqd.R
import com.squareup.picasso.Picasso
import kotlin.collections.HashMap

abstract class ListMemeAdapter(
    private val context: Context,
    private val Listmeme: HashMap<String, String>,
    val API_URL:String = "https://apimeme.com/meme?meme="

) :
    RecyclerView.Adapter<ListMemeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row_layout, parent, false)
        view.setOnClickListener { view -> onItemClick(view) }
        return ViewHolder(view)
    }

    abstract fun onItemClick(view: View): Boolean

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        var list : ArrayList<String> = arrayListOf()
        var list2 : ArrayList<String> = arrayListOf()
        for ((key, value) in Listmeme){
         list.add(key)
         list2.add(value)
        }
        Picasso.with(context).load(API_URL+list2[i]).resize(300, 300).into(holder.img_androidlist)
        Log.d("TAG", API_URL+list2[i])
        holder.txtAndroid.text = list.get(i).toString()

    }

    override fun getItemCount(): Int {
        return Listmeme.size
    }
    fun getId(i:Int):String{
        var list : ArrayList<String> = arrayListOf()
        for ((key, value) in Listmeme){
            list.add(key)
        }
        return list.get(i)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtAndroid: TextView
        var img_androidlist: ImageView

        init {
            txtAndroid = view.findViewById<TextView>(R.id.txt_android)
            img_androidlist= view.findViewById<ImageView>(R.id.img_androidlist)
        }
    }

}