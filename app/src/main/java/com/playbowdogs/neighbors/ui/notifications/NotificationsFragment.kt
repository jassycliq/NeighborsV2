package com.playbowdogs.neighbors.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.playbowdogs.neighbors.data.adapter.SavedNotificationsAdapter
import com.playbowdogs.neighbors.databinding.FragmentNotificationsBinding
import com.playbowdogs.neighbors.data.model.FirestoreMessage
import com.playbowdogs.neighbors.firebase.firestore.FirestoreViewModel
import com.playbowdogs.neighbors.utils.Status
import com.playbowdogs.neighbors.viewmodel.notifications.NotificationsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class NotificationsFragment : Fragment() {
    private val firestoreViewModel: FirestoreViewModel by sharedViewModel()
    private val notificationsViewModel: NotificationsViewModel by sharedViewModel()

    private var mBinding: FragmentNotificationsBinding? = null
    private lateinit var _adapter: SavedNotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = SavedNotificationsAdapter(arrayListOf())
        mBinding?.notificationRecycler?.adapter = _adapter
//        _binding.notificationRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        setObservers()
    }

    override fun onPause() {
        super.onPause()
        mBinding = null
    }

    private fun setObservers() {
        firestoreViewModel.subscribeNotifications().observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
//                        _binding.progressView.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
//                        _binding.progressView.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
//                        _binding.progressView.visibility = View.GONE
                        resource.data?.let { list -> retrieveList(list) }
                    }
                }
            }
        }
    }

    private fun retrieveList(results: List<FirestoreMessage?>) {
        _adapter.apply {
            addNotification(results)
//            notifyDataSetChanged()
        }
    }
}
