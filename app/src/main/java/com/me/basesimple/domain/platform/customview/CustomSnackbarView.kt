package com.me.basesimple.domain.platform.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.ContentViewCallback
import com.me.basesimple.R

class CustomSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ContentViewCallback {
//) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    val customButton: Button
    val customMessage: TextView

    init {
        View.inflate(context, R.layout.custom_snackbar_content, this)
        clipToPadding = false
        this.customButton = findViewById(R.id.customButton)
        this.customMessage = findViewById(R.id.customMessage)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        val scaleX = ObjectAnimator.ofFloat(customButton, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(customButton, View.SCALE_Y, 0f, 1f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}