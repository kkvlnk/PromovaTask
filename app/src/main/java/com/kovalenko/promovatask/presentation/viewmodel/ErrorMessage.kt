package com.kovalenko.promovatask.presentation.viewmodel

import androidx.annotation.StringRes

sealed class ErrorMessage {
    data class StringMessage(val message: String): ErrorMessage()
    data class ResourceMessage(@StringRes val message: Int): ErrorMessage()
}