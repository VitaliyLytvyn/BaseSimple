package com.me.basesimple.domain.platform.customview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.me.basesimple.R

class CustomSnackbar(
    parent: ViewGroup,
    private val content: CustomSnackbarView
) : BaseTransientBottomBar<CustomSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        getView().setPadding(0, 0, 0, 0)
    }

    fun setAction(textAction: CharSequence, action: () -> Any) : CustomSnackbar {
        val button = content.customButton
        button.visibility = View.VISIBLE
        button.text = textAction
        button.setOnClickListener {
            action.invoke()
            dismiss()
        }
       // duration = BaseTransientBottomBar.LENGTH_INDEFINITE

        return this
    }

    fun setBackgroundColor(res: Int): CustomSnackbar {
        content.setBackgroundResource(res)
        return this
    }

    fun setActionTextColor(color: Int) : CustomSnackbar {
        content.customButton.setTextColor(color)
        return this
    }
    companion object {

        fun make(view: View, text: CharSequence, duration: Int = LENGTH_SHORT): CustomSnackbar {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.custom_snackbar_outer,
                parent,
                false
            ) as CustomSnackbarView

            customView.customMessage.text = text

            val dur = when (duration) {
                LENGTH_INDEFINITE -> BaseTransientBottomBar.LENGTH_INDEFINITE
                LENGTH_LONG -> BaseTransientBottomBar.LENGTH_LONG
                else -> BaseTransientBottomBar.LENGTH_SHORT
            }

            // We create and return our Snackbar
            return CustomSnackbar(
                parent,
                customView
            ).setDuration(dur)
        }

        const val LENGTH_INDEFINITE = -2
        const val LENGTH_SHORT = -1
        const val LENGTH_LONG = 0
    }

}