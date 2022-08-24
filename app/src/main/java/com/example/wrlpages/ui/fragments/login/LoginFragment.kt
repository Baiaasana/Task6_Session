package com.example.wrlpages.ui.fragments.login

import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.wrlpages.Constants
import com.example.wrlpages.MyDataStore
import com.example.wrlpages.R
import com.example.wrlpages.ui.fragments.base.BaseFragment
import com.example.wrlpages.databinding.FragmentLoginBinding
import com.example.wrlpages.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentListener()

    }

    private fun setFragmentListener() {
        setFragmentResultListener(Constants.REQUEST_KEY) { requestKey, bundle ->
            val resultemail = bundle.getString(Constants.BUNDLE_KEY)
            binding.etEmail.setText(resultemail)
        }

        setFragmentResultListener(Constants.REQUEST_KEY_PAS) { requestKey, bundle ->
            val resultPassword = bundle.getString(Constants.BUNDLE_KEY_PAS)
            binding.etPassword.setText(resultPassword)
        }
    }

    override fun listeners() {
        binding.btnLogin.setOnClickListener {
            when {
                isEmptyField() -> {
                    Toast.makeText(context,
                        getString(R.string.empty_fields_error),
                        Toast.LENGTH_SHORT)
                        .show()
                }
                !isValidEmail() -> {
                    Toast.makeText(context,
                        getString(R.string.invalid_email_error),
                        Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    viewModel.login(Constants.KEY, email = binding.etEmail.text.toString(),
                        password = binding.etPassword.text.toString())
                }
            }
            if(binding.rememberMe.isChecked){
                observerss()
            }
            else{
                navigateToHomeFragment(binding.etEmail.text.toString())
            }
        }
    }

    override fun init() {
    }

    override fun observers() {
    }

    private fun observerss() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect {
                    when (it) {
                        is Resource.Success -> {
                            it.data?.let {
                                Toast.makeText(context,
                                    getString(R.string.logged_success),
                                    Toast.LENGTH_SHORT).show()
                                hideProgressBar()

                                navigateToHomeFragment(null)
                            }
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                            Toast.makeText(context,
                                getString(R.string.email_password_incorrect) + "\n ${it.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loader -> {
                            showProgressBar()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToHomeFragment(arg: String?) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(arg))
    }

    private fun isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()

    private fun isEmptyField(): Boolean = with(binding) {
        return@with binding.etEmail.text.toString().isEmpty() ||
                binding.etPassword.text.toString().isEmpty()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
}