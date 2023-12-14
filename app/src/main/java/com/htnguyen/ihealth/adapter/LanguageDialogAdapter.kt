package com.htnguyen.ihealth.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.util.CommonUtils
import com.htnguyen.ihealth.util.PreferencesUtil

class LanguageDialogAdapter : RecyclerView.Adapter<LanguageDialogAdapter.LanguageDialogViewModel> {
    lateinit var context: Context
    var listLanguage = mutableListOf<String>()

    constructor(context: Context, listLanguage: MutableList<String>) : super() {
        this.context = context
        this.listLanguage = listLanguage
    }

    constructor(listLanguage: MutableList<String>) : super() {
        this.listLanguage = listLanguage
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageDialogViewModel {
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.item_language, parent, false)
        return LanguageDialogViewModel(context, itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LanguageDialogViewModel, position: Int) {
        holder.bindItem(listLanguage[position])
    }

    inner class LanguageDialogViewModel(context: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var txtLanguage: TextView = itemView.findViewById(R.id.txtLanguage)
        @SuppressLint("NotifyDataSetChanged")
        fun bindItem(listLanguage: String) {
            txtLanguage.text = CommonUtils.getStringLanguage(listLanguage)
            txtLanguage.setCompoundDrawablesWithIntrinsicBounds(
                CommonUtils.getLanguages(listLanguage),
                0,
                0,
                0
            )

            itemView.setOnClickListener {
                txtLanguage.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    CommonUtils.getLanguages(listLanguage),
                    0,
                    R.drawable.ic_choose_tick,
                    0
                )
                PreferencesUtil.language = listLanguage
                notifyDataSetChanged()
            }

            if (listLanguage == PreferencesUtil.language) {
                txtLanguage.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    CommonUtils.getLanguages(listLanguage),
                    0,
                    R.drawable.ic_choose_tick,
                    0
                )
            } else {
                txtLanguage.setCompoundDrawablesWithIntrinsicBounds(
                    CommonUtils.getLanguages(listLanguage),
                    0,
                    0,
                    0
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return listLanguage.size
    }

}

