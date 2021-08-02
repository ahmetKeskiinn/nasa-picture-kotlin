package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.InternetSharedPref

class NoInternetFragment : Fragment() {
    private lateinit var root:View
    private lateinit var internet:InternetSharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_no_internet, container, false)
        return root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        initShared()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }
    private fun initShared(){
        internet = InternetSharedPref()
        context?.let { internet.instancePref(it) }
        isOnline()

    }
    private fun checkInternet() : String{
        return internet.getInternet().toString()

    }
    private fun isOnline(){
        if(checkInternet().equals("true")){
            Navigation.findNavController(root).navigate(R.id.navigation_curiosity)
        }
        else{
            Handler().postDelayed({ this.isOnline() }, 1000)
        }
    }
}