package nokia.tablefootball.tablefootballandroid.utils

import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import java.util.*
import kotlin.collections.Collection
import kotlin.collections.getValue

class TableDataUtil {
    companion object {
        fun toFloorMap(tables: Collection<TableDTO>): TreeMap<Int, ArrayList<TableDTO>> {

            val resultMap = TreeMap<Int, ArrayList<TableDTO>>()

            for (dto in tables) {

                var operationSet = ArrayList<TableDTO>()

                if (resultMap.containsKey(dto.floor)) {
                    operationSet = resultMap.getValue(dto.floor)
                }

                operationSet.add(dto)
                resultMap[dto.floor] = operationSet
            }

            return resultMap
        }

        fun toFloorMapAsStrings(tables: Collection<TableDTO>): TreeMap<String, List<String>>{
            val map = toFloorMap(tables)

            val result = TreeMap<String, List<String>>()

            for(key : Int in map.keys){
                result[key.toString()] = dtoAsStringList(map[key]!!)
            }

            return result
        }

        fun asStringList(floors: Collection<Int>) : ArrayList<String>{
            val list = ArrayList<String>(floors.size)

            for(i : Int in floors){
                list.add(i.toString())
            }

            return list

        }

        fun dtoAsStringList(dtos: Collection<TableDTO>) : ArrayList<String>{
            val list = ArrayList<String>(dtos.size)

            for(dto : TableDTO in dtos){
                list.add("${dto.id} ${dto.occupied}")
            }

            return list

        }
    }
}