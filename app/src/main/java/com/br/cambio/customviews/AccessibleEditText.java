package com.br.cambio.customviews;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.appcompat.widget.AppCompatEditText;

import com.br.cambio.R;

public final class AccessibleEditText extends AppCompatEditText {

    interface AccessibilityContent {

        String getFieldTitle();

        String getFieldValue();

        String getPrefix();

        String getDescription();

        String getHint();

        Boolean isRequired();
    }

    private AccessibilityContent accessibilityContent = null;

    public void setAccessibilityContent(AccessibilityContent accessibilityContent) {
        this.accessibilityContent = accessibilityContent;
    }

    public AccessibleEditText(Context context) {
        super(context);
    }

    public AccessibleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccessibleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);

        String customText = setUpCustomTalkbackText(
                accessibilityContent.getFieldTitle(),
                accessibilityContent.getFieldValue(),
                accessibilityContent.getPrefix(),
                accessibilityContent.getDescription(),
                accessibilityContent.isRequired(),
                accessibilityContent.getHint()
        );

        info.setText(customText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            info.setHintText(null);
        }
    }

    public String setUpCustomTalkbackText(String fieldTitle, String fieldValue, String prefix,
            String description, Boolean isRequired, String hint) {
        String completeValue = String.format("%s %s", prefix, fieldValue);
        String requiredText = getResources().getString(R.string.required_field_text);

        StringBuilder customText;
        if (TextUtils.isEmpty(fieldValue)) {
            customText = this
                    .fillInWhenFieldValueIsEmpty(fieldTitle, prefix, hint, description, isRequired,
                            requiredText);
        } else {
            customText = this
                    .fillInWhenFieldValueIsNotEmpty(completeValue, fieldTitle, description);
        }

        return customText.toString();
    }

    private StringBuilder fillInWhenFieldValueIsNotEmpty(String completeValue, String fieldTitle,
            String description) {

        StringBuilder customText = new StringBuilder();
        customText.append(completeValue);

        if (!TextUtils.isEmpty(fieldTitle)) {
            customText.append(", ");
            customText.append(fieldTitle);
        }

        if (!TextUtils.isEmpty(description)) {
            customText.append(". ");
            customText.append(description);
        }
        return customText;
    }

    private StringBuilder fillInWhenFieldValueIsEmpty(String fieldTitle, String prefix, String hint,
            String description,
            Boolean isRequired, String requiredText) {

        StringBuilder customText = new StringBuilder();

        if (!TextUtils.isEmpty(fieldTitle)) {
            customText.append(fieldTitle);
        }

        if (!TextUtils.isEmpty(prefix)) {
            customText.append(". ");
            customText.append(prefix);
        }

        if (!TextUtils.isEmpty(hint)) {
            customText.append(". ");
            customText.append(hint);
        }

        if (!TextUtils.isEmpty(description)) {
            customText.append(". ");
            customText.append(description);
        }

        if (isRequired) {
            customText.append(". ");
            customText.append(requiredText);
        }
        return customText;
    }
}
