package com.alberto.market.marketapp.ui.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.ItemProductBinding
import com.alberto.market.marketapp.domain.ProductResponseDto
import com.squareup.picasso.Picasso

class ProductAdapter constructor(
    var products: List<ProductResponseDto> = listOf(),
    val itemClicked: (ProductResponseDto) -> Unit,
): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding: ItemProductBinding = ItemProductBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(product: ProductResponseDto) = with(binding) {
            tvCode.text = "Cod: ${product.code}"
            tvDescription.text = product.description
            tvPrice.text = "S/ ${product.price}"

            Picasso.get()
                .load(product.image?.get(0))
                .error(R.drawable.empty)
                .into(imgProduct)

            root.setOnClickListener {
                itemClicked(product)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(products: List<ProductResponseDto>) {
        this.products = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }
}