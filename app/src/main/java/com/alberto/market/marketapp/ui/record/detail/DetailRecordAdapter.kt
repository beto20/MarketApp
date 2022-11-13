package com.alberto.market.marketapp.ui.record.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.ItemRecordDetailBinding
import com.alberto.market.marketapp.domain.ProductDescDto

class DetailRecordAdapter constructor(
    var products: List<ProductDescDto>
) : RecyclerView.Adapter<DetailRecordAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemRecordDetailBinding = ItemRecordDetailBinding.bind(itemView)

        fun bind(productDescDto: ProductDescDto) = with(binding) {
            tvProductDescription.text = productDescDto.description
            tvProductPrice.text = productDescDto.price.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(productsRecord: List<ProductDescDto>) {
        this.products = productsRecord
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_record_detail, parent, false)
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