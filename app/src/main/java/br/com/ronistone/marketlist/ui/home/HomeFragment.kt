package br.com.ronistone.marketlist.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        val recycleView: RecyclerView = binding.PurchaseList
        val purchaseAdapter = PurchaseAdapter(emptyList())
        recycleView.layoutManager = LinearLayoutManager(activity)
        recycleView.adapter = purchaseAdapter

        homeViewModel.purchases.observe(viewLifecycleOwner) {
            Log.i("PURCHASE", "this is the purchase: $it")
            if(it.isEmpty()) {
                textView.text = homeViewModel.text.value
                textView.visibility = View.VISIBLE
                recycleView.visibility = View.GONE
            } else {
                textView.visibility = View.GONE
                recycleView.visibility = View.VISIBLE
                purchaseAdapter.replaceItems(it)
            }
        }
        homeViewModel.fetch(root)
        Log.i("HOME", "onCreateView")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("HOME", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i("HOME", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("HOME", "onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
