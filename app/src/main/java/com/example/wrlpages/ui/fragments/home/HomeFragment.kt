package com.example.wrlpages.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.wrlpages.Constants
import com.example.wrlpages.MyDataStore
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()


    override fun listeners() {
        binding.btnLogout.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
        }
    }

    override fun init() {
        lifecycleScope.launch {
            val emailValue = MyDataStore.read(Constants.KEY)
            binding.text.text = emailValue.toString()
        }
    }





    override fun observers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.read(Constants.KEY).collect{
                    binding.text.text = it

                }
            }
        }
    }
}