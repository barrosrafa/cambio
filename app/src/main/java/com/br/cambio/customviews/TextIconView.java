package com.br.cambio.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;

public class TextIconView extends AppCompatTextView {

    /**
     * Instantiates a new Text icon view.
     *
     * @param context contexto da aplicação
     * @param attrs   atributo de tamanho
     */
    public TextIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            TypefaceUtil.setTypeface(this, TypefaceUtil.ICONS);
        }
    }

    /**
     * Instantiates a new Text icon view.
     *
     * @param context  contexto da aplicação
     * @param attrs    atributo de tamanho
     * @param defStyle the def style
     */
    public TextIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypefaceUtil.setTypeface(this, TypefaceUtil.ICONS);
    }

    /**
     * Instantiates a new Text icon view.
     *
     * @param context contexto da aplicação
     */
    public TextIconView(Context context) {
        super(context);
        TypefaceUtil.setTypeface(this, TypefaceUtil.ICONS);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        if (isClickable()) {
            return Button.class.getName();
        } else {
            return ImageView.class.getName();
        }
    }

}
