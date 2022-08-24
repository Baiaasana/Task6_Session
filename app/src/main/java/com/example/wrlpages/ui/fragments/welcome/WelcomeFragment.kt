package com.example.wrlpages.ui.fragments.welcome

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wrlpages.Constants
import com.example.wrlpages.MyDataStore
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.launch

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(
    FragmentWelcomeBinding::inflate) {

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun hasSession(key: String):Boolean{
        val token = MyDataStore.read(key)
        return token.toString().isNotEmpty()
    }

    private fun navigateToHome(){
        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment(null))
    }

    override fun listeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragment())
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
                Log.d("login", "welcome login")
            }
        }
    }

    override fun init() {

        if (hasSession(Constants.KEY)){
            navigateToHome()
        }

    }

    override fun observers() {
    }


}