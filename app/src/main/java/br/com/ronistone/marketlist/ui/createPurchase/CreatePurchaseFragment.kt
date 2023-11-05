package br.com.ronistone.marketlist.ui.createPurchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.GONE
import android.widget.AdapterView.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.adapter.ItemHolder
import br.com.ronistone.marketlist.adapter.ListItemDetailsLookup
import br.com.ronistone.marketlist.adapter.ItemKeyProvider
import br.com.ronistone.marketlist.databinding.FragmentCreatePurchaseBinding
import br.com.ronistone.marketlist.helper.ItemClickSupport

class CreatePurchaseFragment : Fragment() {

    private var marketListAdapter: CreatePurchaseMarketAdapter? = null
    private var marketListView: RecyclerView? = null
    private var _binding: FragmentCreatePurchaseBinding? = null
    private var navController: NavController? = null
    private var createPurchaseViewModel : CreatePurchaseViewModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tracker: SelectionTracker<String>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        createPurchaseViewModel =
                ViewModelProvider(this).get(CreatePurchaseViewModel::class.java)

        _binding = FragmentCreatePurchaseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val emptyMarketText = binding.emptyMarket
        val createPurchaseButton = binding.createPurchaseButton
        val createMarketButton = binding.purchaseCreateMarketButton
        marketListView = binding.marketList
        marketListAdapter = CreatePurchaseMarketAdapter(emptyList(), createPurchaseViewModel!!)

        marketListView?.layoutManager = LinearLayoutManager(activity)
        marketListView?.adapter = marketListAdapter

        ItemClickSupport.addTo(marketListView!!).setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClicked(recyclerView: RecyclerView?, position: Int, v: View?) {
                marketListAdapter!!.onSelect(position)
            }
        })

        createPurchaseViewModel!!.markets.observe(viewLifecycleOwner) {
            if(it == null || it.isEmpty()) {
                marketListView?.visibility = GONE
                emptyMarketText.visibility = VISIBLE
            } else {
                marketListView?.visibility = VISIBLE
                emptyMarketText.visibility = GONE
                marketListAdapter!!.replaceItems(it)
            }
        }

        createPurchaseButton.setOnClickListener {
            context?.let { context -> createPurchaseViewModel!!.createPurchase(context, root) {
                    navController?.navigate(R.id.action_nav_create_purchase_to_nav_home)
                }
            }
        }

        createMarketButton.setOnClickListener {
            navController?.navigate(R.id.action_nav_create_purchase_to_nav_create_market)
        }

        setupSelectionTracker()
        createPurchaseViewModel!!.loadMarkets(root)
        Log.i("CREATE PURCHASE", "onCreateView")

        return root
    }

    private fun setupSelectionTracker() {
        tracker = createTracker()
        marketListAdapter?.tracker = tracker
    }

    private fun createTracker() = SelectionTracker.Builder(
        "selectionMarket",
        binding.marketList,
        ItemKeyProvider(marketListAdapter as ListAdapter<ItemHolder, *>),
        ListItemDetailsLookup(binding.marketList),
        StorageStrategy.createStringStorage()
    ).withSelectionPredicate(
        SelectionPredicates.createSelectAnything()
    ).build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController(view)
        Log.i("CREATE PURCHASE", "onViewCreated")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
