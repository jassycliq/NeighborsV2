package com.playbowdogs.neighbors.ui.recordedClips

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.data.adapter.RecordedClipsListAdapter
import com.playbowdogs.neighbors.data.model.AngelCamRecordedClips
import com.playbowdogs.neighbors.databinding.FragmentRecordedClipsListBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.utils.EMPTY_RECORDED_CLIPS
import com.playbowdogs.neighbors.utils.Status.*
import com.playbowdogs.neighbors.viewmodel.recordedClipsList.RecordedClipsListViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


@ExperimentalCoroutinesApi
class RecordedClipsListFragment : BaseFragment<FragmentRecordedClipsListBinding>(FragmentRecordedClipsListBinding::inflate) {

    private val _viewModel: RecordedClipsListViewModel by sharedViewModel()

    private lateinit var adapter: RecordedClipsListAdapter

    private var networkCallJob: Deferred<Unit?>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInsetPadding(view)
        insertNestedFragment()

        adapter = RecordedClipsListAdapter(EMPTY_RECORDED_CLIPS, _viewModel, viewLifecycleOwner)
        binding.fragmentRecordedClipsFragmentRecyclerView.adapter = adapter

        setObservers()

        networkCallJob?.cancel()
    }

    override fun onPause() {
        super.onPause()
        _viewModel.cancelJob()
        if (!requireActivity().isInPictureInPictureMode) {
            binding.constraintLayout.transitionToStart()
        }
//        binding = null
    }

    private fun setInsetPadding(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            // Move our RecyclerView below status bar
            binding.fragmentRecordedClipsFragmentRecyclerView.updatePadding(top = insets.systemWindowInsetTop)
            binding.fragmentRecordedClipsVideoView.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }

    private fun insertNestedFragment() {
        val childFragment: Fragment = RecordedClipListVideoFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_recorded_clips_video_view, childFragment)
            .commit()
    }

    private fun setObservers() {
        _viewModel.recordedClips().observe(viewLifecycleOwner) {
            it?.let { list ->
                when (list.isEmpty()) {
                    true -> retrieveList(EMPTY_RECORDED_CLIPS)
                    false -> retrieveList(list)
                }
            }
        }

        _viewModel.chosenCamera.observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                    }
                    ERROR -> {
                        Timber.e("Is list fragment binding null? $binding")
                        binding.constraintLayout.transitionToStart()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        binding.constraintLayout.transitionToEnd()
                    }
                }
            }
        }
    }

    private fun retrieveList(results: List<AngelCamRecordedClips>) {
        adapter.apply {
            val sortedResult = results.sortedByDescending { it.created_at }
            addClips(sortedResult)
            notifyDataSetChanged()
        }
    }
}
