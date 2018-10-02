package me.saket.fluidresize

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window

data class ActivityViewHolder(
    val decorView: ViewGroup,
    val contentViewFrame: ViewGroup,
    val contentView: ViewGroup
) {

  companion object {

    /**
     * DecorView <- does not get resized, contains space for system Ui bars.
     * - LinearLayout
     * -- FrameLayout <- gets resized
     * --- LinearLayout
     * ---- Activity content
     */
    fun createFrom(activity: Activity): ActivityViewHolder {
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

  fun onDetach(onDetach: () -> Unit) {
    decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
      override fun onViewDetachedFromWindow(v: View?) {
        onDetach()
      }

      override fun onViewAttachedToWindow(v: View?) {}
    })
  }
}
