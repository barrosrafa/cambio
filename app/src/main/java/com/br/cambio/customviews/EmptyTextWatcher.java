package com.br.cambio.customviews;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * <p>This class check when has empty text in the EditText and triggered by the action</p>
 */
public final class EmptyTextWatcher implements TextWatcher {

    @NonNull
    private final Action1<Boolean> onEmpty;
    private Boolean isEmpty = null;

    /**
     * Default constructor
     *
     * @param onEmpty Action return Boolean. True when is empty, false otherwise
     */
    public EmptyTextWatcher(@NonNull final Action1<Boolean> onEmpty) {
        this.onEmpty = onEmpty;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isEmpty == null || isEmpty != TextUtils.isEmpty(s)) {
            isEmpty = TextUtils.isEmpty(s);
            onEmpty.invoke(isEmpty);
        }
    }
}
