package com.htnguyen.ihealth.view.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.Query
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentSearchBinding
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.view.search.VideoViewHolder.Clicklistener
import java.util.*


class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listRecent()
        listNew()
        listTrending()
        listActivities()
        listShred()
        listWorkout()
        listYoga()
    }

    private fun listRecent() {
        binding.layoutRecent.txtTitle.setText(R.string.search_recent)
        binding.layoutRecent.rcvList.setHasFixedSize(true)
        binding.layoutRecent.rcvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun listNew() {
        binding.layoutNew.txtTitle.setText(R.string.search_new)
        binding.layoutNew.rcvList.setHasFixedSize(true)
        binding.layoutNew.rcvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun listTrending() {
        binding.layoutTrending.txtTitle.setText(R.string.search_trending)
        binding.layoutTrending.rcvList.setHasFixedSize(true)
        binding.layoutTrending.rcvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun listActivities() {
        binding.layoutActivities.txtTitle.setText(R.string.search_activity)
        binding.layoutActivities.rcvList.setHasFixedSize(true)
        binding.layoutActivities.rcvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun listShred() {
        binding.layoutShredChallenges.txtTitle.setText(R.string.search_shred)
        binding.layoutShredChallenges.rcvList.setHasFixedSize(true)
        binding.layoutShredChallenges.rcvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun listWorkout() {
        binding.layoutWorkout.txtTitle.setText(R.string.search_workouts)
        binding.layoutWorkout.rcvList.setHasFixedSize(true)
        binding.layoutWorkout.rcvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun listYoga() {
        binding.layoutYoga.txtTitle.setText(R.string.search_yoga)
        binding.layoutYoga.rcvList.setHasFixedSize(true)
        binding.layoutYoga.rcvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun firebaseSearch(searchtext: String) {
        val query = searchtext.lowercase(Locale.getDefault())
        val firebaseQuery: Query =
            FirebaseUtils.databaseReferenceVideo.orderByChild("search").startAt(query)
                .endAt(query + "\uf8ff")
        val options: FirebaseRecyclerOptions<VideoModel> =
            FirebaseRecyclerOptions.Builder<VideoModel>()
                .setQuery(firebaseQuery, VideoModel::class.java)
                .build()
        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<VideoModel, VideoViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: VideoViewHolder,
                    position: Int,
                    model: VideoModel
                ) {
                    holder.setExoplayer(requireActivity().application, model.name, model.videourl)
                    holder.setOnClicklistener(object : Clicklistener {
                        override fun onItemClick(view: View?, position: Int) {
                            val name = getItem(position).name
                            val url = getItem(position).videourl
                            val intent = Intent(requireActivity(), Fullscreen::class.java)
                            intent.putExtra("nam", name)
                            intent.putExtra("ur", url)
                            startActivity(intent)
                        }

                        override fun onItemLongClick(view: View?, position: Int) {
                            //name = getItem(position).getName()
                            //showDeleteDialog(name)
                        }
                    })
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_video, parent, false)
                    return VideoViewHolder(view)
                }
            }
        firebaseRecyclerAdapter.startListening()
        binding.layoutRecent.rcvList.adapter = firebaseRecyclerAdapter
    }

    override fun onStart() {
        super.onStart()
        var options : FirebaseRecyclerOptions<VideoModel> =
            FirebaseRecyclerOptions.Builder<VideoModel>()
                .setQuery(FirebaseUtils.video.child("video"), VideoModel::class.java)
                .build()

        val adapter = object : FirebaseRecyclerAdapter<VideoModel, VideoViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {

                return VideoViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_video, parent, false)
                )
            }

            override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: VideoModel) {
                holder.setExoplayer(requireActivity().application, model.name, model.videourl)
                holder.setOnClicklistener(object : Clicklistener {
                    override fun onItemClick(view: View?, position: Int) {
                        val name = getItem(position).name
                        val url = getItem(position).videourl
                        val intent = Intent(requireActivity(), Fullscreen::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("url", url)
                        startActivity(intent)
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        //name = getItem(position).getName()
                        //showDeleteDialog(name)
                    }
                })
            }

        }

        adapter.startListening()
        binding.layoutRecent.rcvList.adapter = adapter
        binding.layoutNew.rcvList.adapter = adapter
        binding.layoutActivities.rcvList.adapter = adapter
        binding.layoutShredChallenges.rcvList.adapter = adapter
        binding.layoutWorkout.rcvList.adapter = adapter
        binding.layoutYoga.rcvList.adapter = adapter
    }

}