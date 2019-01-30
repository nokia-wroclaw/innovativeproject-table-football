package nokia.tablefootball.tablefootballandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_input_adress.*

class InputAdressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_adress)

        apply_button.setOnClickListener {
            var intent = Intent(applicationContext, TablesActivity::class.java).apply {
                putExtra("URL", ip_textinput.text.toString())
            }

            startActivity(intent)
        }
    }


}
