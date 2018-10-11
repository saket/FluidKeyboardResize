package me.saket.fluidresize.sample;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewTreeObserver;

import timber.log.Timber;

public class ViewUtils {

    /**
     * Run a function when a View gets measured and laid out on the screen.
     */
    public static void onNextMeasure(View view, Runnable runnable) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (view.isLaidOut()) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    runnable.run();
                } else if (view.getVisibility() == View.GONE) {
                    Timber.w("View's visibility is set to Gone. It'll never be measured: %s", resourceName(view));
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }

                return true;
            }
        });
    }

    private static String resourceName(View view) {
        String name = "<nameless>";
        try {
            name = view.getResources().getResourceEntryName(view.getId());
        } catch (Resources.NotFoundException e) {
            // swallow
        }
        return name;
    }

    private ViewUtils() { }
}
