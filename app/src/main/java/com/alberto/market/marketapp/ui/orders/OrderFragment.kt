package com.alberto.market.marketapp.ui.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.FragmentOrderBinding
import com.alberto.market.marketapp.domain.ProductDto
import com.alberto.market.marketapp.domain.ProductsDto
import com.alberto.market.marketapp.domain.ProductsPaymentDto
import com.alberto.market.marketapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order) {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderBinding.bind(view)

        init()
        setUpAdapter()
        setUpObserver()

    }

    private fun init() {
        viewModel.getProductsOrder()
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(state: OrderViewModel.OrderState) {
        when (state) {
            OrderViewModel.OrderState.Init -> Unit
            is OrderViewModel.OrderState.Error -> requireContext().toast(state.message)
            is OrderViewModel.OrderState.IsLoading -> showProgress(state.isLoading)
            is OrderViewModel.OrderState.Success -> {
                calculateOrderAmount(state.products)
                changeView(state.products)
                orderAdapter.updateList(state.products)
            }
        }
    }

    private fun calculateOrderAmount(productsAmount: List<ProductDto>) {
        var totalAmount = 0.00
        productsAmount.forEach {
            totalAmount += it.totalAmount
        }
        binding.tvTotalAmountOrder.text = totalAmount.toString()
    }

    private fun showProgress(isLoading: Boolean) {
        TODO()
    }

    private fun changeView(products: List<ProductDto>) {

        val productsListDto: MutableList<ProductsDto> = arrayListOf()
        // TODO:: Check this foreach
        products.forEach {
            val productsDto = ProductsDto()
            productsDto.uuid = it.uuid
            productsDto.categoryId = it.categoryId
            productsDto.quantity = it.quantity

            productsListDto.add(productsDto)
        }

        val productsPaymentDto = ProductsPaymentDto(productsListDto, products[0].totalAmount)

        binding.btnCheckIn.setOnClickListener {
            orderAdapter.changeViewToPayment(productsPaymentDto)
        }
    }

    private fun setUpAdapter() {
        orderAdapter = OrderAdapter { productDto ->
            val directions = OrderFragmentDirections.actionOrderFragmentToPaymentFragment(productDto)
            Navigation.findNavController(binding.root).navigate(directions)
        }

        binding.rvOrder.adapter = orderAdapter
    }

}