package com.alberto.market.marketapp.ui.record.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alberto.market.marketapp.R
import com.alberto.market.marketapp.databinding.FragmentDetailRecordBinding

class DetailRecordFragment : Fragment(R.layout.fragment_detail_record) {

    private lateinit var binding: FragmentDetailRecordBinding
    private val viewModel: DetailRecordViewModel by viewModels()
    private val safeArgs: DetailRecordFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailRecordBinding.bind(view)

    }
}