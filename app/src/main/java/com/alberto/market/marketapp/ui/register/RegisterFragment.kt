package com.alberto.market.marketapp.ui.register

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.data.server.RegisterRequest
import com.alberto.market.marketapp.databinding.FragmentRegisterBinding
import com.alberto.market.marketapp.domain.Gender
import com.alberto.market.marketapp.ui.common.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    private var genders: List<Gender> = listOf()
    private var gender = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        init()
        events()
        setUpObserver()
    }

    private fun events() = with(binding) {
        include.imgBackHeader.setOnClickListener {
            activity?.onBackPressed()
        }

        btnRegister.setOnClickListener {
            val registerRequest = RegisterRequest()

            registerRequest.names = edtNames.text.toString()
            registerRequest.surname = edtSurname.text.toString()
            registerRequest.email = edtEmail.text.toString()
            registerRequest.password = edtPassword.text.toString()
            registerRequest.phone = edtPhone.text.toString()
            registerRequest.gender = gender
            registerRequest.documentNumber = edtDocumentNumber.text.toString()

            if (edtNames.text.toString().isEmpty()) {
                tiNames.error = getString(R.string.fragment_register_validation_names)
                return@setOnClickListener
            }

            if (!swTerms.isChecked) {
                requireContext().toast("Debe aceptar los terminos y condiciones")
            }

            viewModel.createAccount(registerRequest)
        }

        spGender.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            gender = genders [position].gender
        }
    }

    private fun init() = with(binding) {
        include.tvTitleHeader.text = getString(R.string.fragment_register_title)
    }

    private fun setUpObserver() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                RegisterViewModel.RegisterAccountState.Init -> Unit
                is RegisterViewModel.RegisterAccountState.Error -> showError(state.message)
                is RegisterViewModel.RegisterAccountState.IsLoading -> showProgress(state.isLoading)
                is RegisterViewModel.RegisterAccountState.SuccessGenders -> populateGenders(state.genders)
                is RegisterViewModel.RegisterAccountState.SuccessRegister -> {
                    requireContext().toast("Bienvenido ${state.user.names}")
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    private fun showError(errorMessage: String) {
        requireContext().toast(errorMessage)
    }

    private fun showProgress(visibility: Boolean) = with(binding) {
        if (visibility) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun populateGenders(gendersDomain: List<Gender>) = with(binding) {
        spGender.setAdapter(ArrayAdapter(requireContext(), R.layout.item_spinner_gender, gendersDomain))
        genders = gendersDomain
    }
}