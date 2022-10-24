package com.alberto.market.marketapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.FragmentCategoryBinding
import com.alberto.market.marketapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val viewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryBinding.bind(view)

        init()
        setUpAdapter()
        setUpObservers()
    }

    private fun init() {
        viewModel.onUiReady()
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

    private fun updateUI(state: CategoryViewModel.CategoryState) {
        when(state) {
            CategoryViewModel.CategoryState.Init -> Unit
            is CategoryViewModel.CategoryState.Error -> requireContext().toast(state.rawResponse)
            is CategoryViewModel.CategoryState.IsLoading -> showProgress(state.isLoading)
            is CategoryViewModel.CategoryState.Success -> {
                categoryAdapter.updateList(state.categories)
            }
        }
    }

    private fun showProgress(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun setUpAdapter() = with(binding) {

        categoryAdapter = CategoryAdapter() { category ->
            val directions = CategoryFragmentDirections.actionCategoryFragmentToProductFragment(category)
            Navigation.findNavController(binding.root).navigate(directions)
        }
        rvCategories.adapter = categoryAdapter
    }
}