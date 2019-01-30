package nokia.tablefootball.tablefootballandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tables.*
import nokia.tablefootball.tablefootballandroid.activity.helpers.FloorListAdapter
import nokia.tablefootball.tablefootballandroid.network.DataAcquirerAPIController
import nokia.tablefootball.tablefootballandroid.network.DataAcquirerServiceImpl
import nokia.tablefootball.tablefootballandroid.utils.JSONTableParser
import nokia.tablefootball.tablefootballandroid.utils.TableDataUtil

class TablesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables)

        val serviceImpl = DataAcquirerServiceImpl(this)
        val controller = DataAcquirerAPIController(serviceImpl)

        val url = intent.extras.getString("URL").toString()

        controller.post(url, null) { response ->
            val expandableListAdapter = FloorListAdapter(
                applicationContext,
                JSONTableParser.parseArray(response)
            )
            expandableListView.setAdapter(expandableListAdapter)
        }



//        expandableListView.setOnGroupExpandListener {
//            Toast.makeText(
//                applicationContext,
//                "testX" + " List Expanded.",
//                Toast.LENGTH_SHORT
//            ).show();

        }

    }


/* POC


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables)


        var expandableListDetail: HashMap<String, List<String>> = FloorListPump.data
        var expandableListTitle = ArrayList<String>(expandableListDetail.keys)
        var expandableListAdapter = FloorListAdapter(
            this,
            expandableListTitle,
            expandableListDetail
        )
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener {
            Toast.makeText(
                applicationContext,
                "testX" + " List Expanded.",
                Toast.LENGTH_SHORT
            ).show();


        }

        expandableListView.setOnGroupCollapseListener {
            Toast.makeText(
                applicationContext,
                "testX" + " List collapsed.",
                Toast.LENGTH_SHORT
            ).show();

        }

    }



 */