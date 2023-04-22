package com.htnguyen.ihealth.view.social

import android.content.Intent
import android.os.Bundle
import android.os.FileUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.database.ktx.getValue
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.base.BaseFragment
import com.htnguyen.ihealth.databinding.FragmentSocialBinding
import com.htnguyen.ihealth.model.ActivityDaily
import com.htnguyen.ihealth.model.SocialHealth
import com.htnguyen.ihealth.support.*
import com.htnguyen.ihealth.util.CommonUtils
import com.htnguyen.ihealth.util.Database
import com.htnguyen.ihealth.util.FirebaseUtils
import com.htnguyen.ihealth.util.PreferencesUtil
import kotlin.math.roundToInt


class SocialFragment : BaseFragment<FragmentSocialBinding, SocialViewModel>() {
    override val layout: Int get() = R.layout.fragment_social
    private val viewModel by viewModels<SocialViewModel>()
    var totalStep = 0

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

    private fun observeRecord() {
        for (i in Calendar().firstYear..Calendar().endYear step 86400000) {
            getStepDay(i)
        }
        addCustomerReservationsEventListener()
    }

    private fun getStepDay(day: Long) {
        FirebaseUtils.activityDaily
            .child("record_history")
            .child(PreferencesUtil.idPrivate.toString())
            .child("activity_daily")
            .child("date$day")
            .get().addOnSuccessListener { data ->
                val activityDaily = data.getValue<ActivityDaily>()
                if (activityDaily != null) {
                    totalStep += activityDaily.step ?: 0
                }
            }.addOnCompleteListener {

            }
    }

    private fun addCustomerReservationsEventListener() {
        var options: FirebaseRecyclerOptions<SocialHealth> =
            FirebaseRecyclerOptions.Builder<SocialHealth>()
                .setQuery(FirebaseUtils.video.child("socialHealth"), SocialHealth::class.java)
                .build()

        val adapter =
            object : FirebaseRecyclerAdapter<SocialHealth, RecyclerView.ViewHolder>(options) {

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
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


                override fun onBindViewHolder(
                    holder: RecyclerView.ViewHolder,
                    position: Int,
                    model: SocialHealth
                ) {
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
                        Glide.with(requireContext()).load(getItem(position).imageUrl)
                            .error(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_profile_achievement
                                )
                            )
                            .into(itemView.findViewById(R.id.imgSocial))

                        itemView.findViewById<TextView>(R.id.txtNameSocial).text =
                            getItem(position).nameSocial

                        itemView.findViewById<TextView>(R.id.txtSocialFirstDate).text =
                            resources.getString(
                                R.string.social_first_date,
                                SimpleDateFormat("dd/MM/yyyy").format(getItem(position).firstDate)
                                    .toString()
                            )

                        itemView.findViewById<TextView>(R.id.txtSocialEndDate).text =
                            resources.getString(
                                R.string.social_end_date,
                                SimpleDateFormat("dd/MM/yyyy").format(getItem(position).endDate)
                                    .toString()
                            )

                        itemView.findViewById<TextView>(R.id.txtSocialFollowStep).text =
                            resources.getString(
                                R.string.social_follow_step,
                                CommonUtils.formatNumber(getItem(position).followStep!!)
                            )

                        itemView.findViewById<TextView>(R.id.txtSocialYourStep).text =
                            resources.getString(R.string.social_your_step, CommonUtils.formatNumber(totalStep))

                        if (getItem(position).followStep != null) {
                            itemView.findViewById<LinearProgressIndicator>(R.id.progressCircleDeterminate).progress =
                                ((totalStep.toFloat() / getItem(position).followStep!!) * 100).toInt()
                        }
                    }

                }

                inner class TypeTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                    fun bind(position: Int) {
                        Glide.with(requireContext()).load(getItem(position).imageUrl)
                            .error(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_profile_achievement
                                )
                            )
                            .into(itemView.findViewById(R.id.imgSocial))
                    }
                }

                inner class TypeThreeViewHolder(itemView: View) :
                    RecyclerView.ViewHolder(itemView) {
                    fun bind(position: Int) {
                        Glide.with(requireContext()).load(getItem(position).imageUrl)
                            .error(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_profile_achievement
                                )
                            )
                            .into(itemView.findViewById(R.id.imgSocial))
                    }
                }

                inner class TypeFourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                    fun bind(position: Int) {
                        Glide.with(requireContext()).load(getItem(position).imageUrl)
                            .error(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_profile_achievement
                                )
                            )
                            .into(itemView.findViewById(R.id.imgSocial))
                    }
                }

                inner class TypeFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                    fun bind(position: Int) {
                        Glide.with(requireContext()).load(getItem(position).imageUrl)
                            .error(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_profile_achievement
                                )
                            )
                            .into(itemView.findViewById(R.id.imgSocial))
                    }
                }

                inner class TypeSexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                    fun bind(position: Int) {
                        Glide.with(requireContext()).load(getItem(position).imageUrl)
                            .error(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_profile_achievement
                                )
                            )
                            .into(itemView.findViewById(R.id.imgSocial))
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

