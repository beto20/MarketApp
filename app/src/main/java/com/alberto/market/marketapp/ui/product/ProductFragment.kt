package com.alberto.market.marketapp.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.FragmentProductBinding
import com.alberto.market.marketapp.util.toast
import com.alberto.market.marketapp.ui.common.gone
import com.alberto.market.marketapp.ui.common.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var binding : FragmentProductBinding
    private val viewModel: ProductViewModel by viewModels()
    private val safeArgs: ProductFragmentArgs by navArgs()


    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(itemClicked = { product ->
            val directions = ProductFragmentDirections.actionProductFragmentToDetailProductFragment(product)
            Navigation.findNavController(binding.root).navigate(directions)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductBinding.bind(view)

        init()
        setUpAdapter()
        collectData()
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(state: ProductViewModel.ProductState) {
        when(state) {
            ProductViewModel.ProductState.Init -> Unit
            is ProductViewModel.ProductState.IsLoading -> handleLoading(state.isLoading)
            is ProductViewModel.ProductState.Success -> {
                productAdapter.updateList(state.products)
            }
            is ProductViewModel.ProductState.Error -> requireContext().toast(state.rawResponse)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visible()
        else binding.progressBar.gone()
    }

    private fun setUpAdapter() {
        binding.rvProducts.adapter = productAdapter
    }

    private fun init() {
        val categoryId = safeArgs.category.uuid
        viewModel.getProducts(categoryId)
    }
}