package nokia.tablefootball.tablefootballandroid.dto

data class TableDTO(val id:String, val occupied:Boolean,
                    val online:Boolean, val lastNotifDate: Long,
                    val floor:Int, val room:Int) : Comparable<TableDTO> {

    override fun compareTo(other: TableDTO): Int {
        return room - other.room
    }

}
