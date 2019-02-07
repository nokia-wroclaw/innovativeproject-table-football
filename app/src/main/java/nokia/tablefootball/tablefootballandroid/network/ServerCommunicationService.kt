package nokia.tablefootball.tablefootballandroid.network

import org.json.JSONArray
import org.json.JSONObject

interface ServerCommunicationService
{
    fun get(
        path: String,
        completionHandler: (response: JSONArray) -> Unit
    )

    fun post(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject) -> Unit
    )
}