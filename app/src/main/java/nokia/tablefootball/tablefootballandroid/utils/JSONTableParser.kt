package nokia.tablefootball.tablefootballandroid.utils

import nokia.tablefootball.tablefootballandroid.model.TableModel
import org.json.JSONArray
import org.json.JSONObject

class JSONTableParser
{
    companion object
    {
        fun parseArray(jsonArray: JSONArray): List<TableModel>
        {

            val tables = ArrayList<TableModel>()

            for (i in 0..(jsonArray.length() - 1))
            {
                val obj = jsonArray.getJSONObject(i)
                val tableDto = parseObject(obj)

                tables.add(tableDto)
            }

            return tables
        }

        fun parseObject(obj: JSONObject): TableModel
        {
            return TableModel(
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
