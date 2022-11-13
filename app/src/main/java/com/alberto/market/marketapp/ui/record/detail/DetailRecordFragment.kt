package com.alberto.market.marketapp.ui.record.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.FragmentDetailRecordBinding
import com.alberto.market.marketapp.domain.ProductDescDto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailRecordFragment : Fragment(R.layout.fragment_detail_record) {

    private lateinit var binding: FragmentDetailRecordBinding
    private val safeArgs: DetailRecordFragmentArgs by navArgs()

    private val detailRecordAdapter: DetailRecordAdapter by lazy {
        DetailRecordAdapter(emptyList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailRecordBinding.bind(view)

        init()
        setUpAdapter()
    }

    private fun init() = with(binding) {
        safeArgs.record.let { record ->

            tvStatus.text = status(record.status)
            tvOrderNumberDetail.text = record.correlative
            tvLocationType.text = locationType(record.addressDelivery.type)
            tvQuantityDetail.text = productQuantity(record.product)
            tvPurchaseDate.text = record.date
            tvSendDate.text = record.date
            tvSendHour.text = record.hour
            tvPaymentTypeDetail.text = paymentType(record.paymentType.type)
            tvTotalPaymentDetail.text = record.paymentType.amount.toString()
            tvSubtotal.text = record.totalAmount.toString()
            tvTotalAmountOrderDetail.text = deliveryIncrement(record.totalAmount)

            detailRecordAdapter.updateList(record.product)
        }
    }

    private fun setUpAdapter() = with(binding) {
        rvOrderDetail.adapter = detailRecordAdapter
    }

    private fun productQuantity(products: List<ProductDescDto>): String {
        var quantity = 0
        products.forEach {
            quantity = it.quantity
        }
        return quantity.toString()
    }

    private fun deliveryIncrement(subTotal: Double): String {
        val total =  subTotal + 20;
        return total.toString()
    }

    private fun status(status: Int): String {
        var statusResponse = "Sin data"

        when(status) {
            1 -> statusResponse = "Pendiente"
            2 -> statusResponse = "Atendido/Finalizado"
            -1 -> statusResponse = "Rechazado"
        }
        return  statusResponse
    }

    private fun locationType(type: Int): String {
        var location = "Sin data"

        when(type) {
            1 -> location = "Casa"
            2 -> location = "Oficina"
            3 -> location = "Otro"
        }
        return  location
    }

    private fun paymentType(type: Int): String {
        var payment = "Sin data"

        when(type) {
            1 -> payment = "Yape"
            2 -> payment = "Plin"
            3 -> payment = "Efectivo"
        }
        return  payment
    }
}