package nokia.tablefootball.tablefootballandroid.network

import org.json.JSONArray
import org.json.JSONObject

interface DataAcquirerService {
    fun post(
        path: String,
        params: JSONObject?,
        completionHandler: (response: JSONArray) -> Unit
    )
}