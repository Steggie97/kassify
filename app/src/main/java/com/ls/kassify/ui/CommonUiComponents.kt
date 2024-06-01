package com.ls.kassify.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ls.kassify.R
import com.ls.kassify.ui.screens.TransactionDetailsScreen
import com.ls.kassify.ui.screens.TransactionFormScreen
import com.ls.kassify.ui.screens.TransactionListScreen

//App-UI
@Composable
fun ScaffoldWithTopBar(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            KassifyAppBar(
                title = stringResource(R.string.app_name)
            )
        }
    ) { innerPadding ->

        TransactionListScreen(contentPadding = innerPadding)

    }
}

//Common Ui-Components
@Composable
fun KassifyAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {/*TODO*/ }
    )

}

@Composable
fun TransactionCard(
    transactionId: Int,
    date: String,
    amount: String,
    text: String
) {
    TextButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(2 / 10f)
                        .padding(end = 4.dp)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(3 / 8f)
                        .padding(horizontal = 4.dp)
                )
                Text(
                    text = amount,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(3 / 12f)
                        .padding(start = 4.dp)
                )
            }
        }
    }

}

@Composable
fun FormSwitch(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    @DrawableRes iconChecked: Int,
    tintIconChecked: Color,
    @DrawableRes iconUnchecked: Int,
    tintIconUnchecked: Color,
    checkedThumbColor: Color,
    checkedTrackColor: Color,
    uncheckedThumbColor: Color,
    uncheckedTrackColor: Color,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            thumbContent = {
                if (checked) {
                    Icon(
                        painter = painterResource(iconChecked),
                        contentDescription = null,
                        tint = tintIconChecked
                    )
                } else {
                    Icon(
                        painter = painterResource(iconUnchecked),
                        contentDescription = null,
                        tint = tintIconUnchecked
                    )
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = checkedThumbColor,
                checkedTrackColor = checkedTrackColor,
                uncheckedThumbColor = uncheckedThumbColor,
                uncheckedTrackColor = uncheckedTrackColor
            ),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Composable
fun FormField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    TextField(
        label = { Text(stringResource(label)) },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    onButtonIconClicked: () -> Unit,
    showPassword: Boolean = false,
    keyboardOptions: KeyboardOptions
) {
    TextField(
        label = { Text(stringResource(label)) },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        visualTransformation =
        if (showPassword)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onButtonIconClicked) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun FormCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun DetailItem(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    content: String,
    lastItem: Boolean = false,
) {
    //Label
    Text(
        text = stringResource(label),
        fontSize = 14.sp,
        fontWeight = FontWeight.Thin,
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
    )
    //Content
    Text(
        text = content,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 8.dp)
    )
    if (!lastItem) {
        HorizontalDivider()
    }
}

@Composable
fun CredentialFields(
    modifier: Modifier = Modifier,
    email: String,
    showPassword: Boolean,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onShowPasswordClick: () -> Unit,
) {
    FormField(
        label = R.string.e_mail,
        value = email,
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)

    )

    PasswordField(
        icon =
        if (showPassword)
            R.drawable.visibility_icon
        else
            R.drawable.visibility_off_icon,
        onButtonIconClicked = onShowPasswordClick,
        label = R.string.password,
        value = password,
        onValueChange = onPasswordChange,
        showPassword = showPassword,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

//Previews
@Preview(
    showBackground = true
)
@Composable
fun TransactionCardPreview() {
    TransactionCard(
        transactionId = 1,
        date = "28.05.2024",
        amount = "- 30,00 €",
        text = "Aral"
    )
}


