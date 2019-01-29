package nokia.tablefootball.tablefootballandroid.service

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonArrayRequest
import nokia.tablefootball.tablefootballandroid.RequestQueueSingleton
import org.json.JSONArray
import org.json.JSONObject

class DataAcquirerServiceImpl(private val context: Context) : DataAcquirerService {

    private val TAG = "JSON GET"

    override fun post(
        path: String,
        params: JSONObject?,
        completionHandler: (response: JSONArray) -> Unit
    ) {
                                                                         //   TODO("params")
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
            Response.Listener<JSONArray> { response ->
                Log.d(TAG, "/post request OK!")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(JSONArray())
            })

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest)
    }
}
