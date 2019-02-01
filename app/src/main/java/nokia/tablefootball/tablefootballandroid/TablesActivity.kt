package nokia.tablefootball.tablefootballandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_tables.*
import nokia.tablefootball.tablefootballandroid.adapters.FloorListAdapter
import nokia.tablefootball.tablefootballandroid.network.DataAcquirerAPIController
import nokia.tablefootball.tablefootballandroid.network.DataAcquirerServiceImpl
import nokia.tablefootball.tablefootballandroid.utils.JSONTableParser
import org.json.JSONArray

class TablesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables)

        val serviceImpl = DataAcquirerServiceImpl(this)
        val controller = DataAcquirerAPIController(serviceImpl)

        val url = intent.extras.getString("URL").toString()
//        controller.post(url, null) { response ->
//            val expandableListAdapter = FloorListAdapter(
//                applicationContext,
//                //JSONTableParser.parseArray(response)
//                JSONTableParser.parseArray(testArray)
//            )
//            expandableListView.setAdapter(expandableListAdapter)
//        }

        val testArray = JSONArray("[{\"id\":\"01:C1:D1:31:F2:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":2,\"room\":225},{\"id\":\"01:F1:D1:31:F1:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":4,\"room\":473},{\"id\":\"21:33:88:31:20:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":80},{\"id\":\"43:C1:D1:74:F1:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":3,\"room\":385},{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":1,\"room\":132},{\"id\":\"06:F9:A1:AA:BB:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":3,\"room\":395}]")
        // val testArray = JSONArray("[{\"id\":\"01:C1:D1:31:F2:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":5,\"room\":225},{\"id\":\"01:F1:D1:31:F1:C1\",\"occupied\":true,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":473},{\"id\":\"21:33:88:31:20:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":80},{\"id\":\"43:C1:D1:74:F1:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":385},{\"id\":\"01:F1:D1:88:99:C1\",\"occupied\":false,\"online\":false,\"lastNotificationDate\":1539159310000,\"floor\":0,\"room\":132},{\"id\":\"06:F9:A1:AA:BB:C1\",\"occupied\":false,\"online\":true,\"lastNotificationDate\":1539159310000,\"floor\":1,\"room\":395}]")
        val expandableListAdapter = FloorListAdapter(
            applicationContext,
            JSONTableParser.parseArray(testArray)
        )

        expandableListView.setAdapter(expandableListAdapter)
        }

    }
