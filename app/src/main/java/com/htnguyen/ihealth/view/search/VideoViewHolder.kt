package com.htnguyen.ihealth.view.search

import android.app.Application
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.htnguyen.ihealth.view.search.VideoViewHolder.Clicklistener
import android.widget.TextView
import com.htnguyen.ihealth.R
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.ExoPlayerFactory
import android.net.Uri
import android.util.Log
import android.view.View
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import java.lang.Exception
import androidx.recyclerview.widget.RecyclerView

class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var exoPlayer: SimpleExoPlayer? = null
    var playerView: PlayerView? = null
    private var mClickListener: Clicklistener? = null
    fun setExoplayer(application: Application?, name: String?, Videourl: String?) {
        val textView = itemView.findViewById<TextView>(R.id.tv_item_name)
        playerView = itemView.findViewById(R.id.exoplayer_item)
        textView.text = name
        try {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(application).build()
            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            exoPlayer = ExoPlayerFactory.newSimpleInstance(application) as SimpleExoPlayer
            val video = Uri.parse(Videourl)
            val dataSourceFactory = DefaultHttpDataSourceFactory("video")
            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource =
                ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null)
            playerView!!.player = exoPlayer
            exoPlayer!!.prepare(mediaSource)
            exoPlayer!!.playWhenReady = false
        } catch (e: Exception) {
            Log.e("ViewHolder", "exoplayer error$e")
        }
    }

    fun setOnClicklistener(clicklistener: Clicklistener?) {
        mClickListener = clicklistener
    }

    interface Clicklistener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    init {
        itemView.setOnClickListener { view -> mClickListener!!.onItemClick(view, adapterPosition) }
        itemView.setOnLongClickListener { view ->
            mClickListener!!.onItemLongClick(view, adapterPosition)
            false
        }
    }
}