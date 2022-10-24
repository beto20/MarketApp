package com.alberto.market.marketapp.ui.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.ItemCategoryBinding
import com.alberto.market.marketapp.domain.Category
import com.squareup.picasso.Picasso

class CategoryAdapter constructor(
    var categories: List<Category> = listOf(),
    var itemClicked:(Category) -> Unit
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding: ItemCategoryBinding = ItemCategoryBinding.bind(itemView)

        fun bind(category: Category) {
            Picasso.get().load(category.cover)
                .error(R.drawable.splash_footer)
                .into(binding.imgCategory)

            binding.root.setOnClickListener {
                itemClicked(category)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}