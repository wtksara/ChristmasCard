package pl.polsl.lab6

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.widget.Toast
import java.util.*

class Gifts : AppCompatActivity() {

    var listOfGifts: String = ""
    var istalking: Boolean = false

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webPage = WebView(this)
        webPage.settings.javaScriptEnabled = true
        webPage.addJavascriptInterface(this, "giftsactivity")
        webPage.loadUrl("file:///android_asset/gifts.html")
        setContentView(webPage)
    }

    @JavascriptInterface
    fun addGift(view: View?) {
        istalking = true
        val voice = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voice.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pl-PL")
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "Powiedz jaki prezent chcesz dostac")
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(voice, 0)
        } else {
            Toast.makeText(this, "Nie slysze", Toast.LENGTH_SHORT)
                .show()
            istalking = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK && data != null) {
                val items : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                listOfGifts += if (listOfGifts.isEmpty()) {
                    items[0]
                } else {
                    "\n" + items[0]
                }
                istalking = false
            } else
                istalking = false
        }
    }

    @JavascriptInterface
    fun getGiftList(): String {
        return listOfGifts
    }

    @JavascriptInterface
    fun getTalking(): Int {
        if (istalking)
            return 1
        else
            return 0
    }

    @JavascriptInterface
    fun generateCard(view: View?) {
        intent = Intent(this, Card::class.java)
        intent.putExtra("GIFTS", listOfGifts)
        startActivity(intent)
    }
}