package com.example.wrlpages.ui.fragments.registration

import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.wrlpages.Constants
import com.example.wrlpages.R
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentRegistrationBinding
import com.example.wrlpages.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val viewModel: RegistrationViewModel by viewModels()

    override fun listeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                when {
                    isEmptyField() -> {
                        Toast.makeText(context,
                            getString(R.string.empty_fields_error),
                            Toast.LENGTH_SHORT).show()
                    }
                    !isValidEmail() -> {
                        Toast.makeText(context, getString(R.string.invalid_email_error),
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                    isValidPassword() -> {
                        Toast.makeText(context,
                            getString(R.string.password_length_limit),
                            Toast.LENGTH_SHORT).show()
                    }
                    !passwordsMatch() -> {
                        Toast.makeText(context,
                            getString(R.string.pass_not_match),
                            Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.register(email = binding.etEmail.text.toString(),
                            password = binding.etPassword.text.toString())
                        observer()
                    }
                }
            }
        }

        binding.imgBack.setOnClickListener {
            registerToWelcome()
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect {
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let {
                                Toast.makeText(context,
                                    getString(R.string.register_success),
                                    Toast.LENGTH_SHORT).show()
                                fragmentResultApi()
                                delay(2000)
                                findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                                Log.d("log", "yoy have registered")

                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(context,
                                getString(R.string.already_registered) + "\n ${it.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loader -> {
                            binding.progressBar.isVisible = it.loading
                        }
                    }
                }
            }
        }
    }

    override fun init() {
    }
    override fun observers() {
    }

    private fun isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()

    private fun isEmptyField(): Boolean = with(binding) {
        return@with binding.etEmail.text.toString().isEmpty() ||
                binding.etPassword.text.toString().isEmpty()
    }

    private fun isValidPassword(): Boolean = with(binding) {
        return@with binding.etPassword.text.toString().length < 8
    }

    private fun passwordsMatch(): Boolean = with(binding) {
        return@with binding.etPassword.text.toString() == binding.etRepeatPassword.text.toString()
    }

    private fun fragmentResultApi() {
        val emailInput = binding.etEmail.text.toString()
        val passwordInput = binding.etPassword.text.toString()

        setFragmentResult(Constants.REQUEST_KEY,
            bundleOf(
                Constants.BUNDLE_KEY to emailInput,
                Constants.BUNDLE_KEY_PAS to passwordInput
            )
        )
    }

    private fun registerToWelcome() {
        findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToWelcomeFragment())
    }

}