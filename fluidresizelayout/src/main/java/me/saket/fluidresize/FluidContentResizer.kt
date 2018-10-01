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
    val viewHolder = findViews(activity)

    KeyboardVisibilityDetector.listen(viewHolder) {
      animateHeight(viewHolder, it)
    }

    viewHolder.onDetach {
      heightAnimator.cancel()
    }
  }

  private fun animateHeight(viewHolder: ActivityViewHolder, event: KeyboardVisibilityChanged) {
    val contentView = viewHolder.contentView
    contentView.setHeight(event.contentHeightBeforeResize)

    heightAnimator.cancel()

    heightAnimator = ObjectAnimator.ofInt(event.contentHeightBeforeResize, event.contentHeight).apply {
      interpolator = FastOutSlowInInterpolator()
      duration = 300
    }
    heightAnimator.addUpdateListener { contentView.setHeight(it.animatedValue as Int) }
    heightAnimator.start()
  }

  private fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
  }

  /**
   * DecorView <- does not get resized, contains space for system Ui bars.
   * - LinearLayout <- does not get resized, contains space for action mode.
   * -- FrameLayout <- gets resized, contains space for action bar.
   * --- Activity content <- gets resized.
   */
  private fun findViews(activity: Activity): ActivityViewHolder {
    val decorView = activity.window.decorView as ViewGroup
    val decorViewChild = decorView.getChildAt(0) as ViewGroup
    val contentViewFrame = decorViewChild.getChildAt(1) as ViewGroup
    val contentView = decorView.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)

    return ActivityViewHolder(
        decorView = decorView,
        contentViewFrame = contentViewFrame,
        contentView = contentView)
  }
}
