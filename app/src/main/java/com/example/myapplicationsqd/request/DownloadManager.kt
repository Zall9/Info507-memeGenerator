package com.example.myapplicationsqd.request

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File

class DownloadManager {
    //Il n'est pas prévu d'instancier cette classe.
    // On accède à la fonction jsonParse de manière statique
    companion object DownloadAndNameObject{
        var msg: String? = ""
        var lastMsg = ""
        lateinit var context:Context

        fun generateName() :String{
            //appliquer cette methode à une image génère une chaine aléatoire et
            //ajoute l'extension .jpg a la fin.
            //
            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') //regex :)
            val randomString = (1..15)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");//chaine aléatoire
            println(randomString + ".jpg")
            return randomString + ".jpg"
        }
        @SuppressLint("Range")
        fun downloadImage(url: String, context: Context, activity: Activity) {
            /*
            *   url:String -> URL de l'image a télécharger
            * la méthode télécharge l'image à partir d'une url en utilisant le gestionnaire de téléchargements d'Android
            *
            */
            this.context=context
            //crée le repertoire s'il n'existe pas
            val directory = File(Environment.DIRECTORY_PICTURES)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            
            val downloadUri = Uri.parse(url)

            val request = DownloadManager.Request(downloadUri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(url.substring(url.lastIndexOf("/") + 1))
                    .setDescription("")
                    .setDestinationInExternalPublicDir(
                        directory.toString(),
                        File.separator + "MemeCreator" + File.separator + generateName()
                    //Création du repertoire MemeCreator dans PICTURES + génère un nom pour l'image
                    )
            }

            val downloadId = downloadManager.enqueue(request)
            val query = DownloadManager.Query().setFilterById(downloadId)
            Thread(Runnable {
                var downloading = true
                while (downloading) {
                    //le curseur permets de determiner si le telechargement est en cours ou terminé
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                    }
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    msg = statusMessage(url, directory, status)
                    if (msg != lastMsg) {
                        activity.runOnUiThread {
                            Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()

                        }
                        lastMsg = msg ?: ""
                    }
                    cursor.close()
                }
            }).start()
        }
        private fun statusMessage(url: String, directory: File, status: Int): String? {
            // affichage de toast statuant l'état du téléchargement à l'utilisateur
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


    }
}