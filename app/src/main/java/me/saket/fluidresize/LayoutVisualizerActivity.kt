package me.saket.fluidresize

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView

@SuppressLint("CheckResult")
class LayoutVisualizerActivity : AppCompatActivity() {

  private val contentView by bindView<View>(R.id.contentview)
  private val keyboardView by bindView<View>(R.id.keyboard)

  private val onDestroy = PublishSubject.create<Any>()
  private val contentAnimDuration = 300L
  private val contentAnimDelay = 400L

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    RxView.clicks(contentView)
        .observeOn(mainThread())
        .takeUntil(onDestroy)
        .scan(false) { keyboardVisible, _ -> keyboardVisible.not() }
        .subscribe { show ->
          if (show) {
            showKeyboard(smoothly = true)
          } else {
            hideKeyboard(smoothly = true)
          }
        }
  }

  override fun onDestroy() {
    onDestroy.onNext(Any())
    super.onDestroy()
  }


  private fun showKeyboard(smoothly: Boolean = true) {
    val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
    if (smoothly) {
      animateContentHeight(params, 150.dp)
    } else {
      params.bottomMargin = 150.dp
      contentView.layoutParams = params
    }

    keyboardView.visibility = View.VISIBLE
    keyboardView.alpha = 0F
    keyboardView.onNextMeasure {
      keyboardView.translationY = keyboardView.height / 4F
    }

    keyboardView.animate()
        .alpha(1F)
        .translationY(0F)
        .setInterpolator(FastOutSlowInInterpolator())
        .start()
  }

  private fun hideKeyboard(smoothly: Boolean = true) {
    val params = contentView.layoutParams as ViewGroup.MarginLayoutParams

    if (smoothly) {
      animateContentHeight(params, 24.dp)
    } else {
      params.bottomMargin = 24.dp
      contentView.layoutParams = params
    }

    keyboardView.animate()
        .alpha(0F)
        .translationY(keyboardView.height / 4F)
        .setInterpolator(FastOutSlowInInterpolator())
        .withEndAction { keyboardView.visibility = View.GONE }
        .start()
  }

  private fun animateContentHeight(params: ViewGroup.MarginLayoutParams, targetMargin: Int) {
    val paramsAnimator = ObjectAnimator.ofInt(params.bottomMargin, targetMargin).apply {
      duration = contentAnimDuration
      interpolator = FastOutSlowInInterpolator()
      startDelay = contentAnimDelay
    }
    paramsAnimator.addUpdateListener {
      params.bottomMargin = it.animatedValue as Int
      contentView.layoutParams = params
    }
    paramsAnimator.start()
  }
}

private val Number.dp: Int
  get() {
    val metrics = Resources.getSystem().displayMetrics
    return (this.toInt() * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
  }
