package com.ls.kassify

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.unit.dp
import com.amplifyframework.ui.authenticator.ui.Authenticator
import com.amplifyframework.ui.authenticator.ui.PasswordReset
import com.amplifyframework.ui.authenticator.ui.PasswordResetConfirm
import com.amplifyframework.ui.authenticator.ui.SignIn
import com.amplifyframework.ui.authenticator.ui.SignUp
import com.amplifyframework.ui.authenticator.ui.SignUpConfirm
import com.ls.kassify.ui.AuthHeaderContent
import com.ls.kassify.ui.DetailsNoticeContent
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
                    ){

                        // Authenticator bitte nicht löschen, nur bei Bedarf auskommentieren (Enthält LogIn-Screen, etc.)
                        //AWS Amplify-Authenticator UI with custom headers
                        Authenticator(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 32.dp),

                            headerContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center,
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
                                    //modifier = Modifier,
                                    state = signInState,
                                    headerContent = {
                                        AuthHeaderContent(title = R.string.amplify_ui_authenticator_title_signin)
                                    }
                                )
                            },
                            signUpContent = { signUpState ->
                                SignUp(
                                    state = signUpState,
                                    headerContent = {
                                        AuthHeaderContent(title = R.string.amplify_ui_authenticator_title_signup)
                                    }
                                )

                            },
                            passwordResetContent = { passwordResetState ->
                                PasswordReset(
                                    state = passwordResetState,
                                    headerContent = {
                                        AuthHeaderContent(title = R.string.amplify_ui_authenticator_title_password_reset)
                                    }
                                )
                            },
                            passwordResetConfirmContent = { passwordResetConfirmState ->
                                PasswordResetConfirm(
                                    state = passwordResetConfirmState,
                                    headerContent = {
                                        AuthHeaderContent(title = R.string.amplify_ui_authenticator_title_password_reset)
                                    },
                                    deliveryNoticeContent = { details ->
                                        if (details != null) {
                                            DetailsNoticeContent(details = details)
                                        }
                                    }

                                )
                            },
                            signUpConfirmContent = { signUpConfirmState ->
                                SignUpConfirm(
                                    state = signUpConfirmState,
                                    headerContent = {
                                        AuthHeaderContent(title = R.string.amplify_ui_authenticator_title_signup_confirm)
                                    },
                                    deliveryNoticeContent = { details ->
                                        if (details != null) {
                                            DetailsNoticeContent(details = details)
                                        }
                                    }
                                )

                            }
                        ) {
                            //kassify app content: first screen after sign-in is the TransactionListScreen
                            KassifyApp()
                        }
                    }
                }
            }
        }
    }
}
