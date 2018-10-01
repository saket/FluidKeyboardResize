package me.saket.fluidresize

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import me.saket.fluidresize.internal.Timber

class FluidResizeFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

  private lateinit var disposable: Disposable
  private var heightAnimator: ValueAnimator = ObjectAnimator()

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()

    // TODO: remove the need for Activity context
    val activity = context as Activity

    disposable = KeyboardVisibilityDetector.listen(activity) { event ->
      Timber.i("-------------------------------")
      Timber.i("Keyboard visible? ${event.visible}")
      Timber.i("Content height: ${event.contentHeight}")
      Timber.i("Content height: ${event.previousContentHeight}")
      animateHeight(event)
    }
  }

  override fun onDetachedFromWindow() {
    disposable.dispose()
    super.onDetachedFromWindow()
  }

  private fun animateHeight(event: KeyboardVisibilityChanged) {
    heightAnimator.cancel()

    setHeight(this, event.previousContentHeight)

    heightAnimator = ObjectAnimator.ofInt(event.previousContentHeight, event.contentHeight)
    heightAnimator.addUpdateListener { animation ->
      setHeight(this, animation.animatedValue as Int)
    }
    heightAnimator.interpolator = FastOutSlowInInterpolator()
    heightAnimator.duration = 200
    heightAnimator.start()
  }

  private fun setHeight(view: View, height: Int) {
    val params = view.layoutParams
    params.height = height
    view.layoutParams = params
  }
}
