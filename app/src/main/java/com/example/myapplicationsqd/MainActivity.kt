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
    lateinit var toptext: EditText
    lateinit var bottomtext: EditText
    lateinit var recyclerViewListMeme: RecyclerView
    lateinit var managerViewListMeme: LinearLayoutManager
    lateinit var adapterViewListMeme: ListMemeAdapter
    lateinit var bouttonTelecharger: Button
    lateinit var bouttonGenerer: Button
    lateinit var showMemeListButton: Button
    lateinit var requestQueue: RequestQueue
    lateinit var shareButton: ImageButton
    var Listmeme: HashMap<String,String> = hashMapOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = Volley.newRequestQueue(this)

        val API_URL = "http://apimeme.com/meme?meme=%s&top=%s&bottom=%s"
            //On appliquera une methode de formatage sur cette constante
            //pour remplacer les %s dans la chaine afin de construire le meme

        //instanciation des bouttons
        shareButton=findViewById<ImageButton>(R.id.sharebutton)
        bouttonGenerer= findViewById<Button>(R.id.bouttonGenerate)
        bouttonTelecharger=findViewById<Button>(R.id.bouttonSent)
        showMemeListButton= findViewById<Button>(R.id.ButtonShowListOfMeme)
        //instanciation des textview

        toptext= findViewById<EditText>(R.id.textUp)
        bottomtext= findViewById<EditText>(R.id.textDown)
        //on récupere le texte dans des variables
        var textHaut= toptext.text
        var textBas= bottomtext.text


        //On crée le listener du boutton qui affiche la liste des memes
        showMemeListButton.setOnClickListener {
            //le resultat de la requete volley sera parse dans la hashmap Listmeme<String,String>
            RequestVolley.jsonParse(requestQueue, Listmeme)
            //gestion de l'affichage de la liste
            if (recyclerViewListMeme.visibility==View.VISIBLE){
                recyclerViewListMeme.visibility= View.GONE
            }
            else
                recyclerViewListMeme.visibility= View.VISIBLE
        }
        //Reycler View de la liste des memes a selectionner
        recyclerViewListMeme= findViewById<View>(R.id.listOfPictures) as RecyclerView
        managerViewListMeme= LinearLayoutManager(this)
        recyclerViewListMeme.layoutManager=managerViewListMeme
        imageViews= findViewById<ImageView>(R.id.card_current_image_view)
        //on définit l'adapter de la liste des memes
        adapterViewListMeme= object : ListMemeAdapter(applicationContext, Listmeme) {
            override fun onItemClick(view: View): Boolean {
                // On récupère le contenu des valeurs dans Listmeme
                // car on ne peut pas ittérer sur des hashmaps
                var list : ArrayList<String> = arrayListOf()
                for ((key, value) in Listmeme){
                    list.add(value)
                }

                val memeSelected=list.get(recyclerViewListMeme.getChildViewHolder(view).adapterPosition)
                //la ligne ci-dessus permet de récupérer l'élement cliqué dans la liste
                //ensuite on le déssine avec picasso
                Picasso.with(applicationContext).load(API_URL.format(memeSelected, "TOP", "BOTTOM")).resize(600, 600).into(imageViews)

                //le boutton generer permet d'afficher le meme désiré
                bouttonGenerer.setOnClickListener {
                    Picasso.with(applicationContext).load(API_URL.format(memeSelected, textHaut, textBas)).resize(550,600).into(imageViews)
                }

                //Telecharger
                bouttonTelecharger.setOnClickListener{
                    com.example.myapplicationsqd.request.DownloadManager.downloadImage(API_URL.format(memeSelected, textHaut, textBas),applicationContext,this@MainActivity)
                }
                Toast.makeText(applicationContext, "$memeSelected", Toast.LENGTH_LONG).show()
                //Share-Intent pour partager le meme selectionné dans une autre application
                shareButton.setOnClickListener{
                    val bitmap = (imageViews.getDrawable() as BitmapDrawable).bitmap
                    //on recupère, convertis et mets l'image contenue de imageViews dans la variable
                    val intent=Intent()
                    intent.action=Intent.ACTION_SEND
                    //fonction dépréciée mais nous n'avons pas trouvé d'alternative
                    val path= MediaStore.Images.Media.insertImage(contentResolver,bitmap,"title","null")
                    val uri=Uri.parse(path)
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.type="image/*"
                    startActivity(Intent.createChooser(intent,"Share to"))
                }
                recyclerViewListMeme.visibility= View.GONE
                return true
                                                        }
        }
        recyclerViewListMeme.adapter = adapterViewListMeme
    }
}