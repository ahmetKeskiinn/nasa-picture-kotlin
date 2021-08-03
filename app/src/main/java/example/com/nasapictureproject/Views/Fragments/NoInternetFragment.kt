package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.CurrentFragment
import example.com.nasapictureproject.Utils.InternetSharedPref

class NoInternetFragment : Fragment() {
    private lateinit var root: View
    private lateinit var internet: InternetSharedPref
    private lateinit var currentFragment: CurrentFragment

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_no_internet, container, false)
        return root
    }

    override fun onResume() {
        initShared()
        super.onResume()
    }

    private fun initShared() {
        currentFragment = CurrentFragment()
        context?.let { currentFragment.instancePref(it) }
        internet = InternetSharedPref()
        context?.let { internet.instancePref(it) }
        isOnline()

    }

    private fun checkInternet(): String {
        return internet.getInternet().toString()

    }

    private fun isOnline() {
        if (checkInternet().equals("true")) {
            if (currentFragment.getCurrentFragment().equals("spirit")) {
                Navigation.findNavController(root).navigate(R.id.navigation_spirit)
            } else if (currentFragment.getCurrentFragment().equals("opportunity")) {
                Navigation.findNavController(root).navigate(R.id.navigation_oppority)
            } else if (currentFragment.getCurrentFragment().equals("false") || currentFragment.getCurrentFragment().equals("curiosity")) {
                Navigation.findNavController(root).navigate(R.id.navigation_curiosity)
            }

        } else {
            Handler().postDelayed({ this.isOnline() }, 1000)
        }
    }
}