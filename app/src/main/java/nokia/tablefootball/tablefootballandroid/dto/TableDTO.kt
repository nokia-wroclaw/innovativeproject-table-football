package nokia.tablefootball.tablefootballandroid.dto

data class TableDTO(val id:String, val occupied:Boolean,
                    val online:Boolean, val lastNotifDate: Long,
                    val floor:Int, val room:Int) : Comparable<TableDTO> {

    override fun compareTo(other: TableDTO): Int {
        return room - other.room
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TableDTO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}
