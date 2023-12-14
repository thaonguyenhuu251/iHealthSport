package com.htnguyen.ihealth.model

import android.content.Context
import android.view.View



internal open class TextItem(context: Context, description: Int) {
    val description: String = context.getString(description)
    var content: String = ""
    private var icon: Int = 0
    internal var buttonClickListener: View.OnClickListener? = null
    private var updateListener: UpdateListener? = null

    internal open val isSwipeable: Boolean
        get() = false

    internal open fun setContent(content: String) {
        this.content = content
        updateListener?.update(content)
    }

    internal fun setIcon(icon: Int) {
        this.icon = icon
        updateListener?.updateIcon(icon)
    }

    internal fun setUpdateListener(updateListener: UpdateListener) {
        this.updateListener = updateListener
        updateListener.update(content)
        updateListener.updateIcon(icon)
    }

    interface UpdateListener {
        fun update(text: String?)

        fun updateIcon(id: Int)
    }
}
