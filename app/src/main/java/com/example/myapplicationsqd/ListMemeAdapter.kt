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

class ListMemeAdapter (
    private val context: Context,
    private val Listmeme: ArrayList<String>
) :
    RecyclerView.Adapter<ListMemeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_row_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.txt_android.text= "Meme: " + Listmeme.get(i).toString()

    }

    override fun getItemCount(): Int {
        return Listmeme.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txt_android: TextView

        init {
            txt_android =
                view.findViewById<View>(R.id.txt_android) as TextView
        }
    }

}
class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txt_android = view.findViewById<TextView>(R.id.txt_android)
}