package me.saket.fluidresize;

import android.view.ViewTreeObserver;

final class KeyboardVisibilityDetector {

    static void listen(ActivityViewHolder viewHolder, KeyboardVisibilityChangedListener listener) {
        Detector detector = new Detector(viewHolder, listener);
        viewHolder.getNonResizableLayout().getViewTreeObserver().addOnPreDrawListener(detector);
        viewHolder.onDetach(() -> viewHolder.getNonResizableLayout().getViewTreeObserver().removeOnPreDrawListener(detector));
    }

    private static final class Detector implements ViewTreeObserver.OnPreDrawListener {

        private final ActivityViewHolder viewHolder;
        private final KeyboardVisibilityChangedListener listener;

        private int previousHeight = -1;

        Detector(ActivityViewHolder viewHolder, KeyboardVisibilityChangedListener listener) {
            this.viewHolder = viewHolder;
            this.listener = listener;
        }

        @Override
        public boolean onPreDraw() {
            return !detect();
        }

        private boolean detect() {
            int contentHeight = viewHolder.getResizableLayout().getHeight();
            if (contentHeight == previousHeight) {
                return false;
            }

            if (previousHeight != -1) {
                int statusBarHeight = viewHolder.getResizableLayout().getTop();
                boolean isKeyboardVisible = contentHeight < viewHolder.getNonResizableLayout().getHeight() - statusBarHeight;

                listener.onKeyboardVisibilityChanged(new KeyboardVisibilityChangedListener.KeyboardVisibilityChangedEvent(
                        isKeyboardVisible, contentHeight, previousHeight
                ));
            }

            previousHeight = contentHeight;
            return true;
        }
    }
}