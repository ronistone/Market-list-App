package br.com.ronistone.marketlist.ui.createPurchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.databinding.FragmentCreatePurchaseBinding
import br.com.ronistone.marketlist.model.Market

class CreatePurchaseFragment : Fragment() {

    private var _binding: FragmentCreatePurchaseBinding? = null
    private var navController: NavController? = null
    private var createPurchaseViewModel : CreatePurchaseViewModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        createPurchaseViewModel =
                ViewModelProvider(this).get(CreatePurchaseViewModel::class.java)

        _binding = FragmentCreatePurchaseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinnerMarketList = binding.marketList
        val createPurchaseButton = binding.createPurchaseButton
        val createMarketButton = binding.purchaseCreateMarketButton
        val marketListAdapter = container?.context?.let {
            CreatePurchaseMarketSpinnerAdapter(it, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        }
        marketListAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMarketList.adapter = marketListAdapter

        createPurchaseViewModel!!.markets.observe(viewLifecycleOwner) {
            if(it == null || it.isEmpty()) {
                Log.i("MARKET ITEMS", "null value")
                marketListAdapter?.replaceEmpty()
                spinnerMarketList.isActivated = false
            } else {
                Log.i("MARKET ITEMS", it.toString())
                val holders = it.map { it.toHolder() }
                marketListAdapter?.replaceItems(holders)
                marketListAdapter?.notifyDataSetChanged()
                spinnerMarketList.isActivated = true
            }
        }

        spinnerMarketList.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                createPurchaseViewModel!!.selectedMarket.postValue(parent?.getItemAtPosition(position) as Market?)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                if(createPurchaseViewModel!!.markets.value != null && !createPurchaseViewModel!!.markets.value!!.isEmpty()) {
                  spinnerMarketList.prompt = "Selecione um mercado!"
                }
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

        createPurchaseViewModel!!.loadMarkets(root)
        Log.i("CREATE PURCHASE", "onCreateView")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController(view)
        Log.i("CREATE PURCHASE", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i("CREATE PURCHASE", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("CREATE PURCHASE", "onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
