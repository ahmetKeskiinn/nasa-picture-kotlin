package example.com.nasapictureproject.Views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.InternetCheckingService
import example.com.nasapictureproject.Utils.InternetSharedPref

class SplashScreen : AppCompatActivity() {
    private lateinit var isOnline: InternetSharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onStart() {
        startInternetService()
        super.onStart()
    }

    private fun goMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun checkInternet() {
        isOnline = InternetSharedPref()
        isOnline.instancePref(applicationContext)
        if (isOnline.getInternet().equals("true")) {
            Handler().postDelayed({ this.goMainPage() }, 500)
        } else {
            Toast.makeText(this, getString(R.string.openInternet), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ this.checkInternet() }, 1000)
        }
    }

    private fun startInternetService() {
        val intent = Intent(this, InternetCheckingService::class.java)
        intent.action = "checkinternet"
        startService(intent)
        checkInternet()

    }
}