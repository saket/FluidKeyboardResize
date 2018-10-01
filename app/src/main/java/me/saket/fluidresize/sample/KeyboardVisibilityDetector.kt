package me.saket.fluidresize.sample

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

class KeyboardVisibilityDetector {

  fun KeyboardVisibilityDetector(activity: Activity, statusBarHeight: Int) {
    val rootResizableLayout = getWindowRootResizableLayout(activity)
    val rootNonResizableLayout = rootResizableLayout.parent as View

    rootResizableLayout.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener {
      private var previousHeight: Int = -1

      override fun onPreDraw(): Boolean {
        detect()
        return true
      }

      private fun detect() {
        val contentHeight = rootNonResizableLayout.height

        if (contentHeight == previousHeight) {
          return
        }
        if (previousHeight == -1) {
          return
        }

        rootResizableLayout.windowinsets

        previousHeight = contentHeight
      }
    })

//    val subscriber = { emitter ->
//      val layoutListener = {
//        val activityContentHeight = rootResizableLayout.height
//        if (activityContentHeight == activityContentHeightPrevious) {
//          return
//        }
//
//        if (activityContentHeightPrevious == -1) {
//          activityContentHeightPrevious = rootNonResizableLayout.height - statusBarHeight
//        }
//
//        val isKeyboardVisible = activityContentHeight < rootNonResizableLayout.height - statusBarHeight
//        emitter.onNext(KeyboardVisibilityChangeEvent.create(isKeyboardVisible, activityContentHeightPrevious, activityContentHeight))
//
//        activityContentHeightPrevious = activityContentHeight
//      }
//      rootResizableLayout.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
//      emitter.setCancellable({ rootResizableLayout.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener) })
//      rootResizableLayout.post { layoutListener.onGlobalLayout() }      // Initial value.
//    }
//
//    keyboardVisibilityChanges = Observable.create<KeyboardVisibilityChangeEvent>(subscriber)
//        .distinctUntilChanged()
//        .share()
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
