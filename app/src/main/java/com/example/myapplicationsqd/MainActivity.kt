package com.example.myapplicationsqd
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.myapplicationsqd.adapter.DataAdapter
import com.example.myapplicationsqd.adapter.ListMemeAdapter
import com.example.myapplicationsqd.request.DownloadManager
import com.example.myapplicationsqd.request.RequestVolley
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    lateinit var imageViews: ImageView

    var toptext: EditText?=null
    var bottomtext: EditText?=null
    var Listmeme: HashMap<String,String> = hashMapOf()
    var recyclerViewListMeme: RecyclerView? = null
    var managerViewListMeme: LinearLayoutManager?=null
    var adapterViewListMeme: ListMemeAdapter?=null
    var bouttonGenerer: Button?=null
    var showMemeListButton: Button?=null
    var msg: String? = ""
    var lastMsg = ""
    var requestQueue: RequestQueue? = null
    var list_row_layout: RelativeLayout?=null
    var Manager: LinearLayoutManager? = null
    var adapter: DataAdapter? = null
    var ImgUrls: ArrayList<String> = ArrayList()
    var recyclerView: RecyclerView? = null
    //var shareButton: ImageButton?=null


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

                bouttonGenerer!!.setOnClickListener{
                    var listurl : ArrayList<String> = arrayListOf()
                    for ((key, value) in Listmeme){
                        listurl.add(key)
                    }

                    com.example.myapplicationsqd.request.DownloadManager.downloadImage(API_URL.format(memeSelected, textHaut, textBas),applicationContext,this@MainActivity)
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

        /*shareButton!!.setOnClickListener {
            val bitmap = imageViews.drawable
            //val bitMap : Bitmap =imageViews.getDrawingCache()
            val bos : ByteArrayOutputStream = ByteArrayOutputStream()

            val directory: File= File(cacheDir,"images")
            try{
                directory.mkdir()
                var file: File = File(directory,"image_partagee.jpg")
                var outputStream: FileOutputStream= FileOutputStream(file)
                bitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                outputStream.flush()
                outputStream.close()
                val uri = FileProvider.getUriForFile(applicationContext, "com.example.myapplicationsqd", file);
            }catch (e:Exception){}

        }*/



        //RecyclerView qui affiche le meme selectionné

    }




}