package me.saket.fluidresize;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public final class FluidContentResizer {

    private ValueAnimator heightAnimator = new ObjectAnimator();
    private Interpolator interpolator = new FastOutSlowInInterpolator();

    public void listen(Activity activity) {
        ActivityViewHolder viewHolder = ActivityViewHolder.createFrom(activity);

        KeyboardVisibilityDetector.listen(viewHolder, visibilityChangedEvent -> animateHeight(viewHolder, visibilityChangedEvent));

        viewHolder.onDetach(() -> heightAnimator.cancel());
    }

    private void animateHeight(ActivityViewHolder viewHolder, KeyboardVisibilityChangedListener.KeyboardVisibilityChangedEvent event) {
        ViewGroup contentView = viewHolder.getContentView();

        setViewHeight(contentView, event.getContentHeightBeforeResize());

        heightAnimator.cancel();

        heightAnimator = ObjectAnimator.ofInt(event.getContentHeightBeforeResize(), event.getContentHeight());
        heightAnimator.setInterpolator(interpolator);
        heightAnimator.setDuration(300);
        heightAnimator.addUpdateListener(animation -> setViewHeight(contentView, (Integer) animation.getAnimatedValue()));
        heightAnimator.start();
    }

    private void setViewHeight(ViewGroup view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
    }
}