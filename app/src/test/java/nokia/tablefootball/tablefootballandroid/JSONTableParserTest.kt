package nokia.tablefootball.tablefootballandroid

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import nokia.tablefootball.tablefootballandroid.utils.JSONTableParser
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test


class JSONTableParserTest {

    @Test
    fun givenArray_shouldReturnSetWithTwoEntries() {
        val jsonTestArray =
            "[" +
                    "{" +
                    "\"id\":\"01:F1:D1:31:F1:C1\"," +
                    "\"occupied\":true," +
                    "\"online\":true," +
                    "\"lastNotificationDate\":1539159310000," +
                    "\"floor\":4," +
                    "\"room\":473" +
                    "},{" +
                    "\"id\":\"00:00:00:00:F1:C1\"," +
                    "\"occupied\":false," +
                    "\"online\":true," +
                    "\"lastNotificationDate\":1512345310000," +
                    "\"floor\":1," +
                    "\"room\":22" +
                    "}," +
                    "]"

        val jsonArray = JSONArray(jsonTestArray)

        val resultSet = JSONTableParser.parseArray(jsonArray)
        assertThat(resultSet.size, equalTo(2))
    }

    @Test
    fun givenAboveAbove_shouldReturnProperDTO() {
        val jsonTestObj = "{" +
                "\"id\":\"01:F1:D1:31:F1:C1\"," +
                "\"occupied\":true," +
                "\"online\":true," +
                "\"lastNotificationDate\":1539159310000," +
                "\"floor\":4," +
                "\"room\":473" +
                "}"

        val jsonObj = JSONObject(jsonTestObj)

        val dto = JSONTableParser.parseObject(jsonObj)

        assertThat(dto.id, equalTo("01:F1:D1:31:F1:C1"))
        assertThat(dto.occupied, equalTo(true))
        assertThat(dto.online, equalTo(true))
        assertThat(dto.lastNotifDate, equalTo(1539159310000))
        assertThat(dto.floor, equalTo(4))
        assertThat(dto.room, equalTo(473))


    }


}