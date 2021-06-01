package com.playbowdogs.neighbors.ui.firebaseUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.playbowdogs.neighbors.databinding.FragmentOnBoardingPagerBinding
import com.playbowdogs.neighbors.ui.onboard.OnBoardingFragment
import com.playbowdogs.neighbors.ui.userType.UserTypeFragment
import com.playbowdogs.neighbors.utils.RootViewDeferringInsetsCallback
import com.playbowdogs.neighbors.utils.TranslateDeferringInsetsAnimationCallback
import com.playbowdogs.neighbors.viewmodel.SharedViewModel
import com.playbowdogs.neighbors.viewmodel.firebaseUI.NewFirebaseUIViewModel
import com.playbowdogs.neighbors.viewmodel.onboard.OnBoardingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val NUM_PAGES = 2

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
class OnBoardViewPager : Fragment() {
    private var _binding: FragmentOnBoardingPagerBinding? = null
    private val binding: FragmentOnBoardingPagerBinding get() = _binding!!

    private val firebaseVM: NewFirebaseUIViewModel by sharedViewModel()
    private val onBoardingViewModel: OnBoardingViewModel by sharedViewModel()
    private val sharedViewModel: SharedViewModel by sharedViewModel()

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnBoardingPagerBinding.inflate(inflater, container, false)
        viewPager = binding.onBoardPager

        // Instantiate a ViewPager2 and a PagerAdapter.

        // The pager adapter, which provides the pages to the view pager widget.
        pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInsetPadding(view)
        windowInsetAnim()

        lifecycleScope.launchWhenCreated {
            setObservers()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setInsetPadding(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            // Move our View below status bar
            binding.onBoardPager.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }

    private fun setObservers() {
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.buttonCancel.text = "Cancel"
                        binding.buttonNext.text = "Next"
                        binding.buttonNext.isEnabled = firebaseVM.nextButton.value ?: false
                    }
                    1 -> {
                        binding.buttonCancel.text = "Back"
                        binding.buttonNext.text = "Submit"
                        binding.buttonNext.isEnabled = firebaseVM.submitButton.value ?: false
                    }
                }
            }
        })

        binding.buttonCancel.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> firebaseVM.signOut(context)
                1 -> viewPager.currentItem -= 1
            }
        }

        binding.buttonNext.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> viewPager.currentItem += 1
                1 -> onBoardingViewModel.getClient()
            }
        }

        firebaseVM.nextButton.observe(viewLifecycleOwner) {
            binding.buttonNext.isEnabled = it ?: false
        }

        firebaseVM.submitButton.observe(viewLifecycleOwner) {
            binding.buttonNext.isEnabled = it ?: false
        }
    }

    private fun windowInsetAnim() {
        /**
         * 1) Since our Activity has declared `window.setDecorFitsSystemWindows(false)`, we need to
         * handle any [WindowInsetsCompat] as appropriate.
         *
         * Our [RootViewDeferringInsetsCallback] will update our attached view's padding to match
         * the combination of the [WindowInsetsCompat.Type.systemBars], and selectively apply the
         * [WindowInsetsCompat.Type.ime] insets, depending on any ongoing WindowInsetAnimations
         * (see that class for more information).
         */
        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        // RootViewDeferringInsetsCallback is both an WindowInsetsAnimation.Callback and an
        // OnApplyWindowInsetsListener, so needs to be set as so.
        ViewCompat.setWindowInsetsAnimationCallback(binding.root, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root, deferringInsetsListener)

        /**
         * 2) The second step is reacting to any animations which run. This can be system driven,
         * such as the user focusing on an EditText and on-screen keyboard (IME) coming on screen,
         * or app driven (more on that in step 3).
         *
         * To react to animations, we set an [android.view.WindowInsetsAnimation.Callback] on any
         * views which we wish to react to inset animations. In this example, we want our
         * EditText holder view, and the conversation RecyclerView to react.
         *
         * We use our [TranslateDeferringInsetsAnimationCallback] class, bundled in this sample,
         * which will automatically move each view as the IME animates.
         *
         * Note about [TranslateDeferringInsetsAnimationCallback], it relies on the behavior of
         * [RootViewDeferringInsetsCallback] on the layout's root view.
         */
        ViewCompat.setWindowInsetsAnimationCallback(
            binding.buttonNext,
            TranslateDeferringInsetsAnimationCallback(
                view = binding.buttonNext,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
            )
        )
        ViewCompat.setWindowInsetsAnimationCallback(
            binding.buttonCancel,
            TranslateDeferringInsetsAnimationCallback(
                view = binding.buttonCancel,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    private inner class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserTypeFragment()
                1 -> OnBoardingFragment()
                else -> { UserTypeFragment() }
            }
        }
    }
}
