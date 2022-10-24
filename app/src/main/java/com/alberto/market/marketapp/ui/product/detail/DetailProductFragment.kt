package com.alberto.market.marketapp.ui.product.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.core.BaseAdapter
import com.alberto.market.marketapp.data.server.ProductRequest
import com.alberto.market.marketapp.databinding.FragmentDetailProductBinding
import com.alberto.market.marketapp.databinding.ItemImageProductBinding
import com.alberto.market.marketapp.util.toast
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail_product.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductFragment : Fragment(R.layout.fragment_detail_product) {

    private lateinit var binding: FragmentDetailProductBinding
    private val viewModel: DetailProductViewModel by viewModels()
    private val safeArgs: DetailProductFragmentArgs by navArgs()

    private val adapter: BaseAdapter<String> = object : BaseAdapter<String>(emptyList()) {
        override fun getViewHolder(parent: ViewGroup): BaseViewHolder<String> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_product, parent, false)

            return object : BaseViewHolder<String>(view) {
                private val binding: ItemImageProductBinding = ItemImageProductBinding.bind(itemView)

                override fun bind(entity: String) = with(binding) {
                    Picasso.get()
                        .load(entity)
                        .error(R.drawable.empty)
                        .into(imgProduct)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailProductBinding.bind(view)

        setUpAdapter()
        init()
        setUpObservers()
        selectQuantity()
        addProduct()
    }

    private fun setUpAdapter() = with(binding) {
        rvProductsDetail.adapter = adapter
    }


    @SuppressLint("SetTextI18n")
    private fun selectQuantity() = with(binding) {
        var quantity = 0
        tvPlus.setOnClickListener {
            quantity++
            tvQuantity.text = "$quantity"
        }
        tvMinus.setOnClickListener {
            if (quantity > 0) {
                quantity--
                tvQuantity.text = "$quantity"
            }
            requireContext().toast("No puede elegir 0")
        }
    }

    private fun addProduct() = with(binding)  {

        btnAdd.setOnClickListener {

            val uuid = safeArgs.productResponseDto!!.uuid
            val categoryId = safeArgs.productResponseDto!!.categoryId
            val description = safeArgs.productResponseDto!!.description
            val code = safeArgs.productResponseDto!!.code
            val features = safeArgs.productResponseDto!!.features
            val price = safeArgs.productResponseDto!!.price
            val quantity = tvQuantity.text.toString().toInt()
            val image = safeArgs.productResponseDto!!.image!![0]
            val totalAmount = price * quantity

            val productRequest = ProductRequest(uuid, categoryId,description, code, features, price, quantity, totalAmount, image)
            viewModel.addProduct(productRequest)
            requireContext().toast("Producto agregado al carrito")
        }
    }


    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(state: DetailProductViewModel.DetailProductState) {
        when(state) {
            DetailProductViewModel.DetailProductState.Init -> Unit
            is DetailProductViewModel.DetailProductState.Error -> requireContext().toast(state.message)
            is DetailProductViewModel.DetailProductState.IsLoading -> showProgress(state.isLoading)
        }
    }


    private fun showProgress(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun init() = with(binding) {
        safeArgs.productResponseDto?.let { product ->

            tvProductName.text = product.description
            tvProductPrice.text = "$/ ${product.price}"
            tvProductFeatures.text = product.features

            Picasso.get()
                .load(product.image?.get(0))
                .error(R.drawable.empty)
                .into(imgDetail)

            adapter.update(product.image!!)
        }
    }

}