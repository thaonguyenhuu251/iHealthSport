package com.htnguyen.ihealth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.htnguyen.ihealth.databinding.ActivityTutorialBinding

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
                if (position == 2) {
                    binding.txtNext.text = "Get started"
                } else {
                    binding.txtNext.text = "Next"
                }
            }
        })

        binding.txtNext.setOnClickListener {
            val currentPage = binding.viewPager2.currentItem
            if (currentPage < 2) {
                binding.viewPager2.setCurrentItem(currentPage + 1, false)
            } else {
                startActivity(Intent(this@TutorialActivity, MainActivity::class.java))
                finish()
            }

        }
    }

    class DotIndicatorPager2Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        data class Tutorial(val id: Int, val title: Int, val content: Int)

        lateinit var context: Context

        val listTutorial = mutableListOf<Tutorial>().apply {

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