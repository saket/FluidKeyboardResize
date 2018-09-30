package me.saket.baseandroidproject

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
class MainActivity : AppCompatActivity() {

  private val contentView by bindView<View>(R.id.contentview)
  private val keyboardView by bindView<View>(R.id.keyboard)

  private val onDestroy = PublishSubject.create<Any>()
  private val smoothly = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    RxView.clicks(contentView)
        .observeOn(mainThread())
        .takeUntil(onDestroy)
        .scan(false) { keyboardVisible, _ -> keyboardVisible.not() }
        .subscribe { show ->
          if (show) {
            showKeyboard()
          } else {
            hideKeyboard()
          }
        }
  }

  override fun onDestroy() {
    onDestroy.onNext(Any())
    super.onDestroy()
  }

  private fun showKeyboard() {
    val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
    params.bottomMargin = 150.dp
    
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

  private fun hideKeyboard() {
    val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
    params.bottomMargin = 24.dp

    keyboardView.animate()
        .alpha(0F)
        .translationY(keyboardView.height / 4F)
        .setInterpolator(FastOutSlowInInterpolator())
        .withEndAction { keyboardView.visibility = View.GONE }
        .start()
  }
}

private val Number.dp: Int
  get() {
    val metrics = Resources.getSystem().displayMetrics
    return (this.toInt() * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
  }
