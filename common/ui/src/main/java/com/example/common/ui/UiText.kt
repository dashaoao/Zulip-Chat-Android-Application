package com.example.common.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {

    abstract fun stringValue(context: Context): String

    @Composable
    fun stringValue(): String = when (this) {
        is RawString -> value
        is Resource -> stringResource(id = id, formatArgs = args.toTypedArray())
    }

    data class RawString(val value: String) : UiText() {
        override fun stringValue(context: Context): String = value
    }

    data class Resource(@StringRes val id: Int, val args: List<Any> = emptyList()) : UiText() {
        override fun stringValue(context: Context): String = context.getString(id, args)
    }
}
