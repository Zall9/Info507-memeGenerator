package com.example.myapplicationsqd
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.util.*

class ListAdapter (
    private val context: Context,
    private val ImgUrls: ArrayList<String>
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_row_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.txt_android.text= ImgUrls.get(i)

    }

    override fun getItemCount(): Int {
        return ImgUrls.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txt_android: TextView

        init {
            txt_android =
                view.findViewById<View>(R.id.txt_android) as TextView
        }
    }

}