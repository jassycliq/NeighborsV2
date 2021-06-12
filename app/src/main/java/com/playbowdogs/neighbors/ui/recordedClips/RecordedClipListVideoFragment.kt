package com.playbowdogs.neighbors.ui.recordedClips

import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import com.playbowdogs.neighbors.data.model.AngelCamRecordedClips
import com.playbowdogs.neighbors.databinding.FragmentRecordedClipsVideoBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.utils.Status.*
import com.playbowdogs.neighbors.viewmodel.recordedClipsList.RecordedClipsListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


@ExperimentalCoroutinesApi
class RecordedClipListVideoFragment : BaseFragment<FragmentRecordedClipsVideoBinding>(FragmentRecordedClipsVideoBinding::inflate) {
    private val viewModel: RecordedClipsListViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setOnClickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.chosenCamera.value = null
    }

    private fun setObservers() {
        viewModel.chosenCamera.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                    }
                    ERROR -> {
                    }
                    LOADING -> {
                        resource.data?.let { data ->
                            Timber.e("Loading: ${data.download_url}")
                            binding.progressView.root.visibility = View.VISIBLE
                            viewModel.cameraUri.value = data.download_url?.toUri()
                            initializePlayer(data.download_url?.toUri(), data)
                        }
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {
    }

    private fun initializePlayer(uri: Uri?, data: AngelCamRecordedClips) {
        uri?.let {
            Timber.e("Init videoView: $it")
            Timber.e("Is binding null? $binding")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.fragmentRecordedClipsVideoView.setAudioFocusRequest(
                    AudioManager.AUDIOFOCUS_NONE)
            }

            binding.fragmentRecordedClipsVideoView.setOnPreparedListener { mp ->
                mp.setVolume(0f, 0f)
                mp.start()
                binding.progressView.root.visibility = View.INVISIBLE
            }

            binding.fragmentRecordedClipsVideoView.setVideoURI(uri)
        }
    }
}
