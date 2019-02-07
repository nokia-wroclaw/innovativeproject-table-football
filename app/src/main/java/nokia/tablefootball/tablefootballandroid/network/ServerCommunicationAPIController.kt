package nokia.tablefootball.tablefootballandroid.network

import org.json.JSONArray
import org.json.JSONObject

class ServerCommunicationAPIController constructor(serviceInjection: ServerCommunicationService) : ServerCommunicationService
{
    private val service: ServerCommunicationService = serviceInjection

    override fun get(
        path: String,
        completionHandler: (response: JSONArray) -> Unit
    )
    {
        service.get(path, completionHandler)
    }

    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject) -> Unit)
    {
        service.post(path, params, completionHandler)
    }

}
