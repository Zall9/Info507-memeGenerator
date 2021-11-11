package com.example.myapplicationsqd.request

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import com.example.myapplicationsqd.MainActivity
//Il n'est pas prévu d'instancier cette classe.
// On accède à la fonction jsonParse de manière statique
class RequestVolley {
    companion object JsonParsing{
    fun jsonParse(requestQueue: RequestQueue, Listmeme: HashMap<String,String>) {
        //URL du majestueux serveur permettant cette merveille de technologie qu'est notre générateur de meme
        val url = "http://os-vps418.infomaniak.ch:1186/i507_1_2/listmeme.json"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val iter = response.keys()
                    while (iter.hasNext()) { //tant que le json à des valeurs
                        val key = iter.next()//on itère sur le suivant
                        try {
                            val value = response.get(key) as String
                            Listmeme[key] = value //on assigne value à Listmeme[key]
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
}