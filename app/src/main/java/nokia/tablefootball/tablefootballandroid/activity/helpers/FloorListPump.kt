package nokia.tablefootball.tablefootballandroid.activity.helpers

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import nokia.tablefootball.tablefootballandroid.exception.FootballGetRequestException
import nokia.tablefootball.tablefootballandroid.utils.JSONTableParser
import nokia.tablefootball.tablefootballandroid.RequestQueueSingleton
import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import org.json.JSONArray
import java.util.ArrayList
import java.util.HashMap

object FloorListPump {

    val data: HashMap<String, List<String>>
        get(){
            val expandableListDetail = HashMap<String, List<String>>()

            val cricket = ArrayList<String>()
            cricket.add("India")
            cricket.add("Pakistan")
            cricket.add("Australia")
            cricket.add("England")
            cricket.add("South Africa")

            val football = ArrayList<String>()
            football.add("Brazil")
            football.add("Spain")
            football.add("Germany")
            football.add("Netherlands")
            football.add("Italy")

            val basketball = ArrayList<String>()
            basketball.add("United States")
            basketball.add("Spain")
            basketball.add("Argentina")
            basketball.add("France")
            basketball.add("Russia")

            expandableListDetail["CRICKET TEAMS"] = cricket
            expandableListDetail["FOOTBALL TEAMS"] = football
            expandableListDetail["BASKETBALL TEAMS"] = basketball
            return expandableListDetail
        }



    fun getTablesMap(url: String, context: Context) : HashMap<Int, Set<TableDTO>>? {

        val resultMap : HashMap<Int, Set<TableDTO>>? = null

        val jsonArrayRequest=  JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val jsonArray : JSONArray = response
                var setTableDto: Set<TableDTO> =
                    JSONTableParser.parseArray(jsonArray)


            },
            Response.ErrorListener { error ->
                throw FootballGetRequestException(error.message)
            })

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest)

        return resultMap;
    }

}
/* JSON Form
    [
        {
            id: ID:IN:THIS:FORM,
            occupied: true
            online: false
            lastNDate: DATE
            floor: 3
            room: 2
        },
        ...
     ]

 */