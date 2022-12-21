package com.htnguyen.ihealth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.htnguyen.ihealth.databinding.ActivityTutorialBinding
import com.htnguyen.ihealth.view.main.MainActivity

class TutorialActivity : AppCompatActivity() {
    lateinit var binding: ActivityTutorialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DotIndicatorPager2Adapter()
        binding.viewPager2.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager2)

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    5 -> {
                        binding.txtNext.text = "Get started"
                    }
                    0 -> {
                        binding.txtBack.visibility = View.GONE
                    }
                    else -> {
                        binding.txtNext.text = "Next"
                    }
                }
            }
        })

        binding.txtNext.setOnClickListener {
            val currentPage = binding.viewPager2.currentItem
            if (currentPage < 5) {
                binding.viewPager2.setCurrentItem(currentPage + 1, false)
                binding.txtBack.visibility = View.VISIBLE
            } else {
                startActivity(Intent(this@TutorialActivity, MainActivity::class.java))
                finish()
            }

        }

        binding.txtBack.setOnClickListener {
            val currentPage = binding.viewPager2.currentItem
            if (currentPage > 0) {
                binding.viewPager2.setCurrentItem(currentPage - 1, false)
            }

        }

        binding.txtSkip.setOnClickListener {
            startActivity(Intent(this@TutorialActivity, MainActivity::class.java))
            finish()
        }
    }

    class DotIndicatorPager2Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        data class Tutorial(val id: Int, val title: Int, val content: Int)

        lateinit var context: Context

        val listTutorial = mutableListOf<Tutorial>().apply {
            add(Tutorial(R.drawable.img_splash_tutorial_one, R.string.tutorial_exercise_comfort_title, R.string.tutorial_exercise_comfort_content_one))
            add(Tutorial(R.drawable.img_splash_tutorial_two, R.string.tutorial_exercise_comfort_title, R.string.tutorial_exercise_comfort_content_two))
            add(Tutorial(R.drawable.img_splash_tutorial_three, R.string.tutorial_exercise_comfort_title, R.string.tutorial_exercise_comfort_content_three))
            add(Tutorial(R.drawable.img_splash_tutorial_file, R.string.tutorial_connect_doctor_title, R.string.tutorial_connect_doctor_content))
            add(Tutorial(R.drawable.img_splash_tutorial_six, R.string.tutorial_choose_doctor_title, R.string.tutorial_choose_doctor_content))
            add(Tutorial(R.drawable.img_splash_tutorial_seven, R.string.tutorial_healthy_way_title, R.string.tutorial_healthy_way_content))
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            context = parent.context
            return object : RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_tutorial_material_page, parent, false)
            ) {

            }
        }

        override fun getItemCount() = listTutorial.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val binding = holder.itemView
            val imageView = binding.findViewById<ImageView>(R.id.imgTutorial)
            val txtTitle = binding.findViewById<TextView>(R.id.txtTitle)
            val txtContent = binding.findViewById<TextView>(R.id.txtContent)

            imageView.setImageResource(listTutorial[position].id)
            txtTitle.text = context.getString(listTutorial[position].title)
            txtContent.text = context.getString(listTutorial[position].content)
        }
    }
}