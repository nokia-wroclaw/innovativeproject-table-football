package nokia.tablefootball.tablefootballandroid.utils

import nokia.tablefootball.tablefootballandroid.model.TableModel
import java.util.*

object TableDataUtil
{
    //companion object {
    fun toFloorMap(tables: Collection<TableModel>): TreeMap<Int, ArrayList<TableModel>>
    {

        val resultMap = TreeMap<Int, ArrayList<TableModel>>()

        for (dto in tables)
        {

            var operationSet = ArrayList<TableModel>()

            if (resultMap.containsKey(dto.floor))
            {
                operationSet = resultMap.getValue(dto.floor)
            }

            operationSet.add(dto)
            operationSet.sort()
            resultMap[dto.floor] = operationSet
        }

        return resultMap
    }

    fun toFloorMapAsStrings(tables: Collection<TableModel>): TreeMap<String, List<String>>
    {
        val map = toFloorMap(tables)

        val result = TreeMap<String, List<String>>()

        for (key: Int in map.keys)
        {
            result[key.toString()] = dtoAsStringList(map[key]!!)
        }

        return result
    }

    fun asStringList(floors: Collection<Int>): List<String>
    {
        val list = ArrayList<String>(floors.size)

        for (i: Int in floors)
        {
            list.add(i.toString())
        }

        return list

    }

    fun dtoAsStringList(models: Collection<TableModel>): List<String>
    {
        val list = ArrayList<String>(models.size)

        for (model: TableModel in models)
        {
            list.add("${model.id} ${model.occupied}")
        }

        return list

    }
    // }
}