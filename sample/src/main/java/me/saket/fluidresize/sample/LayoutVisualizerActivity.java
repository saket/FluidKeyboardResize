package me.saket.fluidresize.sample;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import me.saket.fluidresize.R;

@SuppressLint("CheckResult")
public class LayoutVisualizerActivity extends AppCompatActivity {

    private View contentView;
    private View keyboardView;

    private final PublishSubject<Object> onDestroy = PublishSubject.create();
    private final long contentAnimDuration = 300L;
    private final long contentAnimDelay = 400L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_visualizer);

        contentView = findViewById(R.id.contentview);
        keyboardView = findViewById(R.id.keyboard);

        RxView.clicks(contentView)
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(onDestroy)
                .scan(false, (keyboardVisible, o) -> !keyboardVisible)
                .subscribe(show -> {
                    if (show) {
                        showKeyboard(true);
                    } else {
                        hideKeyboard(true);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        onDestroy.onNext(new Object());
        super.onDestroy();
    }

    private void showKeyboard(boolean smoothly) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        if (smoothly) {
            animateContentHeight(params, pxToDp(150));
        } else {
            params.bottomMargin = pxToDp(150);
            contentView.setLayoutParams(params);
        }

        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setAlpha(0F);
        ViewUtils.onNextMeasure(keyboardView, () -> {
            keyboardView.setTranslationY(keyboardView.getHeight() / 4F);
        });

        keyboardView.animate()
                .alpha(1F)
                .translationY(0F)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    private void hideKeyboard(boolean smoothly) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();

        if (smoothly) {
            animateContentHeight(params, pxToDp(24));
        } else {
            params.bottomMargin = pxToDp(24);
            contentView.setLayoutParams(params);
        }

        keyboardView.animate()
                .alpha(0F)
                .translationY(keyboardView.getHeight() / 4F)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withEndAction(() -> keyboardView.setVisibility(View.GONE))
                .start();
    }

    private void animateContentHeight(ViewGroup.MarginLayoutParams params, int targetMargin) {
        ValueAnimator paramsAnimator = ObjectAnimator.ofInt(params.bottomMargin, targetMargin);
        paramsAnimator.setDuration(contentAnimDuration)
                .setInterpolator(new FastOutSlowInInterpolator());
        paramsAnimator.setStartDelay(contentAnimDelay);
        paramsAnimator.addUpdateListener(animation -> {
            params.bottomMargin = (int) animation.getAnimatedValue();
            contentView.setLayoutParams(params);
        });
        paramsAnimator.start();
    }

    private static int pxToDp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (px * (((float) metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT));
    }
}