package nokia.tablefootball.tablefootballandroid.utils

import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.*

class JSONTableParser {
    companion object {
        fun parseArray(jsonArray: JSONArray) : List<TableDTO>{

            val tables = ArrayList<TableDTO>()

            for(i in 0..(jsonArray.length()-1)){
                val obj = jsonArray.getJSONObject(i)
                val tableDto = parseObject(obj)

                tables.add(tableDto)
            }

            return tables
        }

        fun parseObject(obj: JSONObject) : TableDTO {
            return TableDTO(
                obj.getString("id"),
                obj.getBoolean("occupied"),
                obj.getBoolean("online"),
                obj.getLong("lastNotificationDate"),
                obj.getInt("floor"),
                obj.getInt("room")
            )
        }
    }
}
