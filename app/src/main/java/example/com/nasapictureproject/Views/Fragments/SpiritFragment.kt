package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.com.nasapictureproject.Adapters.CuriosityRecyclerAdapter
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.CurrentFragment
import example.com.nasapictureproject.Utils.InternetSharedPref
import example.com.nasapictureproject.ViewModels.CuriosityViewModel
import example.com.nasapictureproject.ViewModels.SpiritViewModel

class SpiritFragment : Fragment() {
    private var TAG = "SpiritFragment"
    private lateinit var vm: SpiritViewModel
    private lateinit var sharedPref:CurrentFragment
    private lateinit var root:View
    private lateinit var recyclerView:RecyclerView
    private lateinit var internet: InternetSharedPref
    private lateinit var recyclerAdapter:CuriosityRecyclerAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = isOnline(inflater, container, savedInstanceState)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onResume() {
        if (internet.getInternet().equals("true")) {
            initShared()
            initialUI()
            hookVM("spirit", 10, 1)
        } else {
            Toast.makeText(context, getString(R.string.noInternet), Toast.LENGTH_SHORT).show()
            Navigation.findNavController(root).navigate(R.id.noInternetFragment)
        }
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
        sharedPref.setCurrentFragment("spirit")
    }

    private fun checkInternet(): String {
        internet = InternetSharedPref()
        context?.let { internet.instancePref(it) }
        return internet.getInternet().toString()
    }

    private fun isOnline(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val v: View
        if (checkInternet().equals("true")) {
            v = inflater.inflate(R.layout.fragment_spirit, container, false)
            checkInternet()
        } else {
            v = inflater.inflate(R.layout.no_internet, container, false)
            checkInternet()
        }
        return v
    }
    private fun initialUI() {
        vm = ViewModelProvider(this).get(SpiritViewModel::class.java)
        recyclerView = root.findViewById(R.id.spiritRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerAdapter = CuriosityRecyclerAdapter(context!!, activity!!)
        recyclerView.adapter = recyclerAdapter
    }
    private fun hookVM(name:String,per_page:Int,page:Int){
        //The job will be done!
        vm.hook(name,per_page,page)
        vm.gameList.observe(this, Observer {
            Log.d(TAG, "hookVM: " + it)
            recyclerAdapter.submitList(it)
        })
    }
}