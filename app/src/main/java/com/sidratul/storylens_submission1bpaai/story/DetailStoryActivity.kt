package com.sidratul.storylens_submission1bpaai.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.sidratul.storylens_submission1bpaai.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater )
        setContentView(binding.root)
        supportActionBar?.hide()

        setupView()
    }

    private fun setupView() {
        val detail = intent.getParcelableExtra<StoryModel>(EXTRA_DETAIL)

        binding.apply {
            tvNameDetail.text = detail?.name
            tvDesc.text = detail?.description
        }
        Glide.with(this)
            .load(detail?.photoUrl)
            .into(binding.imgStoryDetail)
    }
}