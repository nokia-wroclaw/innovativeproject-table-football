package nokia.tablefootball.tablefootballandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        main_webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        val url = intent.extras.get("url").toString()

        main_webview.settings.javaScriptEnabled = true // ## Check JS vulnerabilities ##
        main_webview.settings.setAppCacheEnabled(true)
        main_webview.settings.domStorageEnabled = true


        Log.i("URL", url)
        main_webview.loadUrl(url)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) {
            var newToken: String = it.token
            Log.e("TOKEN", newToken)
        }
    }
}