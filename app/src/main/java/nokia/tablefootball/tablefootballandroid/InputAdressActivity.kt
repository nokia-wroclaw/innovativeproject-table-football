package nokia.tablefootball.tablefootballandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_input_adress.*
import nokia.tablefootball.tablefootballandroid.service.DataAcquirerAPIController
import nokia.tablefootball.tablefootballandroid.service.DataAcquirerServiceImpl
import nokia.tablefootball.tablefootballandroid.utils.JSONTableParser
import nokia.tablefootball.tablefootballandroid.utils.TableDataUtil

class InputAdressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_adress)

        apply_button.setOnClickListener {
//            val serviceImpl = DataAcquirerServiceImpl(this)
//            val controller = DataAcquirerAPIController(serviceImpl)
//
//            val url = "http://192.168.0.111:8080/sensorStatus"
//
//            controller.post(url,null){response ->
//               Log.d("JSON", TableDataUtil.toFloorMap(JSONTableParser.parseArray(response!!)).toString())
//            }

            var intent = Intent(applicationContext, TablesActivity::class.java).apply {
                putExtra("URL", ip_textinput.text.toString())
            }

            startActivity(intent)
        }
    }


}
