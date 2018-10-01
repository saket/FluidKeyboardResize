package me.saket.fluidresize

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

object KeyboardVisibilityDetector {

  fun listen(viewHolder: ActivityViewHolder, listener: (KeyboardVisibilityChanged) -> Unit) {
    val detector = Detector(viewHolder, listener)
    viewHolder.decorView.viewTreeObserver.addOnPreDrawListener(detector)
    viewHolder.onDetach {
      viewHolder.decorView.viewTreeObserver.removeOnPreDrawListener(detector)
    }
  }

  private class Detector(
      val viewHolder: ActivityViewHolder,
      val listener: (KeyboardVisibilityChanged) -> Unit
  ) : ViewTreeObserver.OnPreDrawListener {

    private var previousHeight: Int = -1

    override fun onPreDraw(): Boolean {
      val detected = detect()

      // The layout flickers for a moment, usually on the first
      // animation. Intercepting this pre-draw seems to solve the problem.
      return detected.not()
    }

    private fun detect(): Boolean {
      val contentHeight = viewHolder.contentViewFrame.height
      if (contentHeight == previousHeight) {
        return false
      }

      if (previousHeight != -1) {
        val statusBarHeight = viewHolder.contentViewFrame.top
        val isKeyboardVisible = contentHeight < viewHolder.decorView.height - statusBarHeight

        listener(KeyboardVisibilityChanged(
            visible = isKeyboardVisible,
            contentHeight = contentHeight,
            contentHeightBeforeResize = previousHeight))
      }

      previousHeight = contentHeight
      return true
    }
  }
}

data class ActivityViewHolder(
    val decorView: ViewGroup,
    val contentViewFrame: ViewGroup,
    val contentView: ViewGroup
) {

  fun onDetach(onDetach: () -> Unit) {
    decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
      override fun onViewDetachedFromWindow(v: View?) {
        onDetach()
      }

      override fun onViewAttachedToWindow(v: View?) {}
    })
  }
}
