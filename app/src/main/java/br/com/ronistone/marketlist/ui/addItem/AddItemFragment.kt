package br.com.ronistone.marketlist.ui.addItem

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.ronistone.marketlist.MainActivity
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.adapter.ProductNameArrayAdapter
import br.com.ronistone.marketlist.databinding.FragmentAddItemBinding
import br.com.ronistone.marketlist.model.Product
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import com.google.android.material.snackbar.Snackbar
import java.lang.Double.parseDouble

class AddItemFragment : Fragment() {


    private lateinit var pricePerUnitText: TextView
    private lateinit var clearButton: Button
    private lateinit var cameraButton: Button
    private lateinit var itemPrice: EditText
    private lateinit var addButton: Button
    private lateinit var itemQuantity: EditText
    private lateinit var productUnit: EditText
    private lateinit var productSize: EditText
    private lateinit var productEan: EditText
    private lateinit var productName: AutoCompleteTextView
    private var _binding: FragmentAddItemBinding? = null
    private var navController: NavController? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: AddItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(AddItemViewModel::class.java)

        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        productName = binding.productName
        productEan = binding.productEan
        productSize = binding.productSize
        productUnit = binding.productUnit
        itemQuantity = binding.itemQuantity
        itemPrice = binding.itemPrice
        addButton = binding.purchaseItemAdd
        cameraButton = binding.productEanCameraOpen
        clearButton = binding.purchaseItemClearForm
        pricePerUnitText = binding.pricePerUnit

        val productsNameAdapter = ProductNameArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf())
        productName.setAdapter(productsNameAdapter)
        productName.threshold

        productName.doOnTextChanged { text, _, _, _ ->
            if(text?.length != null && text.length > 2) {
                viewModel.queryName(root, text.toString())
            }
        }

        productName.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, _ ->
            val product = productsNameAdapter.getItem(position)
            viewModel.selectProduct(product!!)
        }

        cameraButton.setOnClickListener {
            setFragmentResultListener("EAN_KEY") { key, bundle ->
                val result = bundle.getString("EAN_RESULT")
                viewModel.queryEan(root, result!!) {
                    productEan.setText(result)
                }
            }
            navController?.navigate(R.id.action_nav_add_item_to_nav_camera_barcode)
        }

        productSize.doOnTextChanged { _, _, _, _ -> updatePricePerUnit() }
        productUnit.doOnTextChanged { _, _, _, _ -> updatePricePerUnit() }
        itemPrice.doOnTextChanged { _, _, _, _ -> updatePricePerUnit() }

        addButton.setOnClickListener {
            val isValid = validateForm(root)
            if (isValid) {
                val item = viewModel.selectedProduct.value
                val purchaseItem = PurchaseItem(
                    id = item?.id,
                    price = (if(itemPrice.text.isNotBlank()){
                        (parseDouble(itemPrice.text.toString()) * 100).toInt()
                    } else {
                        null
                    }),

                    product = Product(
                        id = item?.product?.id,
                        ean = if (productEan.text.isBlank()) null else productEan.text?.toString(),
                        name = productName.text.toString(),
                        size = Integer.parseInt(productSize.text.toString()),
                        unit = productUnit.text.toString()
                    ),
                    quantity = Integer.parseInt(itemQuantity.text.toString())

                )
                if(viewModel.isEdition) {
                    viewModel.updateItem(root, purchaseItem, viewModel.purchaseId.value!!){
                        goPurchase()
                    }
                } else {
                    viewModel.addItem(root, purchaseItem, viewModel.purchaseId.value!!) {
                        goPurchase()
                    }
                }
            }
        }

        clearButton.setOnClickListener {
            viewModel.selectedProduct.postValue(null)
        }

        viewModel.productsSearch.observe(viewLifecycleOwner) {
            if(it != null) {
                productsNameAdapter.update(it.toMutableList())
            } else {
                productsNameAdapter.update(mutableListOf())
            }
        }

        viewModel.selectedProduct.observe(viewLifecycleOwner) {
            if(viewModel.isEdition) {
                addButton.text = getString(R.string.button_update)
            }
            if(it != null) {
                productName.setText(it.product.name)
                productEan.setText(it.product.ean)
                productSize.setText(it.product.size?.toString())
                productUnit.setText(it.product.unit)
                itemQuantity.setText(it.quantity.toString())
                it.price?.let { it1 -> processPrice(it1) }
                itemQuantity.requestFocus()
            } else {
                productName.setText("")
                productEan.setText("")
                productSize.setText("")
                productUnit.setText("")
                itemQuantity.setText("")
                itemPrice.setText("")
            }
        }


        return root
    }

    private fun processPrice(it1: Int) {
        var value: Double = it1.toDouble()
        if (it1 != 0) {
            value = it1 / 100.0
        }
        itemPrice.setText(value.toString())
    }

    private fun convertValue(value: String): Double? {
        return try {
            parseDouble(value)
        } catch (e: Exception) {
            null
        }
    }
    private fun updatePricePerUnit() {
        var price: String = itemPrice.text.toString()
        var unit: String = productSize.text.toString()
        var unitMetric: String = productUnit.text.toString()

        var unitConverted = convertValue(unit)
        unit = if(unitConverted != null) {
            "1"
        } else {
            unitConverted = 1.0
            "--"
        }

        val priceConverted = convertValue(price)
        price = if(priceConverted != null) {
            String.format("%.6f", priceConverted/unitConverted)
        } else {
            "--"
        }

        unitMetric = unitMetric.ifBlank {
            "--"
        }

        pricePerUnitText.text = getString(R.string.price_per_unit, price, unit, unitMetric)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val bundle = this.arguments

        val purchaseId = bundle?.getInt("purchaseId")
        viewModel.purchaseId.postValue(purchaseId)

        if(purchaseId == null) {
            goPurchase()
        }

        viewModel.purchaseItemId.observe(viewLifecycleOwner) {
            if(it != null && !viewModel.selectedProduct.isInitialized) {
                viewModel.fetchItem(view, purchaseId!!, it)
            }
        }

        if(bundle?.containsKey("purchaseItemId") == true) {
            viewModel.purchaseItemId.postValue(bundle.getInt("purchaseItemId"))
            val mainActivity = activity as MainActivity
            val toolbar = mainActivity.findViewById<Toolbar>(R.id.toolbar)
            toolbar.title = resources.getString(R.string.menu_update_item)
        }


    }

    private fun goPurchase() {
        navController?.navigateUp()
    }

    private fun validateForm(root: View): Boolean {
        val fields = ArrayList<String>()
        if (productName.text.isBlank()) {
            fields.add("Nome do Produto")
            productName.error = getString(R.string.required_field)
        }
        if (productSize.text.isBlank()) {
            fields.add("Tamanho do Produto")
            productSize.error = getString(R.string.required_field)
        }
        if (productUnit.text.isBlank()) {
            fields.add("Unidade do Tamanho do Produto")
            productUnit.error = getString(R.string.required_field)
        }
        if (itemQuantity.text.isBlank()) {
            fields.add("Quantidade de Produtos Pegos")
            itemQuantity.error = getString(R.string.required_field)
        }
        if (fields.isNotEmpty()) {
            val fieldsMsg = if (fields.size > 1) {
                fields.joinToString(prefix = "os ")
            } else {
                fields.joinToString(prefix = "o ")
            }
            Snackbar.make(root, "Você precisa preencher $fieldsMsg!", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

}
