package com.example.myapplicationsqd
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.util.*
import java.io.File
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    lateinit var imageViews: ImageView
    lateinit var memeSelected: String
    var toptext: EditText?=null
    var bottomtext: EditText?=null
    var ImgUrls: ArrayList<String> = ArrayList()
    var Listmeme: HashMap<String,String> = hashMapOf()
    var recyclerViewListMeme: RecyclerView? = null
    var managerViewListMeme: LinearLayoutManager?=null
    var adapterViewListMeme: ListMemeAdapter?=null
    var recyclerView: RecyclerView? = null
    var bouttonGenerer: Button?=null
    var showMemeListButton: Button?=null
    var Manager: LinearLayoutManager? = null
    var adapter: DataAdapter? = null
    var msg: String? = ""
    var lastMsg = ""
    var requestQueue: RequestQueue? = null
    var list_row_layout: RelativeLayout?=null

    fun generateName() :String{
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..15)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
        println(randomString + ".jpg")
        return randomString + ".jpg"
    }
    @SuppressLint("Range")
    fun downloadImage(url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //Environment.getStorageDirectory()
        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    File.separator + "MemeCreator" + File.separator + generateName()
                    //url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }
    private fun statusMessage(url: String, directory: File, status: Int): String? {
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = Volley.newRequestQueue(this)

        val API_URL = "http://apimeme.com/meme?meme=%s&top=%s&bottom=%s"



        bouttonGenerer= findViewById<Button>(R.id.bouttonSent)
        showMemeListButton= findViewById<Button>(R.id.ButtonShowListOfMeme)
        toptext= findViewById<EditText>(R.id.textUp)
        bottomtext= findViewById<EditText>(R.id.textDown)

        var textHaut= toptext!!.text
        var textBas= bottomtext!!.text

        showMemeListButton!!.setOnClickListener {
            jsonParse()

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
                //val list : ArrayList<String> = Listmeme.values as ArrayList<String>
                val memeSelected=list.get(recyclerViewListMeme!!.getChildViewHolder(view).adapterPosition)
                Picasso.with(applicationContext).load(API_URL.format(memeSelected, "TOP", "BOTTOM")).resize(600, 600).into(imageViews)
                bouttonGenerer!!.setOnClickListener{
                    var listurl : ArrayList<String> = arrayListOf()
                    for ((key, value) in Listmeme){
                        listurl.add(key)
                    }
                    downloadImage(API_URL.format(memeSelected, textHaut, textBas))
                }


                Toast.makeText(applicationContext, "$memeSelected", Toast.LENGTH_LONG).show()
//                recyclerView = findViewById<View>(R.id.card_current_recycler_view) as RecyclerView
  //              Manager = LinearLayoutManager(applicationContext)
    //            recyclerView!!.layoutManager = Manager
      //          adapter = DataAdapter(applicationContext,ImgUrls)
        //        recyclerView!!.adapter = adapter
                recyclerViewListMeme!!.visibility= View.GONE
                return true
                                                        }
            }
        recyclerViewListMeme!!.adapter = adapterViewListMeme




        //RecyclerView qui affiche le meme selectionné

    }

    private fun jsonParse() {
        val url = "http://os-vps418.infomaniak.ch:1186/i507_1_2/listmeme.json"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val iter = response.keys()
                    while (iter.hasNext()) {
                        val key = iter.next()
                        try {
                            val value = response.get(key) as String
                            Listmeme[key] = value
                            Log.d("tablo", "$key")
                        } catch (e: JSONException){
                            e.printStackTrace()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }
}