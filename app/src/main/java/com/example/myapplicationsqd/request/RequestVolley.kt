package com.example.myapplicationsqd.request

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import com.example.myapplicationsqd.MainActivity
class RequestVolley {
    companion object JsonParsing{
    fun jsonParse(requestQueue: RequestQueue, Listmeme: HashMap<String,String>) {
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
}