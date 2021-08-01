package example.com.nasapictureproject.Views

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import example.com.nasapictureproject.Models.AllPhotos
import example.com.nasapictureproject.Models.Photo
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.Api
import example.com.nasapictureproject.Utils.CurrentFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var TAG = "Activity Main"
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var sharedPref:CurrentFragment
    private lateinit var api:Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView  = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_curiosity, R.id.navigation_oppority, R.id.navigation_spirit
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        disableVisible()
        super.onStop()
    }

    override fun onPause() {
        disableVisible()
        super.onPause()
    }

    override fun onDestroy() {
        sharedPref.setCurrentFragment("false")
        super.onDestroy()

    }
    override fun onResume() {
        initSharedPref()
        enableVisible()
        super.onResume()
    }
    private fun disableVisible(){
        navController.navigate(R.id.onPauseFragment)
        navView.isVisible = false
    }
    private fun enableVisible(){
        Toast.makeText(this,sharedPref.getCurrentFragment(),Toast.LENGTH_SHORT).show()
        if(sharedPref.getCurrentFragment().equals("curiosity") || sharedPref.getCurrentFragment().equals("false")){
            navController.navigate(R.id.navigation_curiosity)
        }
        else if(sharedPref.getCurrentFragment().equals("oppority") ){
            navController.navigate(R.id.navigation_oppority)

        }
        else if(sharedPref.getCurrentFragment().equals("spirit")){
            navController.navigate(R.id.navigation_spirit)

        }
        navView.isVisible = true
    }
    private fun initSharedPref(){
        sharedPref = CurrentFragment()
        sharedPref.instancePref(applicationContext)
    }
    }