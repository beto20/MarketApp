package com.alberto.market.marketapp.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.alberto.market.marketapp.BuildConfig
import com.alberto.market.marketapp.MenuMainHostActivity
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.core.BaseFragment
import com.alberto.market.marketapp.databinding.DialogVersionBinding
import com.alberto.market.marketapp.databinding.FragmentLoginBinding
import com.alberto.market.marketapp.ui.common.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.edtEmail.setText("mock123@newmock.com")
        binding.edtPassword.setText("12345")

        init()
        events()
        setUpObservers()
    }

    private fun init() {
        val versionApp = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME

        val versionServer = 1
        if (versionApp < versionServer) {
            createDialogVersion().show()
        }
    }

    private fun createDialogVersion(): AlertDialog {

        val bindingAlert = DialogVersionBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(bindingAlert.root)

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)

        bindingAlert.btnUpdate.setOnClickListener {
            alertDialog.dismiss()
            val url = "https://play.google.com/store/apps/details?id=com.spotify.music&hl=es"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        bindingAlert.btnLate.setOnClickListener {
            alertDialog.dismiss()
        }

        return alertDialog
    }

    private fun events() = with(binding) {
        btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (edtEmail.text.toString().isEmpty()) {
                tiEmail.error = getString(R.string.fragment_login_validation)
                return@setOnClickListener
            }

            if (edtPassword.text.toString().isEmpty()) {
                tiPassword.error = getString(R.string.fragment_login_validation)
                return@setOnClickListener
            }
            viewModel.auth(email, password)
        }

        tvCreateAccount.setOnClickListener {
            navigationToAction(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setUpObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->

            when (state) {
                LoginViewModel.LoginState.Init -> Unit
                is LoginViewModel.LoginState.Error -> showError(state.error)
                is LoginViewModel.LoginState.IsLoading -> showProgress(state.isLoading)
                is LoginViewModel.LoginState.Success -> {
                    val userResponse = state.user
                    requireContext().toast("Bienvenido:: ${userResponse.names}, ${userResponse.surname}")
                    val intent = Intent(requireContext(), MenuMainHostActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showError(message: String) {
        requireContext().toast(message)
    }

    private fun showProgress(isCharge: Boolean) {
        if (isCharge) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }
}