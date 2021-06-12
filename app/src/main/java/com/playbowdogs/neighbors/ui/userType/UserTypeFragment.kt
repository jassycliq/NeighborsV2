package com.playbowdogs.neighbors.ui.userType

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.data.adapter.GenericAdapter
import com.playbowdogs.neighbors.data.adapter.UserTypeDetailLookup
import com.playbowdogs.neighbors.data.model.UserType
import com.playbowdogs.neighbors.data.viewHolder.ViewHolderFactory
import com.playbowdogs.neighbors.databinding.FragmentOnBoardingUserTypeBinding
import com.playbowdogs.neighbors.utils.BaseFragment
import com.playbowdogs.neighbors.utils.MyItemKeyProvider
import com.playbowdogs.neighbors.utils.SpanningLinearLayoutManager
import com.playbowdogs.neighbors.utils.USER_TYPE_PREF
import com.playbowdogs.neighbors.viewmodel.firebaseUI.NewFirebaseUIViewModel
import com.playbowdogs.neighbors.viewmodel.onboard.OnBoardingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@FlowPreview
class UserTypeFragment : BaseFragment<FragmentOnBoardingUserTypeBinding>(FragmentOnBoardingUserTypeBinding::inflate), KoinComponent {
    private val firebaseUIViewModel: NewFirebaseUIViewModel by sharedViewModel()
    private val onBoardingViewModel: OnBoardingViewModel by sharedViewModel()
    private val sharedPref by inject<SharedPreferences>()
    private val sharedPrefEdit by inject<SharedPreferences.Editor>()

    private lateinit var adapter: GenericAdapter<Any>

    private lateinit var list: List<UserType>
    private var tracker: SelectionTracker<Long>? = null
    private var lastSelection: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        list = listOf(
            UserType(
                type = "Dog Sitter",
                description = "Looking to offer your services",
                drawableRes = R.drawable.dog_walker,
                backgroundColor = R.color.baby_blue
            ),
            UserType(
                type = "Customer",
                description = "Looking to board your pet",
                drawableRes = R.drawable.client_artwork,
                backgroundColor = R.color.cream
            ),
        )

        adapter = object : GenericAdapter<Any>(list, tracker) {
            override fun getLayoutId(position: Int, obj: Any): Int {
                return R.layout.recycler_view_user_type
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return ViewHolderFactory.create(view, container, viewType)
            }
        }

        adapter.setHasStableIds(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = SpanningLinearLayoutManager(requireActivity())

        binding.userTypeRecyclerView.layoutManager = layoutManager
        binding.userTypeRecyclerView.adapter = adapter

        binding.let {
            tracker = SelectionTracker.Builder(
                "mySelection",
                it.userTypeRecyclerView,
                MyItemKeyProvider(it.userTypeRecyclerView),
                UserTypeDetailLookup(it.userTypeRecyclerView),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectSingleAnything()
            ).build()
        }

        adapter.tracker = tracker

        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                tracker?.let {
                    if (it.selection.isEmpty) {
                        lastSelection?.let { selected -> it.select(selected) }
                    } else {
                        // TODO: Eventually replace hardcoded city with dynamic city
                        lastSelection = it.selection.first()
                        val userType = list[it.selection.first().toInt()]
                        firebaseUIViewModel.saveUserType(userType)
                        firebaseUIViewModel.setNextButton(true)
                        sharedPrefEdit.putString(USER_TYPE_PREF, userType.type)
                            .apply()
                    }
                }
            }
        })

        arguments?.let {
            setFragmentResult("userData", bundleOf(
                "fullName" to it.getString("fullName"),
                "email" to it.getString("email"),
                "phoneNumber" to it.getString("phoneNumber"),
            ))
        }
    }
}
