package com.alberto.market.marketapp.ui.payment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.data.server.AddressRequest
import com.alberto.market.marketapp.data.server.OrderRequest
import com.alberto.market.marketapp.data.server.PaymentMethodRequest
import com.alberto.market.marketapp.data.server.PaymentRequest
import com.alberto.market.marketapp.databinding.DialogPaymentConfirmationBinding
import com.alberto.market.marketapp.databinding.FragmentPaymentBinding
import com.alberto.market.marketapp.ui.common.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private lateinit var binding: FragmentPaymentBinding
    private val viewModel: PaymentViewModel by viewModels()
    private val safeArgs: PaymentFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymentBinding.bind(view)

        setUpObserver()

        binding.include.imgBackToOrder.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnPayment.setOnClickListener {
            paymentProcess()
        }
    }

    private fun paymentProcess() = with(binding) {
        // Address
        val addressRequest = AddressRequest()

        if (rbHome.isChecked) addressRequest.type = 1
        if (rbOffice.isChecked) addressRequest.type = 2
        if (rbOther.isChecked) addressRequest.type = 3

        addressRequest.address = edtAddress.text.toString()
        addressRequest.reference = edtReference.text.toString()
        addressRequest.district = edtDistrict.text.toString()

        // PaymentMethod
        val paymentMethodRequest = PaymentMethodRequest()
        if (rbYape.isChecked) paymentMethodRequest.type = 1
        if (rbPlin.isChecked) paymentMethodRequest.type = 2
        if (rbCash.isChecked) paymentMethodRequest.type = 3

        val amount = edtPaymentCashAmount.text.toString()
        paymentMethodRequest.amount = amount.toDouble()

        // Date & Hour
        val dateToReceive = edtDate.text.toString() + " " + edtHour.text.toString()

        // Order
        val orderList: MutableList<OrderRequest> = arrayListOf()

        safeArgs.productsPaymentDto.productsDto.forEach {
            val orderRequest = OrderRequest()
            orderRequest.categoryId = it.categoryId
            orderRequest.productId = it.uuid
            orderRequest.quantity = it.quantity

            orderList.add(orderRequest)
        }

        // TotalAmount
        val totalAmount = safeArgs.productsPaymentDto.totalAmount

        // Payment Object
        val paymentRequest = PaymentRequest(addressRequest, paymentMethodRequest, dateToReceive, orderList, totalAmount)

        if (swTermsPayment.isChecked) {
            viewModel.processPayment(paymentRequest)
        } else {
            requireContext().toast("Debe aceptar terminos y condiciones")
        }
    }

    private fun setUpObserver() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                PaymentViewModel.PaymentState.Init -> Unit
                is PaymentViewModel.PaymentState.Error -> showError(state.message)
                is PaymentViewModel.PaymentState.SuccessPayment -> {
                    requireContext().toast("Bienvenido:: ${state.response.data}")
                    createDialogPayment(state.response.data).show()

                }
            }
        }
    }

    private fun showError(errorMessage: String) {
        requireContext().toast(errorMessage)
    }

    private fun createDialogPayment(responseData: String?): AlertDialog {
        val bindingAlert = DialogPaymentConfirmationBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())

        bindingAlert.tvOrderNumber.text = responseData

        builder.setView(bindingAlert.root)

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)

        bindingAlert.btnAccept.setOnClickListener {
            alertDialog.dismiss()
        }
        return alertDialog
    }
}