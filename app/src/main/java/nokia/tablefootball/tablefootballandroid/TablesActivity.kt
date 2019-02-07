package nokia.tablefootball.tablefootballandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tables.*
import nokia.tablefootball.tablefootballandroid.adapters.FloorListAdapter
import nokia.tablefootball.tablefootballandroid.network.ServerCommunicationAPIController
import nokia.tablefootball.tablefootballandroid.network.ServerCommunicationServiceImpl
import nokia.tablefootball.tablefootballandroid.utils.JSONTableParser

class TablesActivity : AppCompatActivity()
{

    private val serviceImpl = ServerCommunicationServiceImpl(this)
    private val controller = ServerCommunicationAPIController(serviceImpl)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.app_bar))
        setContentView(R.layout.activity_tables)

        val url = intent.extras.getString("URL").toString()

        var expandableListAdapter: FloorListAdapter

        controller.get(url) { response ->
            expandableListAdapter = FloorListAdapter(
                this,
                JSONTableParser.parseArray(response)
            )

            expandable_list_view.setAdapter(expandableListAdapter)

            swipe_refresh_layout.setOnRefreshListener {
                controller.get(url) { response ->
                    val newList = JSONTableParser.parseArray(response)
                    expandableListAdapter.updateData(newList)
                    swipe_refresh_layout.isRefreshing = false
                }
        }





            /*
        //        val testArray = JSONArray("[{\"id\":\"01:C1:D1:31:F2:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":2,\"room\":225},{\"id\":\"01:F1:D1:31:F1:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":4,\"room\":473},{\"id\":\"21:33:88:31:20:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":80},{\"id\":\"43:C1:D1:74:F1:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":3,\"room\":385},{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":1,\"room\":132},{\"id\":\"06:F9:A1:AA:BB:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":3,\"room\":395}]")
        //        val testArray = JSONArray(
        //            "[{\"id\":\"01:C1:D1:31:F2:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":225}," +
        //                "{\"id\":\"01:F1:D1:31:F1:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":473}," +
        //                "{\"id\":\"21:33:88:31:20:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":80}," +
        //                "{\"id\":\"43:C1:D1:74:F1:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":385}," +
        //                "{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":1,\"room\":37}," +
        //                "{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":1,\"room\":21}," +
        //                "{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":3,\"room\":37}," +
        //                "{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":3,\"room\":13}," +
        //                "{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":4,\"room\":132}," +
        //                "{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":5,\"room\":177}," +
        //                "{\"id\":\"06:F9:A1:AA:BB:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":395}]")
        //        val expandableListAdapter = FloorListAdapter(
        //            this,
        //            JSONTableParser.parseArray(testArray)
        //        )

         //       expandable_list_view.setAdapter(expandableListAdapter)
        */
        }

    }
}
