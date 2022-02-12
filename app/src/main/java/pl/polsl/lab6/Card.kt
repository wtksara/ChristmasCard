package pl.polsl.lab6
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

public class Card : AppCompatActivity() {

    var listOfGifts: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle: Bundle = intent.extras!!
        listOfGifts = bundle.get("GIFTS") as String

        val webPage = WebView(this)
        webPage.settings.javaScriptEnabled = true
        webPage.addJavascriptInterface(this, "cardactivity")
        webPage.loadUrl("file:///android_asset/card.html")
        setContentView(webPage)
    }

    @JavascriptInterface
    fun getGiftList(): String {
        return listOfGifts
    }

}
