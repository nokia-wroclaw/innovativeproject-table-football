package nokia.tablefootball.tablefootballandroid

import java.util.ArrayList
import java.util.HashMap

object FloorListInflater {

    val data: HashMap<String, List<String>>
        get() {
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

}
