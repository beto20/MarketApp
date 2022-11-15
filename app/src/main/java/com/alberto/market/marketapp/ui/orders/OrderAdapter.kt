package com.alberto.market.marketapp.ui.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.ItemProductOrderBinding
import com.alberto.market.marketapp.domain.ProductDto
import com.alberto.market.marketapp.domain.ProductResponseDto
import com.alberto.market.marketapp.domain.ProductsPaymentDto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderAdapter constructor(
    var itemClickedDelete: (ProductDto) -> Unit,
    var itemClickedEdit: (ProductResponseDto) -> Unit,
    private var orderProducts: List<ProductDto>,
    val passOrderData: (ProductsPaymentDto) -> Unit,

    ) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemProductOrderBinding = ItemProductOrderBinding.bind(itemView)

        fun bind(productDto: ProductDto) = with(binding) {
            tvTitleOrder.text = productDto.description
            tvOrderQuantity.text = productDto.quantity.toString()
            tvOrderTotal.text = productDto.totalAmount.toString()

            Picasso.get()
                .load(productDto.image)
                .error(R.drawable.empty)
                .into(imgOrder)

            imgDelete.setOnClickListener {
                itemClickedDelete(productDto)
            }

            imgEdit.setOnClickListener {
                itemClickedEdit(toProductResponseDto(productDto))
            }
        }

        fun toProductResponseDto(productDto: ProductDto): ProductResponseDto {
            return ProductResponseDto(
                productDto.uuid,
                productDto.categoryId,
                productDto.description,
                productDto.code,
                productDto.features,
                productDto.price,
                0,
                listOf(productDto.image),
                productDto.quantity
            )
        }
    }

    fun changeViewToPayment(productsPaymentDto: ProductsPaymentDto) {
        passOrderData(productsPaymentDto)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(orderProducts: List<ProductDto>) {
        this.orderProducts = orderProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_product_order, parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = orderProducts[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return orderProducts.size
    }
}