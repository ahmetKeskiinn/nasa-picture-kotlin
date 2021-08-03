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
import example.com.nasapictureproject.Utils.CurrentFragment
import example.com.nasapictureproject.Utils.InternetSharedPref

class NoInternetFragment() : Fragment() {
    private lateinit var root:View
    private lateinit var internet:InternetSharedPref
    private lateinit var currentFragment: CurrentFragment
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
        currentFragment = CurrentFragment()
        context?.let { currentFragment.instancePref(it) }
        internet = InternetSharedPref()
        context?.let { internet.instancePref(it) }
        isOnline()

    }
    private fun checkInternet() : String{
        return internet.getInternet().toString()

    }
    private fun isOnline(){
        if(checkInternet().equals("true")){
            Log.d("TAG", "isOnline: " + currentFragment.getCurrentFragment())
            if(currentFragment.getCurrentFragment().equals("spirit")){
                Navigation.findNavController(root).navigate(R.id.navigation_spirit)
            }
            else if(currentFragment.getCurrentFragment().equals("opportunity")){
                Navigation.findNavController(root).navigate(R.id.navigation_oppority)
            }
            else if(currentFragment.getCurrentFragment().equals("false")|| currentFragment.getCurrentFragment().equals("curiosity")){
                Navigation.findNavController(root).navigate(R.id.navigation_curiosity)
             //   Log.d(TAG, "isOnline: ")
            }

            }


        else{
            Handler().postDelayed({ this.isOnline() }, 1000)
        }
    }
}