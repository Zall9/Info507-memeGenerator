package com.example.myapplicationsqd
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MainActivity : AppCompatActivity() {
    var ImgUrls: ArrayList<String> = ArrayList()
    var recyclerView: RecyclerView? = null
    var Manager: LinearLayoutManager? = null
    var adapter: DataAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //creer_Json_local(ImgUrls);
        //creer_Json_extern(ImgUrls);
        ImgUrls.add("https://images.pexels.com/photos/2220401/pexels-photo-2220401.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=350")
        ImgUrls.add("https://images.pexels.com/photos/2765586/pexels-photo-2765586.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=350")
        ImgUrls.add("https://i.imgur.com/DulGEc5_d.jpg")
        ImgUrls.add("https://i.imgur.com/rXkffMg_d.jpg")

        recyclerView = findViewById<View>(R.id.card_recycler_view) as RecyclerView
        Manager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = Manager
        adapter = DataAdapter(this,ImgUrls)
        recyclerView!!.adapter = adapter
    }
}