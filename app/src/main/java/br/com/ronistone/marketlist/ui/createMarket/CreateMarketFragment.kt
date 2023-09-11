package br.com.ronistone.marketlist.ui.createMarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.ronistone.marketlist.R
import br.com.ronistone.marketlist.databinding.FragmentCreateMarketBinding

class CreateMarketFragment : Fragment() {

    private var _binding: FragmentCreateMarketBinding? = null
    private var navController: NavController? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val createMarketViewModel =
                ViewModelProvider(this).get(CreateMarketViewModel::class.java)

        _binding = FragmentCreateMarketBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: EditText = binding.createMarketMarketName
        val createButton: Button = binding.createMarketButton

//        createMarketViewModel.marketName.observe(viewLifecycleOwner) {
//        }

        textView.doOnTextChanged { text, _, _, _ ->
            createMarketViewModel.updateName(text.toString())
        }

        createButton.setOnClickListener {
            container?.context?.let { context -> createMarketViewModel.create(context, root) {
                    navController?.navigate(R.id.action_nav_create_market_to_nav_create_purchase2)
                }
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
