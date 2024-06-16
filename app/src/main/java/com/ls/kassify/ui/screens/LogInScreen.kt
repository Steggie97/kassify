package com.ls.kassify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ls.kassify.R
import com.ls.kassify.ui.CredentialFields


@Composable
fun LogInScreen(
    email: String,
    password: String,
    showPassword: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onShowPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSignUpButtonClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit,
    onForgotPasswordButtonClicked: () -> Unit,
    emailErrorMessage: String? = null,
    passwordErrorMessage: String? = null,
    isError: Boolean = false
) {

    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Logo
        Image(
            painter = painterResource(R.drawable.file),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(bottom = 16.dp)
        )

        CredentialFields(
            email = email,
            showPassword = showPassword,
            password = password,
            onEmailChange = { onEmailChange(it) },
            onPasswordChange = { onPasswordChange(it) },
            onShowPasswordClick = { onShowPasswordClick() },
            emailErrorMessage = emailErrorMessage,
            passwordErrorMessage = passwordErrorMessage,
            isLastField = true
        )

        //Forgot-Password-Link
        TextButton(
            onClick = { onForgotPasswordButtonClicked() },
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 14.sp
            )
        }

        // Login-Button
        Button(
            onClick = { onLoginButtonClicked() },
            enabled = !isError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = stringResource(R.string.login))
        }

        //SignUp-Button
        OutlinedButton(
            onClick = { onSignUpButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.signup))
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LogInScreenPreview() {
    LogInScreen(
        onLoginButtonClicked = {},
        onForgotPasswordButtonClicked = {},
        onSignUpButtonClicked = {},
        onPasswordChange = {},
        onEmailChange = {},
        onShowPasswordClick = {},
        email = "",
        password = "",
        showPassword = false
    )
}