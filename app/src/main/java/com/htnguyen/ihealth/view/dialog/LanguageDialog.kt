package com.htnguyen.ihealth.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.adapter.LanguageDialogAdapter
import com.htnguyen.ihealth.base.BaseDialog
import com.htnguyen.ihealth.databinding.DialogLanguageBinding
import com.htnguyen.ihealth.util.Constant
import com.htnguyen.ihealth.util.Event


class LanguageDialog : BaseDialog.Scaffold<DialogLanguageBinding>() {
    override val layout: Int = R.layout.dialog_language
    override val style: Int = R.style.MyDialog
    override val height: Int = WindowManager.LayoutParams.WRAP_CONTENT
    override val width: Int= WindowManager.LayoutParams.WRAP_CONTENT
    override val dim: Float? get() = 0.2f
    override val cancelable: Boolean = false
    var listLanguage = mutableListOf<String>()
    var layoutManager: LinearLayoutManager? = null
    private var languageAdapter: LanguageDialogAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        binding.txtOKLanguage.setOnClickListener {
            Event.eventChangeLanguage()
            dismiss()
        }
        binding.txtCancelLanguage.setOnClickListener {
            dismiss()
        }
        binding.recyclerview.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerview.layoutManager = layoutManager

        generateItemWork()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun generateItemWork() {
        addListLanguage()

        languageAdapter = LanguageDialogAdapter(requireContext(), listLanguage)
        binding.recyclerview.adapter = languageAdapter
        languageAdapter?.notifyDataSetChanged()
    }

    private fun addListLanguage() {
        listLanguage.add(Constant.LG_VIETNAMESE)
        listLanguage.add(Constant.LG_KOREAN)
        listLanguage.add(Constant.LG_ENGLISH)
        listLanguage.add(Constant.LG_LAOS)
        listLanguage.add(Constant.LG_MYANMAR)
        listLanguage.add(Constant.LG_RUSSIAN)
        listLanguage.add(Constant.LG_THAI)
        listLanguage.add(Constant.LG_CHINESE)
        listLanguage.add(Constant.LG_JAPANESE)
        listLanguage.add(Constant.LG_FILIPINO)
        listLanguage.add(Constant.LG_INDONESIAN)
        listLanguage.add(Constant.LG_SPANISH)
        listLanguage.add(Constant.LG_FRENCH)
        listLanguage.add(Constant.LG_INDIAN)
        listLanguage.add(Constant.LG_GERMAN)
        listLanguage.add(Constant.LG_ITALIAN)
    }

    companion object {

    }
}