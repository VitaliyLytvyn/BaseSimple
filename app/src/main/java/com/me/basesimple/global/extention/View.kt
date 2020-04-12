package com.me.basesimple.global.extention

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.*
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition


fun View.isVisible() = visibility == View.VISIBLE

fun View.setVisible() = apply { visibility = View.VISIBLE }
fun View.setInvisible() = apply { visibility = View.INVISIBLE }
fun View.setGone() = apply { visibility = View.GONE }

//check it - alternative to setOnClickListener to disable clicks on presentation under progress bar
fun View.setEnabledAll(enabled: Boolean) {
    this.isEnabled = enabled
    this.isFocusable = enabled
    if (this is ViewGroup) {
        for (i in 0 until this.childCount)
            this.getChildAt(i).setEnabledAll(enabled)
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadFromUrl(url: String?) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadUrlAndPostponeEnterTransition(url: String, activity: FragmentActivity) {
    val target: Target<Drawable> = ImageViewBaseTarget(
        this,
        activity
    )
    Glide.with(context.applicationContext)
        .load(url)
        .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
        .into(target)
}


private class ImageViewBaseTarget(var imageView: ImageView?, var activity: FragmentActivity?) :
    DrawableImageViewTarget(imageView) {
    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        imageView?.setImageDrawable(resource)
        activity?.supportStartPostponedEnterTransition()
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        activity?.supportStartPostponedEnterTransition()
    }
}

