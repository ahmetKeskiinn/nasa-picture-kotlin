package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import example.com.nasapictureproject.Adapters.CuriosityRecyclerAdapter
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.CurrentFragment
import example.com.nasapictureproject.ViewModels.CuriosityViewModel

class CuriosityFragment : Fragment() {
    private lateinit var sharedPref:CurrentFragment
    private var TAG = "CURIOSITY FRAGMENT"
    private lateinit var vm: CuriosityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var root: View


    //  private lateinit var navhostFragment: Fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_curiosity, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        initShared()
        initialUI()
        initRecycler()
        hookVM("curiosity", 1,10)
        super.onResume()
    }
    private fun initialUI() {
        vm =ViewModelProvider(this).get(CuriosityViewModel::class.java)
        recyclerView = root.findViewById(R.id.coriosityRecyclerView)
    }
    private fun hookVM(name:String,per_page:Int,page:Int){
        //The job will be done!
        vm.hook(name,per_page,page)
        vm.gameList.observe(this, Observer {
            Log.d("TAG", "hookVM: " + it)
        })
    }
    private fun initShared(){
        val sharedPref = CurrentFragment()
        context?.let { sharedPref.instancePref(it) }
        sharedPref.setCurrentFragment("curiosity")
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CuriosityRecyclerAdapter(getModels())

    }
    fun getModels(): MutableList<String> {
        val models = mutableListOf(
            "Çin", "Pekin"
        )
        return models
    }
}