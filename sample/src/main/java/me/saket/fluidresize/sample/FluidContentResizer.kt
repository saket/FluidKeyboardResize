package me.saket.fluidresize.sample

import android.app.Activity
import android.transition.*
import android.view.View
import android.view.ViewGroup
import me.saket.fluidresize.ActivityViewHolder
import me.saket.fluidresize.KeyboardVisibilityChanged
import me.saket.fluidresize.KeyboardVisibilityDetector

object FluidContentResizer {

  fun listen(activity: Activity) {
    val viewHolder = ActivityViewHolder.createFrom(activity)

    KeyboardVisibilityDetector.listen(viewHolder) {
      animateHeight(viewHolder, it)
    }
    viewHolder.onDetach {
      viewHolder.contentView.clearAnimation()
    }
    viewHolder.contentView.apply {
      post {
        setHeight(viewHolder.resizableLayout.height)
      }
    }
  }

  private fun animateHeight(viewHolder: ActivityViewHolder, event: KeyboardVisibilityChanged) {
    val contentView = viewHolder.contentView
    contentView.setHeight(event.contentHeightBeforeResize)

    val transition = ChangeBounds()
    val sceneRoot = contentView.parent as ViewGroup
    TransitionManager.beginDelayedTransition(sceneRoot, transition)

    contentView.setHeight(event.contentHeight)
  }

  private fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
  }
}
