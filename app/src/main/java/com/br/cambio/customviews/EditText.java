package com.br.cambio.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.br.cambio.R;

import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public final class EditText extends RelativeLayout {

    private TextView title;
    private TextView prefix;
    private TextView icon;
    private TextView smallIcon;
    private TextView errorIcon;
    private TextView description;
    private LinearLayout iconAlignmentContainer;
    private AccessibleEditText edit;
    private View line;
    private Action0 iconListener;

    private final Action1<Boolean> onTextEmpty = new Action1<Boolean>() {
        @Override
        public void invoke(Boolean isTextEmpty) {
            final ColorStateList prefixColor =
                    isTextEmpty ? edit.getHintTextColors() : edit.getTextColors();
            prefix.setTextColor(prefixColor);
        }
    };

    private final OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (error) {
                ViewUtils.setTextAppearance(title, errorTextAppearance);
            } else {
                ViewUtils.setTextAppearance(title,
                        hasFocus ? focusedTitleTextAppearance : titleTextAppearance);

                ColorStateList color = hasFocus ? focusedTitleColor : titleColor;
                title.setTextColor(color);

                color = hasFocus ? focusedLineColor : lineColor;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    line.setBackgroundTintList(color);
                } else {
                    ViewCompat.setBackgroundTintList(line, color);
                }

                int height = hasFocus ? focusedLineHeight : lineHeight;
                changeLineHeight(height);
            }
        }
    };

    @Nullable
    private ColorStateList errorColor = null;
    @Nullable
    private ColorStateList titleColor = null;
    @Nullable
    private ColorStateList focusedTitleColor = null;
    @Nullable
    private ColorStateList focusedLineColor = null;
    @Nullable
    private ColorStateList lineColor = null;
    @Nullable
    private ColorStateList background = null;
    @Nullable
    private ColorStateList disabledBackground = null;
    @Nullable
    private Locale currentCurrencyLocale = null;
    private CurrencyTextWatcher currentWatcher = null;
    private boolean error = false;
    private boolean isRequired = false;
    private int errorLineHeight;
    private int errorTextAppearance;
    private int focusedTitleTextAppearance;
    private int titleTextAppearance;
    private int lineHeight;
    private int focusedLineHeight;
    private CharSequence contentDescription;
    private String descriptionText;
    private String descriptionErrorText;

    public EditText(Context context) {
        this(context, null);
        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    public EditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, R.style.EditText);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        initViews(context);
        initStyledAttributes(context, attrs, defStyleAttr, defStyleRes);
        setupEditText();
    }

    private void initViews(Context context) {
        View.inflate(context, R.layout.edittext, this);

        title = findViewById(R.id.edittext_title);
        prefix = findViewById(R.id.edittext_prefix);
        icon = findViewById(R.id.edittext_icon);
        smallIcon = findViewById(R.id.edittext_small_icon);
        iconAlignmentContainer = findViewById(R.id.icon_alignment_container);
        edit = findViewById(R.id.edittext_edit);
        line = findViewById(R.id.edittext_line);
        errorIcon = findViewById(R.id.edittext_error_icon);
        description = findViewById(R.id.edittext_description);
    }

    private void initStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr,
                                      int defStyleRes) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.EditText, defStyleAttr, defStyleRes);
        setup(typedArray);
        typedArray.recycle();
    }

    private String getFieldName() {
        String fieldTitle = title.getText().toString();
        if (!TextUtils.isEmpty(fieldTitle)) {
            return fieldTitle;
        }

        if (edit.getContentDescription() != null) {
            String fieldContentDescription = edit.getContentDescription().toString();
            if (!TextUtils.isEmpty(fieldContentDescription)) {
                return fieldContentDescription;
            }
        }

        if (edit.getHint() != null) {
            String fieldHint = edit.getHint().toString();
            if (!TextUtils.isEmpty(fieldHint)) {
                return fieldHint;
            }
        }

        return null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        edit.setSelection(getText().length());
    }

    public void setSmallIcon(@Nullable String iconText, String contentDescription) {
        icon.setVisibility(View.GONE);
        smallIcon.setVisibility(View.VISIBLE);

        this.smallIcon.setText(iconText);
        this.smallIcon.setContentDescription(contentDescription);
        this.iconAlignmentContainer.setVisibility(TextUtils.isEmpty(iconText) ?
                View.GONE : View.VISIBLE);
    }

    /**
     * @return the current currency locale used for currency mask {@link Locale}.
     */
    @Nullable
    public Locale getCurrencyLocale() {
        return currentCurrencyLocale;
    }

    /**
     * Sets a {@link Locale} as a string following the pattern "en_US" to build a currency mask
     *
     * @param stringLocale the {@link Locale} that will be set.
     */
    public void setCurrencyLocale(@Nullable final String stringLocale) {
        edit.removeTextChangedListener(currentWatcher);

        if (TextUtils.isEmpty(stringLocale)) {
            return;
        }

        final Locale locale = parseLocale(stringLocale);
        if (locale == null) {
            return;
        }
        setCurrencyLocale(locale);
    }

    /**
     * Sets a {@link Locale} to to build a currency mask
     *
     * @param locale the {@link Locale} that will be set.
     */
    public void setCurrencyLocale(@Nullable final Locale locale) {
        edit.removeTextChangedListener(currentWatcher);

        if (locale == null) {
            return;
        }
        currentCurrencyLocale = locale;

        currentWatcher = new CurrencyTextWatcher(edit);
        edit.addTextChangedListener(currentWatcher);

        if (TextUtils.isEmpty(prefix.getText())) {
            setPrefix(currentWatcher.getCurrencySymbol());
        }
    }

    /**
     * Sets a {@link String} to be displayed {@link String Description}.
     *
     * @param description the {@link String Description} that will be set.
     */
    public void setDescription(@Nullable String description) {
        descriptionText = description;
        setError(false);
        updateDescription();
    }

    /**
     * Sets a {@link Dimension} to sets a text size to both the text and prefix
     *
     * @param size the {@link Float text size of description TextView} that will be set.
     */
    public void setDEscriptionSize(@Dimension float size) {
        this.description.setTextSize(size);
    }

    /**
     * Sets a {@link String} to be displayed {@link String Description}.
     *
     * @param description the {@link String Description} that will be set.
     */
    public void setDescriptionError(@Nullable String description) {
        descriptionErrorText = description;
        setError(true);
        updateDescription();
    }

    private void updateDescription() {
        if (isError()) {
            if (!TextUtils.isEmpty(descriptionErrorText)) {
                this.description.setText(descriptionErrorText);
                this.description.setVisibility(VISIBLE);
                this.errorIcon.setVisibility(VISIBLE);
                this.description.setTextColor(getResources().getColor(R.color.purple_531E6D));
            } else {
                this.description.setVisibility(GONE);
                this.errorIcon.setVisibility(GONE);
            }
        } else {
            this.errorIcon.setVisibility(GONE);
            if (!TextUtils.isEmpty(descriptionText)) {
                this.description.setVisibility(VISIBLE);
                this.description.setText(descriptionText);
                this.description.setTextColor(getResources().getColor(R.color.gray_252220));
            } else {
                this.description.setText("");
                this.description.setVisibility(GONE);
            }
        }
    }

    /**
     * @return if the view is current on error state {@link Error TextAppearance}.
     */
    public boolean isError() {
        return error;
    }

    /**
     * Sets a {@link Boolean} to the view error state, if true it changes the line and title colors {@link Error TextAppearance}.
     *
     * @param error the {@link Error TextAppearance} that will be set.
     */
    public void setError(boolean error) {
        setError(error, null);
    }

    /**
     * Sets error state this component and description message (Nullable)
     *
     * @param error true change error state, false otherwise
     * @param description String of description message
     */
    public void setError(boolean error, @Nullable String description) {
        this.error = error;
        if (description == null) {
            if (error) {
                description = this.descriptionErrorText;
            } else {
                description = this.descriptionText;
            }
        }

        if (error) {
            descriptionErrorText = description;
            updateDescription();

            // Change other Views
            ViewUtils.setTextAppearance(this.title, errorTextAppearance);
            changeLineHeight(this.errorLineHeight);
            this.title.setTextColor(errorColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.line.setBackgroundTintList(errorColor);
            } else {
                ViewCompat.setBackgroundTintList(line, errorColor);
            }
        } else {
            descriptionText = description;
            updateDescription();

            // Change other Views
            onFocusChangeListener.onFocusChange(edit, edit.hasFocus());
        }
    }

    /**
     * Sets a {@link Boolean} that indicates that this field is required (form required).
     * This is important for Accessibility to indicate that the field is empty but is required
     *
     * @param required the {@link Boolean Required} that will be set.
     */
    public void setIsRequired(boolean required) {
        this.isRequired = required;
    }

    /**
     * Sets a {@link StringRes} to be displayed {@link String Prefix}.
     *
     * @param prefix the {@link String Prefix} that will be set.
     */
    public void setPrefix(@StringRes int prefix) {
        setPrefix(getResources().getString(prefix));
    }

    /**
     * @return the current {@link String Prefix}.
     */
    @NonNull
    public String getPrefix() {
        return prefix.getText().toString();
    }

    /**
     * Sets a {@link String} to be displayed {@link String Prefix}.
     *
     * @param prefix the {@link String Prefix} that will be set.
     */
    public void setPrefix(@Nullable String prefix) {
        this.prefix.setText(prefix);
        this.prefix.setVisibility(TextUtils.isEmpty(prefix) ?
                View.GONE : View.VISIBLE);

        onTextEmpty.invoke(TextUtils.isEmpty(edit.getText()));
    }

    /**
     * Sets a {@link Dimension} to sets a text size to both the text and prefix
     *
     * @param prefixSize the {@link Float text size of prefix TextView} that will be set.
     */
    public void setPrefixSize(@Dimension float prefixSize) {
        this.prefix.setTextSize(prefixSize);
    }

    /**
     * Sets a {@link StringRes} to be displayed in {@link String EditText}.
     *
     * @param text the {@link String EditText} that will be set.
     */
    public void setText(@StringRes int text) {
        setText(getResources().getString(text));
    }

    /**
     * @return the current text on the internal {@link String value of EditText}.
     */
    public String getText() {
        return this.edit.getText().toString();
    }

    /**
     * Sets a {@link String} to be displayed in {@link String value to EditText}.
     *
     * @param text the {@link String value to EditText} that will be set.
     */
    public void setText(@Nullable String text) {
        this.edit.setText(text);
    }

    /**
     * Sets a {@link StringRes} to be displayed in {@link String Hint of EditText}.
     *
     * @param hint the {@link String Hint of EditText} that will be set.
     */
    public void setHint(@StringRes int hint) {
        setHint(getResources().getString(hint));
    }

    /**
     * @return the current hint on the internal {@link String Hint of EditText}.
     */
    @NonNull
    public String getHint() {
        return TextUtils.isEmpty(edit.getHint()) ? "" : edit.getHint().toString();
    }

    /**
     * Sets a {@link String} to be displayed in {@link String Hint of EditText}.
     *
     * @param hint the {@link String Hint of EditText} that will be set.
     */
    public void setHint(@Nullable String hint) {
        this.edit.setHint(hint);
    }

    /**
     * Sets a {@link StringRes} to be displayed in {@link String value to EditText}.
     *
     * @param title the {@link String value to EditText} that will be set.
     */
    public void setTitle(@StringRes int title) {
        setTitle(getResources().getString(title));
    }

    /**
     * @return the current title
     */
    @NonNull
    public String getTitle() {
        return title.getText().toString();
    }

    /**
     * Sets a {@link String} to be displayed in {@link String value to EditText}.
     *
     * @param title the {@link String value to EditText} that will be set.
     */
    public void setTitle(@Nullable String title) {
        this.title.setText(title);
        this.title.setVisibility(TextUtils.isEmpty(title) ?
                View.GONE : View.VISIBLE);
    }

    /**
     * Sets a {@link String} to be displayed in {@link String value to Icon}.
     *
     * @param icon the {@link String value to Icon} that will be set.
     */
    public void setIcon(@Nullable String icon, String contentDescription) {
        this.icon.setText(icon);
        this.icon.setContentDescription(contentDescription);
        this.iconAlignmentContainer.setVisibility(TextUtils.isEmpty(icon) ?
                View.GONE : View.VISIBLE);
    }

    /**
     * Set a listener that will be called when the user click on icon.
     *
     * @param iconListener a listener that will be called.
     */
    public void setOnIconListener(@Nullable final Action0 iconListener) {
        this.iconListener = iconListener;
    }

    /**
     * Sets a {@link Dimension} to sets a text size of EditText
     *
     * @param textSize the {@link Float text size of EditText} that will be set.
     */
    public void setTextSize(@Dimension float textSize) {
        this.edit.setTextSize(textSize);
    }

    /**
     * Sets a {@link ColorStateList} to sets a text color to both the text and prefix
     *
     * @param color the {@link ColorStateList color text to EditText} that will be set.
     */
    public void setTextColor(@Nullable final ColorStateList color) {
        edit.removeTextChangedListener(currentWatcher);
        this.edit.setTextColor(color);
        this.prefix.setTextColor(color);
    }

    @Override
    public void setContentDescription(CharSequence contentDescription) {
        this.contentDescription = contentDescription;

        if (edit != null && this.contentDescription != null) {
            edit.setContentDescription(contentDescription);
        }
    }

    /**
     * Sets a {@link ColorStateList} to sets a hint color to be displayed on the internal EditText
     *
     * @param color the {@link ColorStateList color text to EditText} that will be set.
     */
    public void setHintTextColor(@Nullable final ColorStateList color) {
        this.edit.setHintTextColor(color);
    }

    /**
     * Sets a {@link ColorStateList} to sets a title text color.
     *
     * @param color the {@link ColorStateList color text of Title} that will be set.
     */
    public void setTitleTextColor(@Nullable final ColorStateList color) {
        this.title.setTextColor(color);
        this.titleColor = color;
    }

    /**
     * Sets a {@link ColorStateList} to sets a color to be show on the title and line when the view is on error state
     *
     * @param color the {@link ColorStateList color error to EditText value} that will be set.
     */
    public void setErrorColor(@Nullable final ColorStateList color) {
        this.errorColor = color;
    }

    /**
     * Sets a {@link Integer} to sets a height to the line when the view is on error state.
     *
     * @param height the {@link ColorStateList height to View Error above EditText} that will be set.
     */
    public void setErrorLineHeight(final int height) {
        this.errorLineHeight = height;
    }

    /**
     * Sets a {@link ColorStateList} to sets a color to the line when the view is not on the error state
     *
     * @param color the {@link ColorStateList color to View above of EditText} that will be set.
     */
    public void setLineColor(@Nullable final ColorStateList color) {
        this.lineColor = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            line.setBackgroundTintList(color);
        } else {
            ViewCompat.setBackgroundTintList(line, color);
        }
    }

    /**
     * Sets a {@link Integer} to sets a height to the line when the view is not on error state.
     *
     * @param height the {@link ColorStateList height to View above EditText} that will be set.
     */
    public void setLineHeight(final int height) {
        this.lineHeight = height;
        changeLineHeight(height);
    }

    /**
     * @return the current internal EditText to further configuration if needed
     */
    @NonNull
    public AccessibleEditText getEditText() {
        return edit;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        title.setEnabled(enabled);
        edit.setEnabled(enabled);
        prefix.setEnabled(enabled);
        icon.setEnabled(enabled);
        if (enabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackgroundTintList(background);
            } else {
                ViewCompat.setBackgroundTintList(this, background);
            }
        } else {
            setError(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackgroundTintList(disabledBackground);
            } else {
                ViewCompat.setBackgroundTintList(this, disabledBackground);
            }
            edit.setTextColor(getResources().getColor(R.color.gray_252220));

            if (edit.getText().toString().isEmpty()) {
                title.setTextColor(getResources().getColor(R.color.gray_252220));
                edit.setHintTextColor(edit.getDrawingCacheBackgroundColor());
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom + line.getMeasuredHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            line.layout(left - getPaddingStart(), line.getBottom() - line.getMeasuredHeight(),
                    right + getPaddingEnd(), line.getBottom());
        } else {
            line.layout(left - getPaddingLeft(), line.getBottom() - line.getMeasuredHeight(),
                    right + getPaddingRight(), line.getBottom());
        }
    }

    private void setup(@NonNull final TypedArray typedArray) {

        // Text Appearance
        final int textAppearance = typedArray
                .getResourceId(R.styleable.EditText_android_textAppearance, 0);
        ViewUtils.setTextAppearance(edit, textAppearance);

        titleTextAppearance = typedArray
                .getResourceId(R.styleable.EditText_titleTextAppearance, 0);
        focusedTitleTextAppearance = typedArray
                .getResourceId(R.styleable.EditText_focusedTitleTextAppearance,
                        titleTextAppearance);
        errorTextAppearance = typedArray
                .getResourceId(R.styleable.EditText_errorTitleTextAppearance,
                        focusedTitleTextAppearance);

        // Max Length
        if (typedArray.hasValue(R.styleable.EditText_android_maxLength)) {
            edit.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(typedArray
                            .getInt(R.styleable.EditText_android_maxLength, 0))
            });
        }

        // Text Size
        if (typedArray.hasValue(R.styleable.EditText_android_textSize)) {
            final float textSize = typedArray
                    .getDimensionPixelSize(R.styleable.EditText_android_textSize, 0);
            this.edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        if (typedArray.hasValue(R.styleable.EditText_prefix_size)) {
            final float prefixTextSize = typedArray
                    .getDimensionPixelSize(R.styleable.EditText_prefix_size, 0);
            this.prefix.setTextSize(TypedValue.COMPLEX_UNIT_PX, prefixTextSize);
        }

        if (typedArray.hasValue(R.styleable.EditText_description_size)) {
            final float descriptionTextSize = typedArray
                    .getDimensionPixelSize(R.styleable.EditText_description_size, 0);
            this.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, descriptionTextSize);
        }

        // Text
        setText(typedArray.getString(R.styleable.EditText_android_text));
        setTitle(typedArray.getString(R.styleable.EditText_title));
        edit.setTypeface(edit.getTypeface(),
                typedArray.getInt(R.styleable.EditText_textStyle, 0));
        setupHintAttr(typedArray);

        disabledBackground = typedArray
                .getColorStateList(R.styleable.EditText_disabledBackground);

        // Color
        if (typedArray.hasValue(R.styleable.EditText_android_background)) {
            background = typedArray
                    .getColorStateList(R.styleable.EditText_android_background);
            disabledBackground = background;
        }

        if (typedArray.hasValue(R.styleable.EditText_focusedTitleTextColor)) {
            focusedTitleColor = typedArray
                    .getColorStateList(R.styleable.EditText_focusedTitleTextColor);
        }

        setTextColor(typedArray
                .getColorStateList(R.styleable.EditText_android_textColor));
        setHintTextColor(typedArray
                .getColorStateList(R.styleable.EditText_android_textColorHint));
        setTitleTextColor(typedArray
                .getColorStateList(R.styleable.EditText_titleTextColor));
        setErrorColor(typedArray
                .getColorStateList(R.styleable.EditText_errorColor));
        setLineColor(typedArray
                .getColorStateList(R.styleable.EditText_lineColor));
        focusedLineColor = typedArray
                .getColorStateList(R.styleable.EditText_focusedLineColor);

        // Flags
        edit.setInputType(typedArray
                .getInt(R.styleable.EditText_android_inputType,
                        InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE));
        edit.setImeOptions(typedArray
                .getInt(R.styleable.EditText_android_imeOptions, EditorInfo.IME_ACTION_DONE));

        // Line
        final int defaultLineHeight = getResources()
                .getDimensionPixelSize(R.dimen.default_margin_2_dp);
        final int lineHeight = typedArray
                .getDimensionPixelSize(R.styleable.EditText_lineHeight, defaultLineHeight);

        focusedLineHeight = getResources()
                .getDimensionPixelSize(R.dimen.default_margin_2_dp);

        final int defaultErrorLineHeight = getResources()
                .getDimensionPixelSize(R.dimen.default_margin_2_dp);
        final int errorLineHeight = typedArray
                .getDimensionPixelSize(R.styleable.EditText_errorLineHeight,
                        defaultErrorLineHeight);
        setLineHeight(lineHeight);
        setErrorLineHeight(errorLineHeight);

        if (typedArray.hasValue(R.styleable.EditText_currencyLocale)) {
            setCurrencyLocale(typedArray.getString(R.styleable.EditText_currencyLocale));
        }

        if (typedArray.hasValue(R.styleable.EditText_prefix)) {
            setPrefix(typedArray.getString(R.styleable.EditText_prefix));
        }

        if (typedArray.hasValue(R.styleable.EditText_required)) {
            setIsRequired(typedArray.getBoolean(R.styleable.EditText_required, false));
        }

        if (typedArray.hasValue(R.styleable.EditText_icon_content_description)) {
            setIcon(typedArray.getString(R.styleable.EditText_icon_content_description),
                    typedArray.getString(R.styleable.EditText_icon_content_description));
        }

        /*
         Keep Descriptions and setError in this specific order, so that setError can check values
         to control elements visibilities
         */
        setDescriptionError(typedArray.getString(R.styleable.EditText_descriptionError));
        setDescription(typedArray.getString(R.styleable.EditText_description));
        setError(typedArray.getBoolean(R.styleable.EditText_error, false));

        setEnabled(typedArray.getBoolean(R.styleable.EditText_android_enabled, true));
    }

    /**
     * Only Allow Hint for API lower than 23 if contentDescription is empty
     * to keep talkback working
     */
    private void setupHintAttr(@NonNull TypedArray typedArray) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (contentDescription == null) {

                setHint(typedArray.getString(R.styleable.EditText_android_hint));
            }
        } else {
            setHint(typedArray.getString(R.styleable.EditText_android_hint));
        }
    }

    private void setupEditText() {
        setContentDescription(contentDescription);
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iconListener.invoke();
            }
        });
        edit.setOnFocusChangeListener(onFocusChangeListener);
        edit.addTextChangedListener(new EmptyTextWatcher(onTextEmpty));
        edit.setAccessibilityContent(new AccessibleEditText.AccessibilityContent() {
            @Override public String getFieldTitle() {
                return getFieldName();
            }

            @Override public String getFieldValue() {
                return edit.getText().toString();
            }

            @Override public String getPrefix() {
                return prefix.getText().toString();
            }

            @Override public String getDescription() {
                return description.getText().toString();
            }

            @Override
            public String getHint() {
                if (edit.getHint() != null) {
                    return edit.getHint().toString();
                } else {
                    return "";
                }
            }

            @Override public Boolean isRequired() {
                return isRequired;
            }
        });
    }

    private void changeLineHeight(final int height) {
        final ViewGroup.LayoutParams params = line.getLayoutParams();
        params.height = height;
    }

    @Nullable
    private Locale parseLocale(@NonNull String stringLocale) {
        final String[] localeValues = stringLocale.split("_");
        if (localeValues.length != 2) {
            return null;
        }

        return new Locale(localeValues[0], localeValues[1]);
    }
}