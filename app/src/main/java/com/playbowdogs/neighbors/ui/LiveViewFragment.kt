package com.playbowdogs.neighbors.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.databinding.FragmentLiveViewBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.viewmodel.DogSitterViewModel
import com.playbowdogs.neighbors.viewmodel.liveView.LiveViewVideoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class LiveViewFragment : BaseFragment<FragmentLiveViewBinding>(FragmentLiveViewBinding::inflate) {
    private val viewModel: DogSitterViewModel by viewModel()
    private val liveViewModel: LiveViewVideoViewModel by sharedViewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val fragment = LiveViewVideoFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adjustBottomSheet()
        setObservers()
        setOnClickListeners()
        bindVideoView()

        setLoadingUI()
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
        bottomSheetBehavior.halfExpandedRatio = .5f
        bottomSheetBehavior.isDraggable = false

        binding.fragmentDogsitterInfo.dogSitterInfoTitle.text = "Today's Customer"
        binding.fragmentCameraDetails.recordingButtons.root.visibility = View.VISIBLE
    }

    private fun setObservers() {

        viewModel.cameraResultForDogSitter.observe(viewLifecycleOwner) { result ->
            liveViewModel.videoURI.value = result.streams[3].url.toUri()
        }

        viewModel.isRecording.observe(viewLifecycleOwner) { isRecording ->
            when (isRecording) {
                true -> {
                    binding.fragmentCameraDetails.fragmentLiveViewVideo.alpha = 1f
                    binding.fragmentCameraDetails.recordingButtons.recordButton.colorFilter =
                        ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
                    binding.fragmentCameraDetails.recordingButtons.stopRecordingButton.colorFilter =
                        ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(1f) })
                    binding.fragmentCameraDetails.recordingButtons.recordButton.isClickable =
                        false
                    binding.fragmentCameraDetails.recordingButtons.stopRecordingButton.isClickable =
                        true
                }
                false -> {
                    binding.fragmentCameraDetails.fragmentLiveViewVideo.alpha = 0f
                    binding.fragmentCameraDetails.recordingButtons.recordButton.colorFilter =
                        ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(1f) })
                    binding.fragmentCameraDetails.recordingButtons.stopRecordingButton.colorFilter =
                        ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
                    binding.fragmentCameraDetails.recordingButtons.recordButton.isClickable =
                        true
                    binding.fragmentCameraDetails.recordingButtons.stopRecordingButton.isClickable =
                        false
                }
            }
        }

        liveViewModel.videoState.observe(viewLifecycleOwner) {
            when (it) {
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> setSuccessUI()
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                }
            }
        }

        viewModel.customerUserForDogSitter.observe(viewLifecycleOwner) { customerFirestoreUser ->
            binding.fragmentDogsitterInfo.dogSitter = customerFirestoreUser
            if (customerFirestoreUser == null) {
                setErrorUI()
                liveViewModel.videoURI.value = null
            }
        }
    }

    private fun setOnClickListeners() {
        binding.fragmentCameraDetails.recordingButtons.recordButton.setOnClickListener {
            viewModel.saveDogSitterRecording(true,
                viewModel.customerUserForDogSitter.value?.user_id)
            viewModel.customerUserForDogSitter.value?.let {
                Timber.e("User id: ${it.user_id}\nFCMToken: ${it.fcm_token}")
                liveViewModel.sendTestMessage(it.fcm_token, "1")
            }
        }

        binding.fragmentCameraDetails.recordingButtons.stopRecordingButton.setOnClickListener {
            viewModel.saveDogSitterRecording(false)
        }
    }

    private fun setLoadingUI() {
        binding.fragmentDogsitterInfo.dogSitterInfo.visibility = View.INVISIBLE
        binding.fragmentDogsitterInfo.dogSitterLoading.visibility = View.VISIBLE

        binding.fragmentDogsitterInfo.dogSitterTitle.text = "Fetching \ncamera feed"
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

        binding.fragmentDogsitterInfo.dogSitterTitle.text = "All out of customers"
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
