package com.playbowdogs.neighbors.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.playbowdogs.neighbors.data.model.AngelCamRecordedClips
import com.playbowdogs.neighbors.databinding.RecyclerViewRecordedClipsBinding
import com.playbowdogs.neighbors.utils.Resource
import com.playbowdogs.neighbors.viewmodel.recordedClipsList.RecordedClipsListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
class RecordedClipsListAdapter(
    private val results: ArrayList<AngelCamRecordedClips>,
    private val viewModel: RecordedClipsListViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<RecordedClipsListAdapter.DataViewHolder>() {

    @ExperimentalCoroutinesApi
    class DataViewHolder(
        private val viewModel: RecordedClipsListViewModel,
        private val binding: RecyclerViewRecordedClipsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: AngelCamRecordedClips) {
            binding.name.text = result.name
            binding.start.text = viewModel.getTimeDiff(result)
            binding.createdOn.text = viewModel.humanReadableDate(result.created_at)
            binding.path = "${itemView.context.cacheDir}${result.id}.jpg"
            binding.resultUrl = result.thumbnail_url

            itemView.setOnClickListener {
                Timber.e("Clicked on recorded clip ${result.download_url}")
                viewModel.chosenCamera.value = Resource.loading(data = result)
            }
//            when {
//                result.name.isEmpty() -> Unit
//                result.end.isEmpty() -> Unit
//                result.start.isEmpty() -> Unit
//                result.id.isEmpty() -> Unit
//                result.created_at.isEmpty() -> Unit
//                result.download_url.isNullOrEmpty() -> Unit
//                result.thumbnail_url.isNullOrEmpty() -> Unit
//                else -> {
//                    binding.name.text = result.name
//                    binding.start.text = viewModel.getTimeDiff(result)
//                    binding.createdOn.text = viewModel.humanReadableDate(result.created_at)
//                    binding.path = "${itemView.context.cacheDir}${result.id}.jpg"
//                    binding.resultUrl = result.thumbnail_url
//
//                    itemView.setOnClickListener {
//                        Timber.e("Clicked on recorded clip ${result.download_url}")
//                        viewModel.chosenCamera.value = Resource.loading(data = result)
//                    }
//                }
//            }
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
//            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_recorded_clips, parent, false))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binder = RecyclerViewRecordedClipsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binder.lifecycleOwner = viewLifecycleOwner
        return DataViewHolder(viewModel, binder)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(results[position])
    }

    fun addClips(results: List<AngelCamRecordedClips>) {
        this.results.apply {
            clear()
            addAll(results)
        }
    }
}
