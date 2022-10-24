package com.alberto.market.marketapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.alberto.market.marketapp.databinding.ActivityMenuMainHostBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuMainHostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuMainHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuMainHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.menu_nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        binding.imgMenu.setOnClickListener {
            binding.menuDrawerLayout.openDrawer(GravityCompat.START)
        }
    }
}