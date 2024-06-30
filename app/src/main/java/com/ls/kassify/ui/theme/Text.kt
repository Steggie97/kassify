package com.ls.kassify.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.ls.kassify.R
import com.ls.kassify.ui.theme.TextDownloadableFontsSnippet1.provider

/**
 * This file lets DevRel track changes to snippets present in
 * https://developer.android.com/jetpack/compose/text
 *
 * No action required if it's modified.
 */

private object TextDownloadableFontsSnippet1 {
    // [START android_compose_text_df_provider]
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    // [END android_compose_text_df_provider]
}

object TextDownloadableFontsSnippet2 {
    // [START android_compose_text_df_fontFamily]

    // [START_EXCLUDE]
    /**
    // [END_EXCLUDE]
    import androidx.compose.ui.text.googlefonts.GoogleFont
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.text.googlefonts.Font
    // [START_EXCLUDE]
     **/
    // [END_EXCLUDE]

    val fontName = GoogleFont("Ubuntu")


    var fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )
    // [END android_compose_text_df_fontFamily]
}

private object TextDownloadableFontsSnippet3 {
    // [START android_compose_text_df_fontFamily_style]

    // [START_EXCLUDE]
    /**
    // [END_EXCLUDE]
    import androidx.compose.ui.text.googlefonts.GoogleFont
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.text.googlefonts.Font
    // [START_EXCLUDE]
     **/
    // [END_EXCLUDE]

    val fontName = GoogleFont("Lobster Two")

    val fontFamily = FontFamily(
        Font(
            googleFont = fontName,
            fontProvider = provider,
        )
    )
}


private val fontFamily = FontFamily()
private const val TAG = ""