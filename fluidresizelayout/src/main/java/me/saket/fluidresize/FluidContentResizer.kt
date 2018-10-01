package me.saket.fluidresize

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
    KeyboardVisibilityDetector.listen(activity) { animateHeight(activity, it) }
  }

  private fun animateHeight(activity: Activity, event: KeyboardVisibilityChanged) {
    val contentViewGroup = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
    contentViewGroup.setHeight(event.contentHeightBeforeResize)
    suppress(contentViewGroup, true)

    heightAnimator.cancel()

    heightAnimator = ObjectAnimator.ofInt(event.contentHeightBeforeResize, event.contentHeight)
    heightAnimator.addUpdateListener { animation -> contentViewGroup.setHeight(animation.animatedValue as Int) }
    heightAnimator.interpolator = FastOutSlowInInterpolator()
    heightAnimator.duration = 200
    heightAnimator.addListener(object: AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator?) {
        suppress(contentViewGroup, false)
      }
    })
    heightAnimator.start()
  }

  private fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
  }

  private fun suppress(viewGroup: ViewGroup, suppress: Boolean) {
    ViewGroupUtilsApi18.suppressLayout(viewGroup, suppress)
  }
}
