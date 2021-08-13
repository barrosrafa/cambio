package com.br.cambio.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.NO_ID;
import static android.view.View.VISIBLE;

public final class ViewUtils {

    public static final String STATUS_BAR_HEIGHT_ID_NAME = "status_bar_height";
    public static final String DIMENS_RESOURCE_TYPE = "dimen";
    public static final String ANDROID_RESOURCE_PACKAGE = "android";

    private ViewUtils() {
        // Do Nothing
    }

    /**
     * Changes the textAppearance of a TextView based on the API Level.
     *
     * @param view  View to apply the textAppearance
     * @param resId TextAppearance style reference
     */
    public static void setTextAppearance(@NonNull final TextView view, @StyleRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setTextAppearance(resId);
        } else {
            view.setTextAppearance(view.getContext(), resId);
        }
    }

    /**
     * Will set the visibility to VISIBLE when it has some text on it, GONE otherwise.
     *
     * @param textView View to apply the visibility logic based on text
     */
    public static void displayWhenHasText(@Nullable final TextView textView) {
        if (textView == null) {
            return;
        }

        final String text = textView.getText().toString();
        final int visibility = text.isEmpty() ? GONE : VISIBLE;
        textView.setVisibility(visibility);
    }

    /**
     * Changes target view's margins, if it's layout supports margins.
     *
     * @param view   View to apply the changes
     * @param start  Left margin, in pixels
     * @param top    Top margin, in pixels
     * @param end    Right margin, in pixels
     * @param bottom Bottom margin, in pixels
     */
    public static void setMargins(View view, @Px int start, @Px int top, @Px int end,
            @Px int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();

            params.setMargins(start, top, end, bottom);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(start);
                params.setMarginEnd(end);
            }

            view.setLayoutParams(params);

            view.requestLayout();
        }
    }

    /**
     * @param view     The Root view to extract the reference
     * @param targetId the Id to find the View reference
     * @param <T>      The type T of the reference
     * @return The view T reference
     */
    @Nullable
    public static <T extends View> T findViewFromParent(@NonNull final View view,
            @IdRes int targetId) {
        final ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            return ((ViewGroup) parent).findViewById(targetId);
        }

        final Context context = view.getContext();
        if (context instanceof Activity) {
            return ((Activity) context).findViewById(targetId);
        }
        return null;
    }

    /**
     * Changes target view's paddings.
     *
     * @param view   View to apply the changes
     * @param start  Left padding, in pixels
     * @param top    Top padding, in pixels
     * @param end    Right padding, in pixels
     * @param bottom Bottom padding, in pixels
     */
    public static void setPaddings(View view, @Px int start, @Px int top, @Px int end,
            @Px int bottom) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setPaddingRelative(start, top, end, bottom);
        } else {
            view.setPadding(start, top, end, bottom);
        }
    }

    /**
     * Retrieves the status bar height from Android resources
     *
     * @param resources Android resources package
     */
    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        @DimenRes int resourceId = resources.getIdentifier(STATUS_BAR_HEIGHT_ID_NAME,
                DIMENS_RESOURCE_TYPE, ANDROID_RESOURCE_PACKAGE);
        if (resourceId != NO_ID) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Show soft keyboard
     *
     * @param view A view used to show the keyboard
     */
    public static void showKeyboard(@NonNull final View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    /**
     * Hide soft keyboard
     *
     * @param view A view used to hide the keyboard
     */
    public static void hideKeyboard(@NonNull final View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * This method calculate the positions view into screen
     *
     * @param view {@link View} object to calculate
     * @return {@link RectF} with positions
     */
    public static RectF calculateRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(),
                location[1] + view.getMeasuredHeight());
    }

    /**
     * This method calculate the positions view into window
     *
     * @param view {@link View} object to calculate
     * @return {@link RectF} with positions
     */
    public static RectF calculateRectInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(),
                location[1] + view.getMeasuredHeight());
    }
}