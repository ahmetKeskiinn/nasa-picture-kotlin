package example.com.nasapictureproject.Utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.util.Log

class InternetCheckingService() : Service() {

    private lateinit var c:Context
    private lateinit var internet:InternetSharedPref
    private var a = 1
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    constructor(context: Context) : this() {
        Log.d("TAG", "asdsadasdasda: ")
        this.c = context

    }
    override fun onCreate() {
        super.onCreate()
        internet = InternetSharedPref()
        internet.instancePref(applicationContext)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handler.post(periodicupdate)
        return START_STICKY
    }

    fun isOnline(c: Context): Boolean {
        val cm = c.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return if (ni != null && ni.isConnectedOrConnecting) {
            true
        } else {
            false
        }
    }

    var handler = Handler()
    private val periodicupdate: Runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1 * 1000 - SystemClock.elapsedRealtime() % 1000)
            val broadCastIntent = Intent()
            broadCastIntent.action = BroadCastStringForAction
            broadCastIntent.putExtra("online_status", "" + isOnline(this@InternetCheckingService))
            if (isOnline(this@InternetCheckingService)) {
                internet.setInternet("true")
            } else {

                internet.setInternet("false")

            }
            sendBroadcast(broadCastIntent)
        }
    }


    companion object {
        const val BroadCastStringForAction = "checkinternet"
    }
}