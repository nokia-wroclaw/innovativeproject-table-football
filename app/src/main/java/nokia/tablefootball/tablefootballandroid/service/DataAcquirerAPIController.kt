package nokia.tablefootball.tablefootballandroid.service

import org.json.JSONArray
import org.json.JSONObject

class DataAcquirerAPIController constructor(serviceInjection: DataAcquirerService) : DataAcquirerService {

    private val service: DataAcquirerService = serviceInjection

    override fun post(
        path: String,
        params: JSONObject?,
        completionHandler: (response: JSONArray) -> Unit
    ) {
        service.post(path,params, completionHandler)
    }

}