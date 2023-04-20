package com.htnguyen.ihealth.view.social

import android.content.Intent
import android.os.Bundle
import android.os.FileUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentSocialBinding
import com.htnguyen.ihealth.model.SocialHealth
import com.htnguyen.ihealth.util.FirebaseUtils


class SocialFragment : BaseFragment<FragmentSocialBinding, SocialViewModel>() {
    override val layout: Int get() = R.layout.fragment_social
    private val viewModel by viewModels<SocialViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.list.recyclerview.setHasFixedSize(true)
        binding.list.recyclerview.isNestedScrollingEnabled = false
        observeRecord()

        binding.fabChallenge.setOnClickListener {
            startActivity(Intent(requireActivity(), ChooseChallengeActivity::class.java))
            binding.fab.close(true)
        }

        binding.fabPosts.setOnClickListener {
            startActivity(Intent(requireActivity(), CreatePostActivity::class.java))
            binding.fab.close(true)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun observeRecord() {
        addCustomerReservationsEventListener()
    }

    private fun addCustomerReservationsEventListener() {
        var options : FirebaseRecyclerOptions<SocialHealth> =
            FirebaseRecyclerOptions.Builder<SocialHealth>()
                .setQuery(FirebaseUtils.video.child("socialHealth"), SocialHealth::class.java)
                .build()

        val adapter = object : FirebaseRecyclerAdapter<SocialHealth, RecyclerView.ViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return when (viewType) {
                    ViewType.TYPE_ONE.type -> {
                        val view = inflater.inflate(R.layout.item_social_one, parent, false)
                        TypeOneViewHolder(view)
                    }
                    ViewType.TYPE_TWO.type -> {
                        val view = inflater.inflate(R.layout.item_social_two, parent, false)
                        TypeTwoViewHolder(view)
                    }
                    ViewType.TYPE_THREE.type -> {
                        val view = inflater.inflate(R.layout.item_social_three, parent, false)
                        TypeThreeViewHolder(view)
                    }
                    ViewType.TYPE_FOUR.type -> {
                        val view = inflater.inflate(R.layout.item_social_two, parent, false)
                        TypeFourViewHolder(view)
                    }
                    ViewType.TYPE_FILE.type -> {
                        val view = inflater.inflate(R.layout.item_social_one, parent, false)
                        TypeFileViewHolder(view)
                    }
                    else -> {
                        val view = inflater.inflate(R.layout.item_social_three, parent, false)
                        TypeSexViewHolder(view)
                    }
                }
            }

            override fun getItemViewType(position: Int): Int {
                return when (getItem(position).typeSocial) {
                    0 -> ViewType.TYPE_ONE.type
                    1 -> ViewType.TYPE_TWO.type
                    2 -> ViewType.TYPE_THREE.type
                    3 -> ViewType.TYPE_FOUR.type
                    4 -> ViewType.TYPE_FILE.type
                    else -> ViewType.TYPE_SIX.type
                }
            }


            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: SocialHealth) {
                when (getItem(position).typeSocial) {
                    0 -> (holder as TypeOneViewHolder).bind(position)
                    1 -> (holder as TypeTwoViewHolder).bind(position)
                    2 -> (holder as TypeThreeViewHolder).bind(position)
                    3 -> (holder as TypeFourViewHolder).bind(position)
                    4 -> (holder as TypeFileViewHolder).bind(position)
                    else -> (holder as TypeSexViewHolder).bind(position)
                }
            }

            inner class TypeOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(position: Int) {
                }

            }

            inner class TypeTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(position: Int) {
                }
            }

            inner class TypeThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(position: Int) {
                }
            }

            inner class TypeFourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(position: Int) {
                }
            }

            inner class TypeFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(position: Int) {
                }
            }

            inner class TypeSexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(position: Int) {
                }
            }


        }

        adapter.startListening()
        binding.list.recyclerview.adapter = adapter
    }

    enum class ViewType(val type: Int) {
        TYPE_ONE(0),
        TYPE_TWO(1),
        TYPE_THREE(2),
        TYPE_FOUR(3),
        TYPE_FILE(4),
        TYPE_SIX(5),
    }

}