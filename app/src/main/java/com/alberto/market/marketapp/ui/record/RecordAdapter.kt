package com.alberto.market.marketapp.ui.record

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.ItemRecordBinding
import com.alberto.market.marketapp.domain.Record

class RecordAdapter constructor(
    var records: List<Record> = listOf(),
    val itemClicked: (Record) -> Unit,
): RecyclerView.Adapter<RecordAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding: ItemRecordBinding = ItemRecordBinding.bind(itemView)

        fun bind(record: Record) = with(binding) {
            var totalQuantity = 0

            record.product.forEach {
                totalQuantity += it.quantity
            }
            tvOrderNumberRecord.text = record.correlative
            tvQuantityRecord.text = totalQuantity.toString()
            tvTotalAmountRecord.text = record.totalAmount.toString()
            tvOrderDate.text = record.date

            root.setOnClickListener {
                itemClicked(record)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(records: List<Record>) {
        this.records = records
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int {
        return records.size
    }
}