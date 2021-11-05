package com.example.myapplicationsqd
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.myapplicationsqd.adapter.DataAdapter
import com.example.myapplicationsqd.adapter.ListMemeAdapter
import com.example.myapplicationsqd.request.DownloadManager
import com.example.myapplicationsqd.request.RequestVolley
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.graphics.drawable.BitmapDrawable

import android.R.attr.path




class MainActivity : AppCompatActivity() {
    lateinit var imageViews: ImageView

    var toptext: EditText?=null
    var bottomtext: EditText?=null
    var Listmeme: HashMap<String,String> = hashMapOf()
    var recyclerViewListMeme: RecyclerView? = null
    var managerViewListMeme: LinearLayoutManager?=null
    var adapterViewListMeme: ListMemeAdapter?=null
    var bouttonTelecharger: Button?=null
    var bouttonGenerer: Button?=null
    var showMemeListButton: Button?=null
    var requestQueue: RequestQueue? = null
    var shareButton: ImageButton?=null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = Volley.newRequestQueue(this)

        val API_URL = "http://apimeme.com/meme?meme=%s&top=%s&bottom=%s"


        shareButton=findViewById<ImageButton>(R.id.sharebutton)
        bouttonGenerer= findViewById<Button>(R.id.bouttonGenerate)
        bouttonTelecharger=findViewById<Button>(R.id.bouttonSent)
        showMemeListButton= findViewById<Button>(R.id.ButtonShowListOfMeme)
        toptext= findViewById<EditText>(R.id.textUp)
        bottomtext= findViewById<EditText>(R.id.textDown)

        var textHaut= toptext!!.text
        var textBas= bottomtext!!.text



        showMemeListButton!!.setOnClickListener {
            RequestVolley.jsonParse(requestQueue!!, Listmeme)
            if (recyclerViewListMeme!!.visibility==View.VISIBLE){
                recyclerViewListMeme!!.visibility= View.GONE
            }
            else
                recyclerViewListMeme!!.visibility= View.VISIBLE
        }

        //Reycler View de la liste des memes a selectionner
        recyclerViewListMeme= findViewById<View>(R.id.listOfPictures) as RecyclerView
        managerViewListMeme= LinearLayoutManager(this)
        recyclerViewListMeme!!.layoutManager=managerViewListMeme
        imageViews= findViewById<ImageView>(R.id.card_current_image_view)
        adapterViewListMeme= object : ListMemeAdapter(applicationContext, Listmeme) {
            override fun onItemClick(view: View): Boolean {

                var list : ArrayList<String> = arrayListOf()
                for ((key, value) in Listmeme){
                    list.add(value)
                }

                val memeSelected=list.get(recyclerViewListMeme!!.getChildViewHolder(view).adapterPosition)
                Picasso.with(applicationContext).load(API_URL.format(memeSelected, "TOP", "BOTTOM")).resize(600, 600).into(imageViews)
                bouttonGenerer!!.setOnClickListener {
                    Picasso.with(applicationContext).load(API_URL.format(memeSelected, textHaut, textBas)).resize(550,600).into(imageViews)
                }
                bouttonTelecharger!!.setOnClickListener{
                    var listurl : ArrayList<String> = arrayListOf()
                    for ((key, value) in Listmeme){
                        listurl.add(key)
                    }

                    com.example.myapplicationsqd.request.DownloadManager.downloadImage(API_URL.format(memeSelected, textHaut, textBas),applicationContext,this@MainActivity)
                }
                Toast.makeText(applicationContext, "$memeSelected", Toast.LENGTH_LONG).show()
                shareButton!!.setOnClickListener{
                    val bitmap = (imageViews.getDrawable() as BitmapDrawable).bitmap
                    val intent=Intent()
                    intent.action=Intent.ACTION_SEND
                    val path= MediaStore.Images.Media.insertImage(contentResolver,bitmap,"title","null")
                    val uri=Uri.parse(path)
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.type="image/*"
                    startActivity(Intent.createChooser(intent,"Share to"))
                }
                recyclerViewListMeme!!.visibility= View.GONE

                return true
                                                        }
            }
        recyclerViewListMeme!!.adapter = adapterViewListMeme

    }




}