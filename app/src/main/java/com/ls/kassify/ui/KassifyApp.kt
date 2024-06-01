package com.ls.kassify.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ls.kassify.ui.theme.KassifyTheme

@Composable
fun KassifyApp() {
    ScaffoldWithTopBar()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun KassifyAppPreview() {
    KassifyTheme(darkTheme = false) {
        KassifyApp()
    }

}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun KassifyAppDarkThemePreview() {
    KassifyTheme(darkTheme = true) {
        KassifyApp()
    }
}