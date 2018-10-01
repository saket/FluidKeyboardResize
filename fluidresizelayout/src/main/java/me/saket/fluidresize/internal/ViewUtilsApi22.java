package me.saket.fluidresize.internal;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViewUtilsApi22 {

  private static final String TAG = "ViewUtilsApi22";

  private static Method sSetLeftTopRightBottomMethod;
  private static boolean sSetLeftTopRightBottomMethodFetched;

  public static void setLeftTopRightBottom(View v, int left, int top, int right, int bottom) {
    fetchSetLeftTopRightBottomMethod();
    if (sSetLeftTopRightBottomMethod != null) {
      try {
        sSetLeftTopRightBottomMethod.invoke(v, left, top, right, bottom);
      } catch (IllegalAccessException e) {
        // Do nothing
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e.getCause());
      }
    }
  }

  @SuppressLint("PrivateApi")
  private static void fetchSetLeftTopRightBottomMethod() {
    if (!sSetLeftTopRightBottomMethodFetched) {
      try {
        sSetLeftTopRightBottomMethod = View.class.getDeclaredMethod("setLeftTopRightBottom",
            int.class, int.class, int.class, int.class);
        sSetLeftTopRightBottomMethod.setAccessible(true);
      } catch (NoSuchMethodException e) {
        Log.i(TAG, "Failed to retrieve setLeftTopRightBottom method", e);
      }
      sSetLeftTopRightBottomMethodFetched = true;
    }
  }

}

