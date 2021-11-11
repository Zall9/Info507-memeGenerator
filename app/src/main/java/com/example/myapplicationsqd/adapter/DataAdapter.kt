package com.example.myapplicationsqd.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationsqd.R
import com.squareup.picasso.Picasso
import java.util.*

/*Cette classe servirait à afficher la liste des memes présent dans le dossier crée dans le répertoire PICTURES
Nous n'avons pas commenté cettes classe car crée à l'image de ListMemeAdapter
 */

class DataAdapter(
    private val context: Context,
    private val ImgUrls: ArrayList<String>
) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        Picasso.with(context).load(ImgUrls[i]).resize(600, 600).into(viewHolder.img_android)
    }

    override fun getItemCount(): Int {
        return ImgUrls.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var img_android: ImageView

        init {
            img_android =
                view.findViewById<View>(R.id.img_android) as ImageView
        }
    }

}