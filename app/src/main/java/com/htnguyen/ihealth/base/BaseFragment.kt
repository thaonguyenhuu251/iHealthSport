package com.htnguyen.ihealth.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.htnguyen.ihealth.R
import com.htnguyen.ihealth.support.InputMethodManager
import com.htnguyen.ihealth.support.hideKeyboard

abstract class BaseFragment<B : ViewDataBinding, T : BaseViewModel> : Fragment() {

    private val TAG = this::class.java.name

    lateinit var binding: B

    protected abstract val layout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root by lazy { binding.root }
        if (!this::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, layout, container, false)
            binding.lifecycleOwner = this
            onCreateView(savedInstanceState)
        } else {
            // prevent crash when transition animation is not over
            (root.parent as ViewGroup?)?.endViewTransition(root)
        }
        return root
    }


    fun bindHideKeyboardListener(vararg view: View) {
        val input = InputMethodManager(context ?: return) ?: return
        for (v in view) {
            v.setOnClickListener { input.hideKeyboard(it) }
            v.isClickable = true
        }
    }
    protected open fun onCreateView(savedInstanceState: Bundle?) {}

    //abstract fun initView(inflater: LayoutInflater, container: ViewGroup?, binding: B)

    override fun onDestroyView() {
        super.onDestroyView()
    }

    //abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    protected fun transitFragment(
        fragment: BaseFragment<*,*>,
        @IdRes id: Int,
        args: Bundle? = null,
        @AnimRes enterAnim: Int = R.anim.slide_in,
        @AnimRes exitAnim: Int = R.anim.fade_out,
        @AnimRes popEnter: Int = R.anim.fade_in,
        @AnimRes popExit: Int = R.anim.slide_out,
    ) {
        val fragmentManager = activity?.supportFragmentManager
        args?.let {
            fragment.arguments = args
        }
        fragmentManager?.beginTransaction()?.setCustomAnimations(
            enterAnim,  // enter
            exitAnim,  // exit
            popEnter,   // popEnter
            popExit  // popExit
        )?.add(id, fragment, fragment.javaClass.name)?.addToBackStack(fragment.TAG)?.commit()
    }

    protected fun transitFragmentAnimation(
        fragment: BaseFragment<*,*>,
        @IdRes id: Int,
        args: Bundle? = null,
        @AnimRes enterAnim: Int = R.anim.slide_up,
        @AnimRes exitAnim: Int = R.anim.slide_down,
        @AnimRes popEnter: Int = R.anim.slide_up,
        @AnimRes popExit: Int = R.anim.slide_down,
    ) {
        val fragmentManager = activity?.supportFragmentManager
        args?.let {
            fragment.arguments = args
        }
        fragmentManager?.beginTransaction()?.setCustomAnimations(
            enterAnim,  // enter
            exitAnim,  // exit
            popEnter,   // popEnter
            popExit  // popExit
        )?.add(id, fragment, fragment.javaClass.name)?.addToBackStack(fragment.TAG)?.commit()
    }

    protected fun replaceFragment(fragment: BaseFragment<*,*>, @IdRes id: Int, args: Bundle? = null) {
        val fragmentManager = activity?.supportFragmentManager
        args?.let {
            fragment.arguments = args
        }
        fragmentManager?.beginTransaction()?.setCustomAnimations(
            R.anim.slide_in,  // enter
            R.anim.fade_out,  // exit
            R.anim.fade_in,   // popEnter
            R.anim.slide_out  // popExit
        )?.replace(id, fragment, fragment.javaClass.name)?.commit()
    }

    protected fun transitChildFragment(
        parentFragment: Fragment,
        fragment: BaseFragment<*,*>,
        @IdRes id: Int,
        args: Bundle? = null
    ) {
        val fragmentManager = parentFragment.childFragmentManager
        fragment.arguments = args

        fragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_in,  // enter
            R.anim.fade_out,  // exit
            R.anim.fade_in,   // popEnter
            R.anim.slide_out  // popExit
        ).add(id, fragment, fragment.javaClass.name).addToBackStack(fragment.TAG).commit()
    }

    fun transitFragment(
        fragment: BaseFragment<*,*>,
        @IdRes id: Int,
        isAddToBackStack: Boolean = true,
        args: Bundle? = null
    ) {
        val fragmentManager = activity?.supportFragmentManager
        fragment.arguments = args
        fragmentManager?.beginTransaction()?.setCustomAnimations(
            R.anim.slide_in,  // enter
            R.anim.fade_out,  // exit
            R.anim.fade_in,   // popEnter
            R.anim.slide_out  // popExit
        )?.add(id, fragment, fragment.javaClass.name)?.addToBackStack(fragment.TAG)?.commit()
    }

    fun transitFragmentNoAnimation(
        fragment: BaseFragment<*,*>,
        @IdRes id: Int,
        isAddToBackStack: Boolean = true,
        args: Bundle? = null
    ) {
        val fragmentManager = activity?.supportFragmentManager
        fragment.arguments = args
        fragmentManager?.beginTransaction()?.add(id, fragment, fragment.javaClass.name)?.addToBackStack(fragment.TAG)?.commit()
    }

    fun replaceFragment(
        fragment: BaseFragment<*,*>,
        @IdRes id: Int,
        isAddToBackStack: Boolean = true,
        args: Bundle? = null
    ) {
        val fragmentManager = activity?.supportFragmentManager
        fragment.arguments = args
        fragmentManager?.beginTransaction()?.setCustomAnimations(
            R.anim.slide_in,  // enter
            R.anim.fade_out,  // exit
            R.anim.fade_in,   // popEnter
            R.anim.slide_out  // popExit
        )?.replace(id, fragment, fragment.javaClass.name)?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}