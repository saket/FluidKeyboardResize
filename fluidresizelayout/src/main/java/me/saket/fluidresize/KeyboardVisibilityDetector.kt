package me.saket.fluidresize

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

object KeyboardVisibilityDetector {

  fun listen(activity: Activity, listener: (KeyboardVisibilityChanged) -> Unit): Disposable {
    val rootResizableLayout = getWindowRootResizableLayout(activity)
    val rootNonResizableLayout = rootResizableLayout.parent as View

    val preDrawListener = object : ViewTreeObserver.OnPreDrawListener {
      private var previousHeight: Int = -1

      override fun onPreDraw(): Boolean {
        val detected = detect()

        // The layout flickers for a moment, usually on the first
        // animation. Intercepting this pre-draw seems to solve the problem.
        return detected.not()
      }

      private fun detect(): Boolean {
        val contentHeight = rootResizableLayout.height
        if (contentHeight == previousHeight) {
          return false
        }

        if (previousHeight != -1) {
          val statusBarHeight = rootResizableLayout.top
          val isKeyboardVisible = contentHeight < rootNonResizableLayout.height - statusBarHeight

          listener(KeyboardVisibilityChanged(
              visible = isKeyboardVisible,
              contentHeight = contentHeight,
              contentHeightBeforeResize = previousHeight))
        }

        previousHeight = contentHeight
        return true
      }
    }
    rootResizableLayout.viewTreeObserver.addOnPreDrawListener(preDrawListener)

    return Disposable {
      rootResizableLayout.viewTreeObserver.removeOnPreDrawListener(preDrawListener)
    }
  }

  /**
   * TODO: can I directly find android.R.id.content?
   *
   * DecorView <- does not get resized and contains space for system Ui bars.
   * - LinearLayout <- does not get resized and contains space for only the status bar.
   * -- Activity content <- gets resized.
   */
  private fun getWindowRootResizableLayout(activity: Activity): View {
    val decorView = activity.window.decorView as ViewGroup
    val contentContainer = decorView.getChildAt(0) as ViewGroup
    return contentContainer.getChildAt(1)
  }
}
