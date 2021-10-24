import android.content.Context
import org.json.JSONObject

class JSONtoListMeme (context: Context, name: String):
    ArrayList<String>(){

    companion object {
        private const val EXTENSION: String = ".json"
    }

    fun objectToJson(key : String, value: String): JSONObject? { return null}
    fun jsonToObject(json: JSONObject): String?{
        return null
    }

    fun dataToString(data: HashMap<String, String>): String {
        val json = JSONObject()
        data.forEach{ json.put("${it.key}", objectToJson(it.key, it.value))}
        return json.toString()
    }

    fun stringToData(value : String): HashMap<String, String> {
        val data = HashMap<String, String>()
        val json = JSONObject(value)
        val iterator = json.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            data[key] = jsonToObject(json.getJSONObject(key))
        }
        return data

    }


}