package nokia.tablefootball.tablefootballandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ExpandableListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_tables.*

class TablesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tables)



        var expandableListDetail: HashMap<String, List<String>> = FloorListInflater.data
        var expandableListTitle = ArrayList<String>(expandableListDetail.keys)
        var expandableListAdapter = FloorListAdapter(this, expandableListTitle, expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener{
                Toast.makeText(applicationContext,
                    "testX" + " List Expanded.",
                    Toast.LENGTH_SHORT).show();

        }

        expandableListView.setOnGroupCollapseListener{
            Toast.makeText(applicationContext,
                "testX" + " List collapsed.",
                Toast.LENGTH_SHORT).show();

        }

    }

    }
