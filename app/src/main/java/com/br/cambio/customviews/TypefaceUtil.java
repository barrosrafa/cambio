package com.br.cambio.customviews;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitário para cachear fontes customizadas.
 */
public final class TypefaceUtil {

    public static final String ICONS = "fonts/fonts_master_24px_v28.ttf";
    private static final Map<String, Typeface> FONTS_CACHE = new HashMap<>();

    /**
     * Validação customizada para os valores de fontes.
     */
    @StringDef({ICONS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Fonts {
    }

    private TypefaceUtil() {
    }

    /**
     * Carrega uma fonte customizada usando o {@link AssetManager} e o caminho relativo.
     *
     * @param assetManager AssetManager
     * @param filePath     Caminho do arquivo de fonte dentor de assets
     * @return Objeto Typeface
     */
    public static Typeface load(final AssetManager assetManager, @Fonts final String filePath) {
        synchronized (FONTS_CACHE) {
            try {
                if (!FONTS_CACHE.containsKey(filePath)) {
                    final Typeface typeface = Typeface.createFromAsset(assetManager, filePath);
                    FONTS_CACHE.put(filePath, typeface);
                    return typeface;
                }
            } catch (Exception e) {
                FONTS_CACHE.put(filePath, null);
                return null;
            }
            return FONTS_CACHE.get(filePath);
        }
    }

    public static void setTypeface(TextView view, @Fonts String font) {

        if (view == null || TextUtils.isEmpty(font)) {
            return;
        }

        final Typeface typeface = load(view.getContext().getAssets(), font);

        if (typeface == null) {
            return;
        }

        view.setTypeface(typeface);
    }

}
