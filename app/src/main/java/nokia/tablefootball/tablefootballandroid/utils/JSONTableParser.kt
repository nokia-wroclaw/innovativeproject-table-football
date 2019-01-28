package nokia.tablefootball.tablefootballandroid.utils

import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import org.json.JSONArray
import org.json.JSONObject

class JSONTableParser {
    companion object {
        fun parseArray(jsonArray: JSONArray) : Set<TableDTO>{

            val tableSet: HashSet<TableDTO> = HashSet<TableDTO>()

            for(i in 0..(jsonArray.length()-1)){
                val obj = jsonArray.getJSONObject(i)
                val tableDto = parseObject(obj)

                tableSet.add(tableDto)
            }

            return tableSet;
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
