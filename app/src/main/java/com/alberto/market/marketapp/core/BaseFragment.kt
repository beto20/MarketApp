package com.alberto.market.marketapp.core

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

//abstract class BaseFragment(@LayoutRes val layoutRes: Int): Fragment() {
abstract class BaseFragment(): Fragment() {

    protected fun navigation(): NavController? {
        return view?.let {
            Navigation.findNavController(it)
        }
    }

    protected fun navigationToAction(action:Int) {
        navigation()?.navigate(action)
    }

    protected fun navigationToDirections(directions: NavDirections) {
        navigation()?.navigate(directions)
    }

}