package com.dgnt.movienensemble.core.presentation.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark", showBackground = true)
annotation class Previews
