package com.htnguyen.ihealth.base

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.KeyEvent.KEYCODE_BACK
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE
import androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL
import androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.htnguyen.ihealth.R

sealed class BaseDialog : DialogFragment() {

    abstract class Alert : BaseDialog() {

        protected open val title: String? = null
        protected open val message: String? = null
        protected open val positive: String? = null
        protected open val negative: String? = null
        protected open val neutral: String? = null

        override fun onCreateDialog(builder: AlertDialog.Builder) {
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setNeutralButton(neutral, null)
            builder.setNegativeButton(negative, null)
            builder.setPositiveButton(positive, null)
        }

        override fun onResume() {
            super.onResume()
            val dialog = dialog as? AlertDialog ?: return
            dialog.getButton(BUTTON_NEUTRAL)?.setOnClickListener(::onNeutralClick)
            dialog.getButton(BUTTON_NEGATIVE)?.setOnClickListener(::onNegativeClick)
            dialog.getButton(BUTTON_POSITIVE)?.setOnClickListener(::onPositiveClick)
        }

        open fun onPositiveClick(v: View) = dismiss()

        open fun onNegativeClick(v: View) = dismiss()

        open fun onNeutralClick(v: View) = dismiss()

    }

    abstract class Scaffold<T : ViewDataBinding> : BaseDialog() {

        protected abstract val layout: Int

        protected lateinit var binding: T

        protected open val positionX = 0
        protected open val positionY = 0

        protected open val gravity = Gravity.CENTER

        protected open val width = WindowManager.LayoutParams.MATCH_PARENT
        protected open val height = WindowManager.LayoutParams.MATCH_PARENT

        abstract val style: Int

        protected open val dim: Float? = 0.6f


        final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = Dialog(requireContext(), style)
            dialog.setCanceledOnTouchOutside(cancelable)
            isCancelable = cancelable
            return dialog
        }

        final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            binding = DataBindingUtil.inflate(inflater, layout, container, false)
            binding.lifecycleOwner = this
            val root = binding.root
            // prevent crash when transition animation is not over
            (root.parent as ViewGroup?)?.endViewTransition(root)
            if (cancelable) {
                root.isFocusable = true
                root.isClickable = true
                root.setOnClickListener { dismiss() }
            }
            onCreateView(savedInstanceState)
            return binding.root
        }

        protected open fun onCreateView(savedInstanceState: Bundle?) {}

        override fun onStart() {
            super.onStart()
            onCreateWindow(dialog?.window ?: return)
        }

        override fun onResume() {
            super.onResume()
            onTrackingView()
        }

        protected open fun onTrackingView() {
            //Analytics.trackScreen(this)
        }

        open fun onCreateWindow(window: Window) {
            window.setGravity(gravity)
            window.setLayout(width, height)
            window.attributes.x = positionX
            window.attributes.y = positionY
            window.attributes.dimAmount = dim ?: return
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

    }

    abstract class Main<T : ViewDataBinding> : Scaffold<T>() {
        //final override val style: Int = R.style.Theme_Dialog_Scaffold
    }

    abstract class Study<T : ViewDataBinding> : Scaffold<T>() {
        //final override val style: Int = R.style.Theme_Dialog_Study_Scaffold
    }


    protected open val cancelable = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(cancelable)
            .apply(::onCreateDialog)
            .create()
        dialog.setCanceledOnTouchOutside(cancelable)
        return dialog
    }

    protected open fun onCreateDialog(builder: AlertDialog.Builder) {}

    override fun onStart() {
        super.onStart()
        if (cancelable) return
        dialog?.setOnKeyListener { _, keyCode, _ -> keyCode == KEYCODE_BACK }
    }

    open fun show(fragment: Fragment, tag: String? = null) {
        if (isAdded) return
        fragment.lifecycleScope.launchWhenResumed {
            show(fragment.parentFragmentManager, tag)
        }
    }

    open fun show(activity: FragmentActivity, tag: String? = null) {
        if (isAdded) return
        activity.lifecycleScope.launchWhenResumed {
            show(activity.supportFragmentManager, tag)
        }
    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismiss()
    }

}