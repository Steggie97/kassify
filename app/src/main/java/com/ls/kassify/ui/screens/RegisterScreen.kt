package com.ls.kassify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ls.kassify.R
import com.ls.kassify.ui.CredentialFields
import com.ls.kassify.ui.PasswordField

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showPasswordConfirm by rememberSaveable { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passwordCheck by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 32.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //TODO: Logo über LogIn-Form einfuegen
        // Image()

        CredentialFields(
            email = email,
            showPassword = showPassword,
            password = password,
            onEmailChange = {email = it},
            onPasswordChange = {password = it},
            onShowPasswordClick = {showPassword = !showPassword}
        )

        PasswordField(
            icon =
            if (showPasswordConfirm)
                R.drawable.visibility_icon
            else
                R.drawable.visibility_off_icon,
            onButtonIconClicked = { showPasswordConfirm = !showPasswordConfirm },
            label = R.string.confirm_password,
            value = passwordCheck,
            onValueChange = { passwordCheck = it },
            showPassword = showPasswordConfirm,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 64.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = stringResource(R.string.register))
        }

        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.cancel))
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}