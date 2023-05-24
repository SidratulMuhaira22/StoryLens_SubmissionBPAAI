package com.sidratul.storylens_submission1bpaai.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sidratul.storylens_submission1bpaai.databinding.ListStoryBinding

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val storyData = ArrayList<StoryModel>()

    fun setData(stories: ArrayList<StoryModel>) {
        storyData.clear()
        storyData.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StoryViewHolder(
        ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) =
        holder.bind(storyData[position])

    override fun getItemCount() = storyData.size

    inner class StoryViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryModel) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(binding.imgStory)
                tvName.text = story.name
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.EXTRA_DETAIL, story)
                }
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgStory, "image"),
                        Pair(binding.tvName, "name"),
                    )
                it.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}