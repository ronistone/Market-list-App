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
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.MainActivity
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.adapter.PurchaseItemDetailsLookup
import br.com.ronistone.marketlist.adapter.PurchaseItemKeyProvider
import br.com.ronistone.marketlist.databinding.FragmentPuchaseBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PurchaseFragment : Fragment() {


    private var purchaseId: Int? = null
    private var fab: FloatingActionButton? = null
    private var deleteToolbarButton: ActionMenuItemView? = null
    private var toolbar: Toolbar? = null
    private lateinit var adapter: PurchaseItemAdapter
    private var _binding: FragmentPuchaseBinding? = null
    private var navController: NavController? = null
    private var title: String? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private lateinit var tracker: SelectionTracker<String>

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
//        val marketName: TextView = binding.purchaseMarketName
//        val purchaseDate: TextView = binding.purchaseDate
        val purchaseTotalSpent: TextView = binding.purchaseTotalSpent
        val purchaseTotalExpected: TextView = binding.purchaseTotalExpected

        recyclerView = binding.PurchaseList

        adapter = PurchaseItemAdapter(purchaseViewModel, emptyList())
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(activity)

        purchaseViewModel.purchase.observe(viewLifecycleOwner) { purchase ->
            if(purchase == null) {
                goHome()
            } else {
                Log.i("PURCHASE", purchase.toString())
                val formatter = DateFormat.getLongDateFormat(context)
                val localDate = formatter.format(purchase.createdAt)
                title = "${purchase.market?.name} $localDate"
                setTitle()
                val totalSpent = purchase.totalSpent / 100.0
                val totalExpected = purchase.totalExpected / 100.0
                purchaseTotalSpent.text = "Valor Gasto: R$ %.2f".format(totalSpent)
                purchaseTotalExpected.text = "Valor Esperado: R$ %.2f".format(totalExpected)
                purchase.items?.let { adapter.submitList(it) }
            }
        }

        if (toolbar == null || deleteToolbarButton == null) {
            setupToolbar()
        }
        setupSelectionTracker()


        val bundle = this.arguments

        purchaseId = bundle?.getInt("purchaseId")

        if(purchaseId == null) {
            goHome()
        }

        purchaseViewModel.fetch(root, purchaseId!!)

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


    private fun setupSelectionTracker() {
        tracker = createTracker()
        tracker.addObserver(trackerSelectionObserver)
        adapter.tracker = tracker
    }

    private fun createTracker() = SelectionTracker.Builder(
        "selectionItem",
        binding.PurchaseList,
        PurchaseItemKeyProvider(adapter!!),
        PurchaseItemDetailsLookup(binding.PurchaseList),
        StorageStrategy.createStringStorage()
    ).withSelectionPredicate(
        SelectionPredicates.createSelectAnything()
    ).build()

    private val trackerSelectionObserver = object : SelectionTracker.SelectionObserver<String>() {
        override fun onSelectionChanged() {
            super.onSelectionChanged()
            if (toolbar == null || deleteToolbarButton == null) {
                setupToolbar()
            }
            val items = tracker.selection.size()
            if (items > 0) {
                toolbar?.title = getString(R.string.action_selected, items)
                deleteToolbarButton?.visibility = View.VISIBLE
            } else {
                setTitle()

                deleteToolbarButton?.visibility = View.INVISIBLE
            }
        }
    }

    private fun setTitle() {
        if (title != null) {
            toolbar?.title = title
        } else {
            toolbar?.title = getString(R.string.menu_purchase)
        }
    }

    private fun setupToolbar() {
        val mainActivity = activity as MainActivity
        toolbar = mainActivity.findViewById(R.id.toolbar)
        deleteToolbarButton = mainActivity.findViewById(R.id.action_delete)
        fab = mainActivity.findViewById(R.id.fab)
        fab?.show()
        fab?.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putInt("purchaseId", viewModel.purchase.value?.id!!)
            navController?.navigate(R.id.action_nav_purchase_to_nav_add_item, bundle)
        }
        deleteToolbarButton?.setOnClickListener {
            adapter!!.currentList.filter {
                tracker.selection.contains(it.id.toString())
            }.forEach {item ->
                Log.i("TOOLBAR", "REMOVE ITEM ${item.id}")
                viewModel.removeItem(it, item)
            }
        }
    }

}
