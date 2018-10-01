package me.saket.fluidresize

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

object FluidContentResizer {

  private var heightAnimator: ValueAnimator = ObjectAnimator()

  fun listen(activity: Activity) {
    KeyboardVisibilityDetector.listen(activity) {
      animateHeight(activity, it)
    }
  }

  private fun animateHeight(activity: Activity, event: KeyboardVisibilityChanged) {
    val contentViewGroup = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
    contentViewGroup.setHeight(event.contentHeightBeforeResize)

    heightAnimator.cancel()

    heightAnimator = ObjectAnimator.ofInt(event.contentHeightBeforeResize, event.contentHeight).apply {
      interpolator = FastOutSlowInInterpolator()
      duration = 300
    }
    heightAnimator.addUpdateListener { contentViewGroup.setHeight(it.animatedValue as Int) }
    heightAnimator.start()
  }

  private fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
  }
}
