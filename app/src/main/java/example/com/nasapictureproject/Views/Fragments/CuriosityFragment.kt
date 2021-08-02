package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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


class CuriosityFragment : Fragment() {
    private lateinit var sharedPref: CurrentFragment
    private var TAG = "CURIOSITY FRAGMENT"
    private lateinit var vm: CuriosityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var root: View
    private lateinit var recyclerAdapter: CuriosityRecyclerAdapter
    private lateinit var internet: InternetSharedPref
    private lateinit var internetImage: ImageView

    //  private lateinit var navhostFragment: Fragment
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
        if (internet.getInternet().equals("true")) {
            initShared()
            initialUI()
            hookVM("curiosity", 10, 1)
        } else {
            Toast.makeText(context, getString(R.string.noInternet), Toast.LENGTH_SHORT).show()
            Navigation.findNavController(root).navigate(R.id.noInternetFragment)
        }
        super.onResume()
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
            v = inflater.inflate(R.layout.fragment_curiosity, container, false)
            checkInternet()
        } else {
            v = inflater.inflate(R.layout.no_internet, container, false)
            checkInternet()
        }
        return v
    }


    private fun initialUI() {
        vm = ViewModelProvider(this).get(CuriosityViewModel::class.java)
        recyclerView = root.findViewById(R.id.coriosityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerAdapter = CuriosityRecyclerAdapter(context!!, activity!!)
        recyclerView.adapter = recyclerAdapter

    }

    private fun hookVM(name: String, per_page: Int, page: Int) {
        //The job will be done!
        vm.hook(name, per_page, page)
        vm.gameList.observe(this, Observer {
            Log.d(TAG, "hookVM: " + it)
            recyclerAdapter.submitList(it)
        })
    }

    private fun initShared() {
        val sharedPref = CurrentFragment()
        context?.let { sharedPref.instancePref(it) }
        sharedPref.setCurrentFragment("curiosity")
    }


}