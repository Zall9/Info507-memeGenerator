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

//Classe abstraite pour implémenter onItemClick
abstract class ListMemeAdapter(
    private val context: Context,
    private val Listmeme: HashMap<String, String>,
    val API_URL:String = "https://apimeme.com/meme?meme="

) :
    RecyclerView.Adapter<ListMemeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row_layout, parent, false)
        view.setOnClickListener { view -> onItemClick(view) } //listener sur chaque element du recyclerView
        return ViewHolder(view)
    }

    abstract fun onItemClick(view: View): Boolean

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        var list : ArrayList<String> = arrayListOf()
        var list2 : ArrayList<String> = arrayListOf()
        /*la variable i est la position du i-eme element dans le recyclerView
            Pour dessiner et afficher le texte correspondant,
            il faut construire deux tableaux contenant respectivement
            clés et valeurs car on ne peut itérer sur des hashmap
        */
        for ((key, value) in Listmeme){
         list.add(key)//contient les urls
         list2.add(value)//contient les titres des memes
        }
        Picasso.with(context).load(API_URL+list2[i]).resize(300, 300).into(holder.img_androidlist)
        //on affiche l'image présent a l'url dans list[i] dans notre viewholder
        //Log.d("TAG", API_URL+list2[i])
        holder.txtAndroid.text = list.get(i).toString()
        //on set le texte de list2[i] dans notre viewholder
    }

    override fun getItemCount(): Int {
        return Listmeme.size
    }
    //Cette classe interne permets de lier a l'initialisation d'un ListMemeAdapter
        // le TextView ainsi que l'ImageView présents dans list_row_layout
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtAndroid: TextView
        var img_androidlist: ImageView

        init {
            txtAndroid = view.findViewById<TextView>(R.id.txt_android)
            img_androidlist= view.findViewById<ImageView>(R.id.img_androidlist)
        }
    }
}