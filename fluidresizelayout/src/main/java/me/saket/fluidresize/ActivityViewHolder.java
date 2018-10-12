package me.saket.fluidresize;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

final class ActivityViewHolder {

    private final ViewGroup nonResizableLayout;
    private final ViewGroup resizableLayout;
    private final ViewGroup contentView;

    public static ActivityViewHolder createFrom(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup contentView = decorView.findViewById(Window.ID_ANDROID_CONTENT);
        ViewGroup actionBarRootLayout = (ViewGroup) contentView.getParent();
        ViewGroup resizableLayout = (ViewGroup) actionBarRootLayout.getParent();

        return new ActivityViewHolder(decorView, resizableLayout, contentView);
    }

    public ViewGroup getNonResizableLayout() {
        return nonResizableLayout;
    }

    public ViewGroup getResizableLayout() {
        return resizableLayout;
    }

    public ViewGroup getContentView() {
        return contentView;
    }

    public void onDetach(Runnable onDetach) {
        nonResizableLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                // ignored
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                onDetach.run();
            }
        });
    }

    private ActivityViewHolder(ViewGroup nonResizableLayout, ViewGroup resizableLayout, ViewGroup contentView) {
        this.nonResizableLayout = nonResizableLayout;
        this.resizableLayout = resizableLayout;
        this.contentView = contentView;
    }
}