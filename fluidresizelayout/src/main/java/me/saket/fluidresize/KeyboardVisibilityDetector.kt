package me.saket.fluidresize

import android.app.Activity
import android.view.ViewGroup
import android.view.ViewTreeObserver

object KeyboardVisibilityDetector {

  fun listen(activity: Activity, listener: (KeyboardVisibilityChanged) -> Unit) {
    val viewHolder = findViews(activity)

    val detector = Detector(viewHolder, listener)
    viewHolder.decorView.viewTreeObserver.addOnPreDrawListener(detector)
    viewHolder.decorView.viewTreeObserver.addOnWindowAttachListener(detector)
  }

  private class Detector(
      val viewHolder: ActivityViewHolder,
      val listener: (KeyboardVisibilityChanged) -> Unit
  ) : ViewTreeObserver.OnWindowAttachListener, ViewTreeObserver.OnPreDrawListener {

    private var previousHeight: Int = -1

    override fun onPreDraw(): Boolean {
      val detected = detect()

      // The layout flickers for a moment, usually on the first
      // animation. Intercepting this pre-draw seems to solve the problem.
      return detected.not()
    }

    private fun detect(): Boolean {
      val contentHeight = viewHolder.contentView.height
      if (contentHeight == previousHeight) {
        return false
      }

      if (previousHeight != -1) {
        val statusBarHeight = viewHolder.contentView.top
        val isKeyboardVisible = contentHeight < viewHolder.decorView.height - statusBarHeight

        listener(KeyboardVisibilityChanged(
            visible = isKeyboardVisible,
            contentHeight = contentHeight,
            contentHeightBeforeResize = previousHeight))
      }

      previousHeight = contentHeight
      return true
    }

    override fun onWindowDetached() {
      viewHolder.decorView.viewTreeObserver.removeOnPreDrawListener(this)
      viewHolder.decorView.viewTreeObserver.removeOnWindowAttachListener(this)
    }

    override fun onWindowAttached() {}
  }

  data class ActivityViewHolder(val decorView: ViewGroup, val contentView: ViewGroup)

  /**
   * TODO: can I directly find android.R.id.content?
   *
   * DecorView <- does not get resized and contains space for system Ui bars.
   * - LinearLayout <- does not get resized and contains space for only the status bar.
   * -- Activity content <- gets resized.
   */
  private fun findViews(activity: Activity): ActivityViewHolder {
    val decorView = activity.window.decorView as ViewGroup
    val decorViewChild = decorView.getChildAt(0) as ViewGroup
    val contentView = decorViewChild.getChildAt(1) as ViewGroup
    return ActivityViewHolder(decorView, contentView)
  }
}
