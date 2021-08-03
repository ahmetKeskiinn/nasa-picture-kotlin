package example.com.nasapictureproject.Views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import example.com.nasapictureproject.ViewModels.OppotunityViewModel

class OpporityFragment : Fragment() {
    private var TAG = "OpporityFragment"
    private lateinit var vm: OppotunityViewModel
    private lateinit var sharedPref: CurrentFragment
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CuriosityRecyclerAdapter
    private lateinit var root: View
    private lateinit var internet: InternetSharedPref

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = isOnline(inflater, container, savedInstanceState)
        return root
    }

    override fun onResume() {
        if (internet.getInternet().equals("true")) {
            initShared()
            initialUI()
            hookVM("opportunity", 10, 1)
        } else {
            Toast.makeText(context, getString(R.string.noInternet), Toast.LENGTH_SHORT).show()
            Navigation.findNavController(root).navigate(R.id.noInternetFragment)
        }
        super.onResume()
    }

    private fun initShared() {
        val sharedPref = CurrentFragment()
        context?.let { sharedPref.instancePref(it) }
        sharedPref.setCurrentFragment("oppority")
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
            v = inflater.inflate(R.layout.fragment_oppority, container, false)
            checkInternet()
        } else {
            v = inflater.inflate(R.layout.no_internet, container, false)
            checkInternet()
        }
        return v
    }

    private fun initialUI() {
        vm = ViewModelProvider(this).get(OppotunityViewModel::class.java)
        recyclerView = root.findViewById(R.id.opporityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerAdapter = CuriosityRecyclerAdapter(context!!, activity!!)
        recyclerView.adapter = recyclerAdapter
    }

    private fun hookVM(name: String, per_page: Int, page: Int) {
        vm.hook(name, per_page, page)
        vm.list.observe(this, Observer {
            recyclerAdapter.submitList(it)
        })
    }

}