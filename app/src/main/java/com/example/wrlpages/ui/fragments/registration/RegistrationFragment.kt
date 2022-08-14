package com.example.wrlpages.ui.fragments.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.wrlpages.R
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentRegistrationBinding
import com.example.wrlpages.utils.Resource
import kotlinx.coroutines.launch


class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        observers()
    }

    private fun listeners() {
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
                    else -> {
                        viewModel.register(email = binding.etEmail.text.toString(),
                            password = binding.etPassword.text.toString())
                    }
                }
            }
        }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect {
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let {
                                hideProgressBar()
                                Toast.makeText(context,
                                    getString(R.string.register_success),
                                    Toast.LENGTH_SHORT).show()

                            }
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                            Toast.makeText(context,
                                getString(R.string.already_registered) + "\n ${it.message}",
                                Toast.LENGTH_SHORT).show()
//                            Snackbar.make(requireView(), getString(R.string.already_registered), Snackbar.LENGTH_LONG).show()
                        }
                        is Resource.Loader -> {
                            showProgressBar()
                        }
                    }
                }
            }
        }
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

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
}