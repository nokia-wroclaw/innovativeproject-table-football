package nokia.tablefootball.tablefootballandroid

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_input_adress.*

class InputAdressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_adress)


        apply_button.setOnClickListener {
            val ipAddress = ip_textinput.text

            val intent = Intent(applicationContext, TablesActivity::class.java);
            intent.putExtra("url",ipAddress)

            startActivity(intent)
        }
    }

}
