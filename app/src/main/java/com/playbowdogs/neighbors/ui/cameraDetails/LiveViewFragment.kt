package com.playbowdogs.neighbors.ui.cameraDetails

import android.content.SharedPreferences
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import com.playbowdogs.neighbors.databinding.FragmentLiveViewBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.utils.USER_TYPE_CUSTOMER
import com.playbowdogs.neighbors.utils.USER_TYPE_DOG_SITTER
import com.playbowdogs.neighbors.utils.USER_TYPE_PREF
import com.playbowdogs.neighbors.viewmodel.liveView.LiveViewViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


@ExperimentalCoroutinesApi
class LiveViewFragment : BaseFragment<FragmentLiveViewBinding>(FragmentLiveViewBinding::inflate) {
    private val liveViewModel: LiveViewViewModel by sharedViewModel()
    private val sharedPref by inject<SharedPreferences>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        liveViewModel.getLiveView()

        liveViewModel.onGoingAppointment.observe(viewLifecycleOwner) { onGoingAppointment ->
            binding.onGoingAppointment = onGoingAppointment
            when (sharedPref.getString(USER_TYPE_PREF, USER_TYPE_CUSTOMER)) {
                USER_TYPE_CUSTOMER -> {
                    binding.recordingButtons.root.visibility = View.GONE
                    when (onGoingAppointment?.now_recording) {
                        true -> liveViewModel.videoURI.value = onGoingAppointment.stream_links?.get(3)?.url?.toUri()
                        else -> liveViewModel.videoURI.value = null
                    }
                }

                USER_TYPE_DOG_SITTER -> {
                    binding.recordingButtons.root.visibility = View.VISIBLE
                    liveViewModel.videoURI.value = onGoingAppointment?.stream_links?.get(3)?.url?.toUri()

                    when (onGoingAppointment?.now_recording) {
                        true -> {
                            binding.recordingButtons.recordButton.isEnabled = false
                            binding.recordingButtons.stopRecordingButton.isEnabled = true
                            setGrayScale(binding.recordingButtons.recordButton, true)
                            setGrayScale(binding.recordingButtons.stopRecordingButton, false)
                        }
                        else -> {
                            binding.recordingButtons.stopRecordingButton.isEnabled = false
                            binding.recordingButtons.recordButton.isEnabled = true
                            setGrayScale(binding.recordingButtons.recordButton, false)
                            setGrayScale(binding.recordingButtons.stopRecordingButton, true)
                        }
                    }
                }
            }
        }

        binding.recordingButtons.recordButton.setOnClickListener { liveViewModel.updateAppointment(true) }

        binding.recordingButtons.stopRecordingButton.setOnClickListener { liveViewModel.updateAppointment(false) }
    }

    private fun setGrayScale(imageView: ImageView, gray: Boolean) {
        val matrix = ColorMatrix()
        when (gray) {
            true -> matrix.setSaturation(0f)
            false -> matrix.setSaturation(1f)
        }
        imageView.colorFilter = ColorMatrixColorFilter(matrix)
    }
}