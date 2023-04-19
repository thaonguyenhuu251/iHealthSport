package com.htnguyen.ihealth.view.search

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.htnguyen.ihealth.BR
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseActivity
import com.htnguyen.ihealth.databinding.ActivityFullscreenBinding

class Fullscreen : BaseActivity<ActivityFullscreenBinding, FullScreenViewModel>() {
    private var player: SimpleExoPlayer? = null
    var fullscreen = false

    private var url: String? = null
    private var playwhenready = false
    private var currentWindow = 0
    private var playbackposition: Long = 0

    override val layout: Int = R.id.exoplayer_fullscreen
    override val viewModel: FullScreenViewModel by viewModels()
    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = intent
        url = intent.extras?.getString("url")

        val title: String? = intent.extras?.getString("name")
        binding.tvFullscreen.text = title
        binding.exoplayerFullscreen.findViewById<ImageView>(R.id.exoplayer_fullscreen_icon).setOnClickListener {
            if (fullscreen) {
                binding.exoplayerFullscreen.findViewById<ImageView>(R.id.exoplayer_fullscreen_icon)
                    .setImageDrawable(
                        ContextCompat.getDrawable(
                            this@Fullscreen,
                            R.drawable.ic_fullscreen_expand
                        )
                    )
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                if (supportActionBar != null) {
                    supportActionBar?.show()
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                val params: LinearLayout.LayoutParams =
                    binding.exoplayerFullscreen.layoutParams as LinearLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (200 * applicationContext.resources.displayMetrics.density).toInt()
                binding.exoplayerFullscreen.layoutParams = params
                fullscreen = false
            } else {
                binding.exoplayerFullscreen.findViewById<ImageView>(R.id.exoplayer_fullscreen_icon).setImageDrawable(
                        ContextCompat.getDrawable(
                            this@Fullscreen,
                            R.drawable.ic_fullscreen_skrink
                        )
                    )
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                if (supportActionBar != null) {
                    supportActionBar?.hide()
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                val params: LinearLayout.LayoutParams =
                    binding.exoplayerFullscreen.layoutParams as LinearLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.exoplayerFullscreen.layoutParams = params
                fullscreen = true
            }
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val datasourcefactory: DataSource.Factory = DefaultHttpDataSourceFactory("video")
        return ProgressiveMediaSource.Factory(datasourcefactory)
            .createMediaSource(uri)
    }

    private fun initializeplayer() {
        player = ExoPlayerFactory.newSimpleInstance(this)
        binding.exoplayerFullscreen.player = player
        val uri: Uri = Uri.parse(url)
        val mediaSource: MediaSource = buildMediaSource(uri)
        player?.playWhenReady = playwhenready
        player?.seekTo(currentWindow, playbackposition)
        player?.prepare(mediaSource, false, false)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 26) {
            initializeplayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT >= 26 || player == null) {
            initializeplayer();
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT > 26) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 26) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playwhenready = player!!.playWhenReady
            playbackposition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player = null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        player?.stop()
        releasePlayer()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}