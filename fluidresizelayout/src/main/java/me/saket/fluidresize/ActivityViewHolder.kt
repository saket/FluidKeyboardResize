package me.saket.fluidresize

import android.view.View
import android.view.ViewGroup

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
