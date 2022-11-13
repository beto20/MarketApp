package com.alberto.market.marketapp.ui.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.FragmentRecordBinding
import com.alberto.market.marketapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordFragment : Fragment(R.layout.fragment_record) {

    private lateinit var binding: FragmentRecordBinding
    private val viewModel: RecordViewModel by viewModels()
    private val recordAdapter: RecordAdapter by lazy {
        RecordAdapter(itemClicked = { record ->
            val directions = RecordFragmentDirections.actionRecordFragmentToDetailRecordFragment(record)
            Navigation.findNavController(binding.root).navigate(directions)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecordBinding.bind(view)

        init()
        setUpAdapter()
        collectData()
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(state: RecordViewModel.RecordState) {
        when(state) {
            RecordViewModel.RecordState.Init -> Unit
            is RecordViewModel.RecordState.Error -> requireContext().toast(state.message)
            is RecordViewModel.RecordState.Success -> {
                recordAdapter.updateList(state.records)
            }
        }
    }

    private fun setUpAdapter() {
        binding.rvRecords.adapter = recordAdapter
    }

    private fun init() {
        viewModel.getRecords()
    }
}