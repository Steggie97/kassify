package com.ls.kassify.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ls.kassify.R
import com.ls.kassify.ui.CredentialFields

@Composable
fun LogInScreen(
    modifier: Modifier = Modifier,
    onSignUpButtonClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit,
    onForgotPasswordButtonClicked: () -> Unit
) {
    //Todo: Viewmodel
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //TODO: Logo über LogIn-Form einfuegen

        CredentialFields(
            email = email,
            showPassword = showPassword,
            password = password,
            onEmailChange = {email = it},
            onPasswordChange = {password = it},
            onShowPasswordClick = {showPassword = !showPassword},
        )

        //Forgot-Password-Link
        TextButton(
            onClick = { onForgotPasswordButtonClicked() },
            modifier = Modifier
                .padding(bottom = 64.dp)
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
        onSignUpButtonClicked = {}
    )
}