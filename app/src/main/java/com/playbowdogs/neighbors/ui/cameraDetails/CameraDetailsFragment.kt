package com.playbowdogs.neighbors.ui.cameraDetails

import android.content.SharedPreferences
import android.media.MediaPlayer.*
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.databinding.FragmentLiveViewBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.utils.USER_TYPE_PREF
import com.playbowdogs.neighbors.viewmodel.liveView.LiveViewVideoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CameraDetailsFragment : BaseFragment<FragmentLiveViewBinding>(FragmentLiveViewBinding::inflate) {
    private val liveViewModel: LiveViewVideoViewModel by sharedViewModel()
    private val sharedPref by inject<SharedPreferences>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    val fragment: LiveViewVideoFragment by lazy { LiveViewVideoFragment() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        liveViewModel.getLiveView()

        adjustBottomSheet()
        setObservers()
        when (sharedPref.getString(USER_TYPE_PREF, "Customer")) {
            "Dog Sitter" -> {
                setOnClickListeners()
                binding.fragmentCameraDetails.recordingButtons.root.visibility = View.VISIBLE
            }
            else -> Unit
        }
        setLoadingUI()
    }

    override fun onPause() {
        super.onPause()
        unbindView()
    }

    private fun bindVideoView() {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_live_view_video, fragment)
            .commit()
    }

    private fun unbindView() {
        childFragmentManager.popBackStack()
        childFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commitAllowingStateLoss()
    }

    private fun adjustBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.fragmentDogsitterInfo.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.halfExpandedRatio = .65f
        bottomSheetBehavior.isDraggable = false
    }

    private fun setObservers() {
        liveViewModel.onGoingAppointment.observe(viewLifecycleOwner) { onGoingAppointment ->
            onGoingAppointment?.let {
                binding.fragmentDogsitterInfo.onGoingAppointment = onGoingAppointment
            }
            when (onGoingAppointment?.now_recording) {
                true -> {
                    bindVideoView()
                    liveViewModel.videoURI.value = onGoingAppointment.streams?.get(3)?.url?.toUri()
                }
                else -> {
                    unbindView()
                    setErrorUI()
                }
            }
        }

        liveViewModel.videoState.observe(viewLifecycleOwner) {
            when (it) {
                MEDIA_INFO_VIDEO_RENDERING_START -> setSuccessUI()
                MEDIA_INFO_BUFFERING_START -> {
                }
                MEDIA_INFO_BUFFERING_END -> {
                }
            }
        }
    }

    private fun setOnClickListeners() {
    }

    private fun setLoadingUI() {
        binding.fragmentDogsitterInfo.dogSitterInfo.visibility = View.INVISIBLE
        binding.fragmentDogsitterInfo.dogSitterLoading.visibility = View.VISIBLE

        binding.fragmentDogsitterInfo.dogSitterTitle.text = "Fetching \ntoday's stream"
        binding.fragmentDogsitterInfo.dogSitterProgressBar.visibility = View.VISIBLE
        binding.fragmentDogsitterInfo.dogSitterImageView.setImageDrawable(ResourcesCompat.getDrawable(
            resources,
            R.drawable.dog_fetching,
            context?.theme))
        binding.fragmentDogsitterInfo.dogSitterImageView.visibility = View.VISIBLE

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setErrorUI() {
        binding.fragmentDogsitterInfo.dogSitterInfo.visibility = View.INVISIBLE
        binding.fragmentDogsitterInfo.dogSitterLoading.visibility = View.VISIBLE

        binding.fragmentDogsitterInfo.dogSitterTitle.text = "All out of stream"
        binding.fragmentDogsitterInfo.dogSitterProgressBar.visibility = View.GONE
        binding.fragmentDogsitterInfo.dogSitterImageView.setImageDrawable(ResourcesCompat.getDrawable(
            resources,
            R.drawable.corgi_boba,
            context?.theme))
        binding.fragmentDogsitterInfo.dogSitterImageView.visibility = View.VISIBLE

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setSuccessUI() {
        binding.fragmentDogsitterInfo.dogSitterInfo.visibility = View.VISIBLE
        binding.fragmentDogsitterInfo.dogSitterLoading.visibility = View.GONE

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }
}
