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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.adapter.ProductNameArrayAdapter
import br.com.ronistone.marketlist.databinding.FragmentAddItemBinding
import br.com.ronistone.marketlist.model.Product
import br.com.ronistone.marketlist.model.ProductInstance
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import com.google.android.material.snackbar.Snackbar
import java.lang.Double.parseDouble

class AddItemFragment : Fragment() {

    private lateinit var clearButton: Button
    private lateinit var cameraButton: Button
    private var purchaseId: Int? = null
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
            viewModel.selectedProduct.postValue(product)
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

        addButton.setOnClickListener {
            val isValid = validateForm(root)
            if (isValid) {
                val purchaseItem = PurchaseItem(
                    purchase = Purchase(
                        id = purchaseId
                    ),
                    productInstance = ProductInstance(
                        price = (if(itemPrice.text.isNotBlank()){
                            (parseDouble(itemPrice.text.toString()) * 100).toInt()
                        } else {
                            null
                        }),

                        product = Product(
                            ean = productEan.text?.toString(),
                            name = productName.text.toString(),
                            size = Integer.parseInt(productSize.text.toString()),
                            unit = productUnit.text.toString()
                        )
                    ),
                    quantity = Integer.parseInt(itemQuantity.text.toString())

                )
                viewModel.addItem(root, purchaseItem){
                    goPurchase()
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
            if(it != null) {
                productName.setText(it.name)
                productEan.setText(it.ean)
                productSize.setText(it.size?.toString())
                productUnit.setText(it.unit)
                itemQuantity.requestFocus()
            } else {
                productName.setText("")
                productEan.setText("")
                productSize.setText("")
                productUnit.setText("")
            }
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val bundle = this.arguments

        purchaseId = bundle?.getInt("purchaseId")

        if(purchaseId == null) {
            goPurchase()
        }
    }

    private fun goPurchase() {
        navController?.navigateUp()
    }

    private fun validateForm(root: View): Boolean {
        val fields = ArrayList<String>()
        if (productName.text.isBlank()) {
            fields.add("Nome do Produto")
            productName.error = "Obrigatório"
        }
        if (productSize.text.isBlank()) {
            fields.add("Tamanho do Produto")
            productSize.error = "Obrigatório"
        }
        if (productUnit.text.isBlank()) {
            fields.add("Unidade do Tamanho do Produto")
            productUnit.error = "Obrigatório"
        }
        if (itemQuantity.text.isBlank()) {
            fields.add("Quantidade de Produtos Pegos")
            itemQuantity.error = "Obrigatório"
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
