package br.com.ronistone.marketlist.ui.purchase

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.databinding.FragmentPuchaseBinding

class PurchaseFragment : Fragment() {


    private var _binding: FragmentPuchaseBinding? = null
    private var navController: NavController? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PurchaseFragment()
    }

    private lateinit var viewModel: PuchaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val purchaseViewModel =
            ViewModelProvider(this).get(PuchaseViewModel::class.java)

        _binding = FragmentPuchaseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val marketName: TextView = binding.purchaseMarketName
        val purchaseDate: TextView = binding.purchaseDate
        val purchaseTotalSpent: TextView = binding.purchaseTotalSpent
        val purchaseTotalExpected: TextView = binding.purchaseTotalExpected

        val recyclerView: RecyclerView = binding.PurchaseList

        val adapter = PurchaseItemAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        purchaseViewModel.purchase.observe(viewLifecycleOwner) { purchase ->
            if(purchase == null) {
                goHome()
            } else {
                Log.i("PURCHASE", purchase.toString())
                marketName.text = purchase.market?.name
                val formatter = DateFormat.getLongDateFormat(context)
                val localDate = formatter.format(purchase.createdAt)
                purchaseDate.text = localDate
                val totalSpent = purchase.totalSpent / 100.0
                val totalExpected = purchase.totalExpected / 100.0
                purchaseTotalSpent.text = "Total Gasto: R$ %.2f".format(totalSpent)
                purchaseTotalExpected.text = "Total Esperado: R$ %.2f".format(totalExpected)
                purchase.items?.let { adapter.replaceItems(it) }
            }
        }

        val bundle = this.arguments

        val id = bundle?.getInt("purchaseId")

        if(id == null) {
            goHome()
        }

        purchaseViewModel.fetch(root, id!!)

        return root

    }

    private fun goHome() {
        navController?.navigate(R.id.action_nav_purchase_to_nav_home)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        Log.i("PURCHASE", "onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PuchaseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
