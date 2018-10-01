package me.saket.fluidresize

import android.content.res.Resources
import android.view.View
import android.view.ViewTreeObserver
import timber.log.Timber

/**
 * Run a function when a View gets measured and laid out on the screen.
 */
fun View.onNextMeasure(runnable: () -> Unit) {
  if (isInEditMode || isLaidOut) {
    runnable()
    return
  }

  viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
    override fun onPreDraw(): Boolean {
      if (isLaidOut) {
        viewTreeObserver.removeOnPreDrawListener(this)
        runnable()

      } else if (visibility == View.GONE) {
        Timber.w("View's visibility is set to Gone. It'll never be measured: %s", resourceName())
        viewTreeObserver.removeOnPreDrawListener(this)
      }
      return true
    }
  })
}

fun View.resourceName(): String {
  var name = "<nameless>"
  try {
    name = resources.getResourceEntryName(id)
  } catch (e: Resources.NotFoundException) {
    // Nothing to see here
  }
  return name
}

