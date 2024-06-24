package com.ls.kassify

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.unit.dp
import com.amplifyframework.ui.authenticator.PasswordResetState
import com.amplifyframework.ui.authenticator.ui.Authenticator
import com.amplifyframework.ui.authenticator.ui.PasswordReset
import com.amplifyframework.ui.authenticator.ui.SignIn
import com.amplifyframework.ui.authenticator.ui.SignUp
import com.ls.kassify.ui.KassifyApp
import com.ls.kassify.ui.theme.KassifyTheme
import kotlinx.coroutines.CoroutineExceptionHandler

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "There has been an issue: ", throwable)
        }

        setContent {
            KassifyTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(
                        LocalFontFamilyResolver provides createFontFamilyResolver(
                            LocalContext.current,
                            handler
                        )
                    ) {

                        // Authenticator bitte nicht löschen, nur bei Bedarf auskommentieren (Enthält LogIn-Screen, etc.)

                        Authenticator(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            headerContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 64.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.logo),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .padding(bottom = 32.dp)
                                    )
                                }
                            },
                            signInContent = { signInState ->
                                SignIn(
                                    state = signInState,
                                    headerContent = {
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 8.dp),
                                                horizontalArrangement = Arrangement.Center
                                            )
                                            {
                                                Text(
                                                    text = stringResource(R.string.amplify_ui_authenticator_title_signin),
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }
                                        }
                                    }
                                )
                            },
                            signUpContent = { signUpState ->
                                SignUp(
                                    state = signUpState,
                                    headerContent = {
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 8.dp),
                                                horizontalArrangement = Arrangement.Center
                                            )
                                            {
                                                Text(
                                                    text = stringResource(R.string.amplify_ui_authenticator_title_signup),
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }
                                        }
                                    }
                                )

                            },
                            passwordResetContent = { passwordResetState ->
                                PasswordReset(
                                    state = passwordResetState,
                                    headerContent = {
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 8.dp),
                                                horizontalArrangement = Arrangement.Center
                                            )
                                            {
                                                Text(
                                                    text = stringResource(R.string.amplify_ui_authenticator_title_password_reset),
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        ) { //Vorstehende Klammer kann Auskommentiert werden
                        // Anzeige des TransactionList-Screens, nach erfolgreicher Anmeldung
                            KassifyApp()
                        } // Vorstehende Klammer kann auskommentiert werden.
                    }
                }
            }
        }
    }
}

