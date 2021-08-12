package com.br.cambio.customviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.view.ContextThemeWrapper
import com.br.cambio.R
import com.br.cambio.customviews.FormItem.Companion.FIELD_TYPE_NONE
import com.google.android.material.textfield.TextInputLayout

abstract class FormField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr),
    FormItem {

    companion object {
        const val ERROR_TYPE_UNDER = 0
        const val ERROR_TYPE_SNACKBAR_ = 1
        const val ERROR_TYPE_TOOLTIP = 2

        @android.support.annotation.StyleRes
        const val STYLE_DEFAULT = R.style.EditTextMain

        const val EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        const val TEXT_DEFAULT_INCOME = "0"
    }

    override var fieldType: Int = FIELD_TYPE_NONE
    override var validField: Boolean = true
    override var errorType: Int = ERROR_TYPE_UNDER
    override var animateError: Boolean = true
    override var shouldTagValidationError: Boolean = false
    override var validationErrorTag: String? = null
    override var isOptional: Boolean = false

    var isWarning: Boolean = false
    var shouldValidateOnFocusChange: Boolean = true
    var hintFontFamily: Int = FONT_LIGHT
    var textFontFamily: Int = FONT_REGULAR
    var editTextToConfirm: Int = 0
    var confirmationField: Int = 0
    var hideTextOnError: Int = 0
    var textToConfirm: String? = null
    var maxLength: Int = 0
    var placeholder: String? = null
    var forcedPlaceholder: Boolean = false
    @android.support.annotation.StyleRes
    var style: Int = STYLE_DEFAULT

    init {
        setupStyleables(attrs)
        setupView()
    }

    internal open fun setupStyleables(attrs: AttributeSet?) {
        getStyledAttributes(attrs).apply {
            try {
                fieldType = getInteger(R.styleable.FormField_fieldType, FIELD_TYPE_NONE)
                errorType = getInteger(R.styleable.FormField_errorType, ERROR_TYPE_UNDER)
                shouldValidateOnFocusChange = getBoolean(R.styleable.FormField_shouldValidateOnFocusChange, true)
                hintFontFamily = getInteger(R.styleable.FormField_hintFontFamily, FONT_LIGHT)
                textFontFamily = getInteger(R.styleable.FormField_textFontFamily, FONT_REGULAR)
                style = getResourceId(R.styleable.FormField_editTextStyle, STYLE_DEFAULT)
                maxLength = getInteger(R.styleable.FormField_maxLength, 0)
                shouldTagValidationError = getBoolean(R.styleable.FormField_shouldTagValidationError, false)
                isOptional = getBoolean(R.styleable.FormField_isOptional, false)
            } finally {
                recycle()
            }
        }
    }

    private fun getStyledAttributes(attrs: AttributeSet?): TypedArray {
        return context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FormField,
            0, 0)
    }

    /**
     * Configura a view setando automaticamente todas as configurações necessárias para
     * acessibilidade, tamanho máximo de caracteres, tipo de campo, navegação, fonte, style,
     * tratamento de erros, foco, validação, etc.
     */
    internal open fun setupView(isSpinner: Boolean? = null) {
        addView(createEditText(isSpinner).apply {
            setLayoutParams()
            setSingleLine()
        },0)

        setupEditText(null)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupItemSpacing()
    }

    /**
     * Cria e configura o EditText no qual o FormField utilizará.
     */
    internal open fun setupEditText(fieldType: Int? = null) {
        /**
         * Se o fieldType for nāo nulo, o usuário passou programaticamente o novo tipo, e ele é setado na variável fieldType
         */
        fieldType?.let { this.fieldType = it }

        /**
         * Estes métodos devem ser executados APÓS o addView, pois necessitam que o editText já
         * tenha sido criado e faça parte do TextInputLayout.
         *//**
         * NÃO ALTERAR A ORDEM DESSES MÉTODOS
         */
        setAllowedDigits()
        setInputType()
        setMaxLength(maxLength)
        setImeOptions()
    }

    private fun createEditText(isSpinner: Boolean?): EditText {
        return TextInputEditText(ContextThemeWrapper(context, style), null, 0, isSpinner)
    }

    private fun EditText.setLayoutParams() {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT)
    }

    private fun setImeOptions(isLastField: Boolean = false) {
        editText?.imeOptions = if (!isLastField) {
            EditorInfo.IME_ACTION_NEXT
        } else {
            EditorInfo.IME_ACTION_DONE
        }
    }

    open fun setText(text: String?, filterByCode: Boolean = false) {
        editText?.setText(text?.replaceNotApplicable()?.replaceUnknown())
    }

    fun setTextWithoutHintAnimation(text: String?) {
        isHintAnimationEnabled = false
        setText(text)
        isHintAnimationEnabled = true
    }
}