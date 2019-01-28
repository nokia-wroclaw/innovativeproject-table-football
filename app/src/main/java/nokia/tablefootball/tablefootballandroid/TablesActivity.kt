package nokia.tablefootball.tablefootballandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_tables.*
import nokia.tablefootball.tablefootballandroid.activity.helpers.FloorListAdapter
import nokia.tablefootball.tablefootballandroid.activity.helpers.FloorListPump

class TablesActivity : AppCompatActivity() {

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
            val url = "http://192.168.0.106:8080/sensorStatus"
            val queue = Volley.newRequestQueue(this)
            val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    Log.d("JSON", "OKKK \n" + response.toString())
                },
                Response.ErrorListener { response ->
                    Log.d("JSON ERROR", response.toString())
                })

            queue.add(jsonRequest)

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

}
