package com.example.myapplicationsqd
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {
    var ImgUrls: ArrayList<String> = ArrayList()
    var recyclerView: RecyclerView? = null
    var Manager: LinearLayoutManager? = null
    var adapter: DataAdapter? = null
    var msg: String? = ""
    var lastMsg = ""
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
                    File.separator + "MemeCreator" + File.separator + url.substring(url.lastIndexOf("/") + 1)
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
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //creer_Json_local(ImgUrls);
        //creer_Json_extern(ImgUrls);
        ImgUrls.add("https://images.pexels.com/photos/2220401/pexels-photo-2220401.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=350")
        ImgUrls.add("https://images.pexels.com/photos/2765586/pexels-photo-2765586.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=350")
        ImgUrls.add("https://i.imgur.com/DulGEc5_d.jpg")
        ImgUrls.add("https://i.imgur.com/rXkffMg_d.jpg")
        val sizeArray=ImgUrls.size
        for (i in 0 until sizeArray-1)
        {
            downloadImage(ImgUrls[i])
        }

        recyclerView = findViewById<View>(R.id.card_recycler_view) as RecyclerView
        Manager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = Manager
        adapter = DataAdapter(this,ImgUrls)
        recyclerView!!.adapter = adapter
    }
}