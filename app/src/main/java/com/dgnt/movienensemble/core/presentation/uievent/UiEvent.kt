package com.dgnt.movienensemble.core.presentation.uievent

import androidx.annotation.StringRes

sealed interface UiEvent {
    data class SnackBar(
        @StringRes val message: Int
    ) : UiEvent
}