package nokia.tablefootball.tablefootballandroid.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class ServerCommunicationServiceImpl(private val context: Context) : ServerCommunicationService
{
    private val TAG = "JSON REQUEST"

    override fun get(
        path: String,
        completionHandler: (response: JSONArray) -> Unit
    )
    {
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
            Response.Listener<JSONArray> { response ->
                Log.d(TAG, "/get request OK!")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
                completionHandler(JSONArray())
            })

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest)
    }

    override fun post(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject) -> Unit
    )
    {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, path, params,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "/post request OK!")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(JSONObject())
            })

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)

    }
}
