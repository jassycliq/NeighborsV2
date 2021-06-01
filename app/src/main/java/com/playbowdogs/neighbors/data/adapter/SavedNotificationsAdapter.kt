package com.playbowdogs.neighbors.data.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.codequest.animatedrecyclerview.AnimatedItemHolder
import com.codequest.animatedrecyclerview.AnimatedRecyclerAdapter
import com.playbowdogs.neighbors.data.model.FirestoreMessage
import com.playbowdogs.neighbors.databinding.RecyclerViewSavedNotificationsBinding
import com.playbowdogs.neighbors.utils.GlideApp

class SavedNotificationsAdapter(
    private val savedNotification: ArrayList<FirestoreMessage?>,
) : AnimatedRecyclerAdapter<FirestoreMessage, SavedNotificationsAdapter.DataViewHolder>(
    ItemCallbackImpl()
) {

    class DataViewHolder(private val binding: RecyclerViewSavedNotificationsBinding) :
        AnimatedItemHolder(binding.root) {
        private var animator: AnimatorSet? = null

        fun bind(notification: FirestoreMessage) {
            itemView.apply {
                binding.notificationTitle.text = notification.title
                binding.notificationBody.text = notification.body
                binding.notificationTimestamp.text = notification.timestamp

                GlideApp.with(itemView.context)
                    .load(com.playbowdogs.neighbors.R.drawable.ic_notifications)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.notificationIcon)

                setOnClickListener {
//                    it.findNavController().navigate(R.id.action_recordedClipsListFragment_to_recordedClipDetailFragment)
                }
            }
        }

        override fun onEnterFromTop() {
            animator?.cancel()
            animateTranslation(
                startTranslationValue = -ANIMATED_TRANSLATION_AMOUNT,
                finalTranslationValue = 0f,
                startAlphaValue = itemView.alpha,
                finalAlphaValue = 1f
            )
        }

        override fun onExitToTop() {
            animator?.cancel()
            animateTranslation(
                startTranslationValue = 0f,
                finalTranslationValue = -ANIMATED_TRANSLATION_AMOUNT,
                startAlphaValue = itemView.alpha,
                finalAlphaValue = 0f
            )
        }

        override fun onEnterFromBottom() {
            animator?.cancel()
            animateTranslation(
                startTranslationValue = ANIMATED_TRANSLATION_AMOUNT,
                finalTranslationValue = 0f,
                startAlphaValue = itemView.alpha,
                finalAlphaValue = 1f
            )
        }

        override fun onExitToBottom() {
            animator?.cancel()
            animateTranslation(
                startTranslationValue = 0f,
                finalTranslationValue = ANIMATED_TRANSLATION_AMOUNT,
                startAlphaValue = itemView.alpha,
                finalAlphaValue = 0f
            )
        }

        private fun animateTranslation(
            startTranslationValue: Float,
            finalTranslationValue: Float,
            startAlphaValue: Float,
            finalAlphaValue: Float
        ) {
            val translationAnimator =
                ObjectAnimator
                    .ofFloat(itemView, "translationY", startTranslationValue, finalTranslationValue)

            val alphaAnimator =
                ObjectAnimator
                    .ofFloat(itemView, "alpha", startAlphaValue, finalAlphaValue)

            animator = AnimatorSet().apply {
                playTogether(translationAnimator, alphaAnimator)
                duration = ANIMATION_DURATION
                start()
            }
        }

        private companion object {
            const val ANIMATION_DURATION = 200L
            const val ANIMATED_TRANSLATION_AMOUNT = 200f
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
//            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_saved_notifications, parent, false))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            RecyclerViewSavedNotificationsBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = savedNotification.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        savedNotification[position]?.let { holder.bind(it) }
    }

    fun addNotification(results: List<FirestoreMessage?>) {
        this.savedNotification.apply {
            clear()
            addAll(results.asReversed())
            notifyDataSetChanged()
        }
    }
}

class ItemCallbackImpl<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem
}
