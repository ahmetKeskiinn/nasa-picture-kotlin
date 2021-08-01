package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.CurrentFragment
import example.com.nasapictureproject.ViewModels.OppotunityViewModel

class OpporityFragment : Fragment() {

    private lateinit var vm: OppotunityViewModel
    private lateinit var sharedPref:CurrentFragment

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_oppority, container, false)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        initShared()
        initialUI()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initShared(){
        val sharedPref = CurrentFragment()
        context?.let { sharedPref.instancePref(it) }
        sharedPref.setCurrentFragment("oppority")
    }
    private fun initialUI(){
        vm = ViewModelProvider(this).get(OppotunityViewModel::class.java)
    }


}