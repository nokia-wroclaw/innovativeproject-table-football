package nokia.tablefootball.tablefootballandroid

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.contains
import com.natpryce.hamkrest.equalTo
import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import nokia.tablefootball.tablefootballandroid.utils.TableDataUtil
import org.junit.Test

class TableDataUtilTest{

    var dto1 = TableDTO("aaaaaaaaaa",true,true,123,1,2)
    var dto2 = TableDTO("aaaaaaaaaa",true,true,123,1,2)
    var dto3 = TableDTO("bbbbbbbbbb",true,true,123,2,2)
    var dto4 = TableDTO("cccccccccc",false,false,122,2,3)


    @Test
    fun givenArrayListWithDoubledEntries_thenReturnProperMap(){
        var array = ArrayList<TableDTO>()
        array.add(dto1)
        array.add(dto2)
        array.add(dto3)

        var result = TableDataUtil.toFloorMap(array)

        assertThat(result.keys.size, equalTo(2))
        assertThat(result[1]!!.size, equalTo(1))
        assertThat(result[2]!!.size, equalTo(1))

    }

    @Test
    fun givenArrayWithTheSameFloorNo_thenReturnSetContaingValueWithTwoEntries(){
        val array = ArrayList<TableDTO>()
        array.add(dto3)
        array.add(dto4)

        val result = TableDataUtil.toFloorMap(array)

        assertThat(result[2]!!.size, equalTo(2))
    }

    @Test
    fun givenCollectionOfInts_shouldReturnListWithStrings(){
        val values = HashSet<Int>()
        values.add(1)
        values.add(22)
        values.add(33)

        val result = TableDataUtil.asStringList(values)

        assertThat(result.size, equalTo(3))
        assert(result.contains("22"))
        assert(result.contains("1"))
        assert(result.contains("33"))


    }
}