package com.iceka.whatsappclone.status

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.iceka.whatsappclone.databinding.ActivityMyStatusBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback


class MyStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyStatusBinding

    private var filePath: String = "https://www.youtube.com/watch?v=azsQKBYn5rU"

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var extractorsFactory: ExtractorsFactory? = null
    val videoId = "azsQKBYn5rU";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupToolbar()
        getLifecycle().addObserver(binding.youtubePlayerView);

        Glide.with(this)
            .load("https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg")
            .into(binding.youtubeThumbnail);



        loadYoutubeVideo()

    }

    private fun setupToolbar() {
        binding.toolbar.title = "My Status"
        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun loadYoutubeVideo() {
        binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                binding.relativeLayout.visibility = View.GONE
                youTubePlayer.loadVideo(videoId = videoId, 0f)
            }
        })
    }
}