package nokia.tablefootball.tablefootballandroid.dto

data class TableDTO(val id:String, val occupied:Boolean,
                    val online:Boolean, val lastNotifDate: Long,
                    val floor:Int, val room:Int) {

    // TODO set proper icon
//    val iconPath: String = when(online){
//        false ->
//    }

}
