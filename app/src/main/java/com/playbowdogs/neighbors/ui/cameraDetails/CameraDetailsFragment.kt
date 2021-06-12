package com.playbowdogs.neighbors.ui.cameraDetails

import android.media.MediaPlayer.*
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.databinding.FragmentLiveViewBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.viewmodel.liveView.LiveViewVideoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CameraDetailsFragment : BaseFragment<FragmentLiveViewBinding>(FragmentLiveViewBinding::inflate) {
    private val liveViewModel: LiveViewVideoViewModel by sharedViewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    val fragment: LiveViewVideoFragment by lazy { LiveViewVideoFragment() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adjustBottomSheet()
        setObservers()
        setOnClickListeners()

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
//        viewModel.isRecording.observe(viewLifecycleOwner) { isRecording ->
//            Timber.e("Is Recording: $isRecording")
//            when (isRecording) {
//                true -> {
//                    bindVideoView()
//                    viewModel.cameraResultForCustomer.observe(viewLifecycleOwner) { result ->
//                        result?.let {
//                            liveViewModel.videoURI.value = result.streams[3].url.toUri()
//                        }
//                    }
//                }
//                false -> {
//                    unbindView()
//                    setErrorUI()
//                }
//            }
//        }

//        viewModel.dogSitterUser.observe(viewLifecycleOwner) { firestoreUser ->
//            mBinding?.fragmentDogsitterInfo?.dogSitter = firestoreUser
//        }

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
