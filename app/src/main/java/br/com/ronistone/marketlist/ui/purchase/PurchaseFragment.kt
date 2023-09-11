package br.com.ronistone.marketlist.ui.purchase

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.databinding.FragmentHomeBinding
import br.com.ronistone.marketlist.databinding.FragmentPuchaseBinding
import br.com.ronistone.marketlist.ui.home.HomeViewModel

class PurchaseFragment : Fragment() {


    private var _binding: FragmentPuchaseBinding? = null

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




        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PuchaseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
