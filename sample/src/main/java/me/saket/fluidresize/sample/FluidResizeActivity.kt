package me.saket.fluidresize.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fluid_resize.*
import me.saket.fluidresize.ActivityViewHolder
import me.saket.fluidresize.KeyboardVisibilityDetector
import me.saket.fluidresize.R

class FluidResizeActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fluid_resize)

    val viewHolder = ActivityViewHolder.createFrom(this)

    KeyboardVisibilityDetector.listen(viewHolder) {
      event ->
        if (event.contentHeight <= event.contentHeightBeforeResize) {
            next_button.alpha = 0f
        } else {
            next_button.animate()
                    .alpha(1f)
                    .setStartDelay(150)
                    .start()
        }
    }

      FluidContentResizer.listen(this)
  }
}
