package me.saket.fluidresize

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

object KeyboardVisibilityDetector {

  fun listen(activity: Activity, listener: (KeyboardVisibilityChanged) -> Unit): Disposable {
    val rootResizableLayout = getWindowRootResizableLayout(activity)
    val rootNonResizableLayout = rootResizableLayout.parent as View

    val preDrawListener = object : ViewTreeObserver.OnGlobalLayoutListener {
      private var previousHeight: Int = -1

      override fun onGlobalLayout() {
        detect()
      }

      private fun detect() {
        val contentHeight = rootResizableLayout.height
        if (contentHeight == previousHeight) {
          return
        }

        if (previousHeight != -1) {
          val statusBarHeight = rootResizableLayout.top
          val isKeyboardVisible = contentHeight < rootNonResizableLayout.height - statusBarHeight

          listener(KeyboardVisibilityChanged(
              visible = isKeyboardVisible,
              contentHeight = contentHeight,
              previousContentHeight = previousHeight))
        }

        previousHeight = contentHeight
      }
    }
    rootResizableLayout.viewTreeObserver.addOnGlobalLayoutListener(preDrawListener)

    return Disposable {
      rootResizableLayout.viewTreeObserver.removeOnGlobalLayoutListener(preDrawListener)
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
