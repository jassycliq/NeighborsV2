package com.playbowdogs.neighbors.ui

import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import com.playbowdogs.neighbors.databinding.FragmentLiveViewVideoBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.viewmodel.SharedViewModel
import com.playbowdogs.neighbors.viewmodel.liveView.LiveViewVideoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class LiveViewVideoFragment : BaseFragment<FragmentLiveViewVideoBinding>(FragmentLiveViewVideoBinding::inflate) {
    private val viewModel: LiveViewVideoViewModel by sharedViewModel()
    private val sharedViewModel: SharedViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setOnClickListeners()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding = null
//    }

    private fun setObservers() {
        viewModel.videoURI.observe(viewLifecycleOwner) {
            initializePlayer(it)
        }

//        sharedViewModel.isRecording.observe(viewLifecycleOwner,
//            {
//                when (it) {
//                    true -> initializePlayer(viewModel.videoURI.value)
//                    false -> releasePlayer()
//                }
//            })
    }

    private fun setOnClickListeners() {
    }

    private fun initializePlayer(uri: Uri?) {
        Timber.e("VideoURI: $uri")
        uri?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.fragmentLiveViewVideo.setAudioFocusRequest(
                    AudioManager.AUDIOFOCUS_NONE)
            }
            binding.fragmentLiveViewVideo.setOnPreparedListener {
                it.setVolume(0f, 0f)
                it.start()
            }
            binding.fragmentLiveViewVideo.setOnInfoListener { _, what, _ ->
                viewModel.videoState.value = what
                true
            }

            binding.fragmentLiveViewVideo.setVideoURI(uri)
        }
    }

    private fun releasePlayer() {
        binding.fragmentLiveViewVideo.stopPlayback()
    }
}
