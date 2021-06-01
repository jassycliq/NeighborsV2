package com.playbowdogs.neighbors.ui.onboard

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playbowdogs.neighbors.BottomNavActivity
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.databinding.FragmentOnBoardingBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.utils.Status
import com.playbowdogs.neighbors.viewmodel.firebaseUI.NewFirebaseUIViewModel
import com.playbowdogs.neighbors.viewmodel.onboard.ClientResult.*
import com.playbowdogs.neighbors.viewmodel.onboard.OnBoardingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>(FragmentOnBoardingBinding::inflate) {
    private val viewModel: OnBoardingViewModel by sharedViewModel()
    private val firebaseUIViewModel: NewFirebaseUIViewModel by sharedViewModel()

    private var lastChar = " "

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBindingViewModel()
        setObservers()
        setOnClickListener()
        setOnTextChangeListener()

        viewModel.getCurrentUser()
    }

    override fun onResume() {
        super.onResume()
        binding.editTextTextPersonName.let {
            it.requestFocus()
            (context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setBindingViewModel() {
        binding.viewModel = viewModel
    }

    private fun setOnClickListener() {
//        binding?.submitButton?.setOnClickListener {
//            viewModel.getClient()
//        }
    }

    private fun setObservers() {
        viewModel.fullNameCheck.observe(viewLifecycleOwner) {
            it?.let {
                if (!it) {
                    binding.textInputLayoutFullName.error =
                        "${viewModel.fullName.value} is not a valid full name"
                } else {
                    binding.textInputLayoutFullName.error = null
                }
            }
        }

        viewModel.emailCheck.observe(viewLifecycleOwner) {
            it?.let {
                if (!it) {
                    binding.textInputLayoutEmailAddress.error =
                        "${viewModel.email.value} is not a valid email address"
                } else {
                    binding.textInputLayoutEmailAddress.error = null
                }
            }
        }

        viewModel.phoneNumberCheck.observe(viewLifecycleOwner) {
            it?.let {
                if (!it) {
                    binding.textInputLayoutPhoneNumber.error =
                        "${viewModel.phoneNumber.value} is not a valid phone number"
                } else {
                    binding.textInputLayoutPhoneNumber.error = null
                }
            }
        }

        viewModel.formChecker.observe(viewLifecycleOwner) {
            Timber.e(it.toString())
            firebaseUIViewModel.setSubmitButton(it)
        }

        viewModel.client.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        resource.data?.let { acuityClient ->
                            when (viewModel.validateClient(acuityClient.firstOrNull())) {
                                PHONE_NUMBER_ERROR -> {
                                    binding.textInputLayoutPhoneNumber.error =
                                        "Cannot find Acuity account with ${viewModel.phoneNumber.value}"
                                }
                                FULL_NAME_ERROR -> {
                                    binding.textInputLayoutFullName.error =
                                        "${viewModel.fullName.value} does not match the Acuity account"
                                }
                                EMAIL_ERROR -> {
                                    binding.textInputLayoutEmailAddress.error =
                                        "${viewModel.email.value} does not match the Acuity account"
                                }
                                NO_ERROR -> {
                                    binding.textInputLayoutEmailAddress.error = null
                                    binding.textInputLayoutFullName.error = null
                                    showAcuityAccountFoundDialog()
//                                        installModule()
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        Timber.e(it.message)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showAcuityAccountFoundDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(resources.getString(R.string.acuity_account_found))
                .setMessage(resources.getString(R.string.acuity_account_found_message))
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    saveUserToFirestore()
                    saveUserToAcuityFirestore()
                    navigateToBottomNavActivity()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun saveUserToFirestore() {
        viewModel.getToken().observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        resource.data?.let { fcmToken ->
                            viewModel.saveUserToFirestore(
                                userID = viewModel.currentUser.value?.uid,
                                displayName = viewModel.fullName.value,
                                profilePhoto = viewModel.currentUser.value?.photoUrl.toString(),
                                email = viewModel.email.value,
                                phoneNumber = viewModel.phoneNumber.value,
                                address = "",
                                city = "San Francisco",
                                zipCode = 0,
                                isActive = true,
                                fcmToken = fcmToken,
                                completedOnBoarding = true,
                            )
                        }
                    }
                    Status.ERROR -> {
                    }
                }
            }
        }
    }

    private fun saveUserToAcuityFirestore() {
        viewModel.saveAcuityUserToFirestore(viewModel.email.value)
    }

    private fun setOnTextChangeListener() {
        binding.editTextPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
                val digits: Int = binding.editTextPhone.text.toString().length
                if (digits > 1) lastChar =
                    binding.editTextPhone.text.toString().substring(digits - 1)
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int,
            ) {
                val digits: Int = binding.editTextPhone.text.toString().length
                if (lastChar != "-") {
                    if (digits == 3 || digits == 7) {
                        binding.editTextPhone.append("-")
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun navigateToBottomNavActivity() {
        firebaseUIViewModel.navigateTo("BottomNav")
        (requireActivity() as BottomNavActivity).detachNav()
    }
}
