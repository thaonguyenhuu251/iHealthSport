package com.htnguyen.ihealth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.model.GalleryPicture

class GalleryPicturesAdapter (private val list: List<GalleryPicture>, private val context: Context) :
    RecyclerView.Adapter<GalleryPicturesAdapter.ViewHolder>() {

    init {
        initSelectedIndexList()
    }

    constructor(list: List<GalleryPicture>, selectionLimit: Int, context: Context) : this(
        list,
        context
    ) {
        setSelectionLimit(selectionLimit)
    }

    private lateinit var onClick: (GalleryPicture) -> Unit
    private lateinit var afterSelectionCompleted: () -> Unit
    private lateinit var selectedIndexList: ArrayList<Int> // only limited items are selectable.
    private var selectionLimit = 0

    private fun initSelectedIndexList() {
        selectedIndexList = ArrayList(selectionLimit)
    }

    fun setSelectionLimit(selectionLimit: Int) {
        this.selectionLimit = selectionLimit
        initSelectedIndexList()
    }

    fun setOnClickListener(onClick: (GalleryPicture) -> Unit) {
        this.onClick = onClick
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val vh =
            ViewHolder(
                LayoutInflater.from(p0.context).inflate(R.layout.item_pick_image, p0, false)
            )
        return vh
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val picture = list[p1]
        p0.bindItem(picture)
    }

    override fun getItemCount() = list.size


    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ivImg)

        fun bindItem(galleryPicture: GalleryPicture) {
            Glide.with(context).load(galleryPicture.path).into(imageView)
            itemView.setOnClickListener {
                onClick.invoke(galleryPicture)
            }

        }
    }
}