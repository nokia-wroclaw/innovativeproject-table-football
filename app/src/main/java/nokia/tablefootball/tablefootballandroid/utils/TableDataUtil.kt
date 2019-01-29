package nokia.tablefootball.tablefootballandroid.utils

import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import java.util.*
import kotlin.collections.Collection
import kotlin.collections.HashSet
import kotlin.collections.getValue

class TableDataUtil {
    companion object {
        fun toFloorMap(tables: Collection<TableDTO>): TreeMap<Int, HashSet<TableDTO>> {

            val resultMap = TreeMap<Int, HashSet<TableDTO>>()

            val dtoSet = HashSet<TableDTO>(tables)

            for (dto in dtoSet) {

                var operationSet: HashSet<TableDTO> = HashSet()

                if (resultMap.containsKey(dto.floor)) {
                    operationSet = resultMap.getValue(dto.floor)
                }

                operationSet.add(dto)
                resultMap.put(dto.floor, operationSet)
            }

            return resultMap
        }

        fun toFloorMapAsStrings(tables: Collection<TableDTO>): TreeMap<String, List<String>>{
            val map = toFloorMap(tables)

            val result = TreeMap<String, List<String>>()

            for(key : Int in map.keys){
               result.put(key.toString(), dtoAsStringList(map[key]!!))
            }

            return result
        }

        fun toFloorMapAsStrings(tables: HashMap<Int, HashSet<TableDTO>>): TreeMap<String, List<String>>{
           return toFloorMapAsStrings(tables)
        }


        fun asStringList(floors: Collection<Int>) : ArrayList<String>{
            val list = ArrayList<String>(floors.size)

            for(i : Int in floors){
                list.add(i.toString())
            }

            return list;

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