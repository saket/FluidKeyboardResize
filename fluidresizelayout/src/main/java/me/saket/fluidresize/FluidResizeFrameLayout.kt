package me.saket.fluidresize

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout

class FluidResizeFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

  override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
    return super.onApplyWindowInsets(insets)
  }
}
