package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.com.nasapictureproject.Adapters.CuriosityRecyclerAdapter
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.CurrentFragment
import example.com.nasapictureproject.ViewModels.CuriosityViewModel
import example.com.nasapictureproject.ViewModels.SpiritViewModel

class SpiritFragment : Fragment() {
    private var TAG = "SpiritFragment"
    private lateinit var vm: SpiritViewModel
    private lateinit var sharedPref:CurrentFragment
    private lateinit var root:View
    private lateinit var recyclerView:RecyclerView
    private lateinit var recyclerAdapter:CuriosityRecyclerAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_spirit, container, false)
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
        hookVM("spirit", 10,1)
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