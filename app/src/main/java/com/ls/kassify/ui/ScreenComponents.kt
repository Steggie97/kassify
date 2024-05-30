package com.ls.kassify.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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


//App-UI

@Composable
fun KassifyApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            KassifyAppBar(
                title = stringResource(R.string.app_name)
            )
        }
    ) { innerPadding ->
        TransactionFormScreen(modifier = Modifier.padding(innerPadding))

    }
}

//Screens:
@Composable
fun TransactionFormScreen(modifier: Modifier = Modifier) {
    var isDeposit by remember { mutableStateOf(true) }
    //TODO: Aktueller Kassenbestand muss in der View sichtbar sein
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 32.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: Datepicker einfuegen
        FormField(
            label = R.string.date,
            value = "01.01.2024",
            onValueChange = { /*TODO*/ },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .fillMaxWidth()
        )

        FormSwitch(
            label =
            if (isDeposit)
                R.string.deposit
            else
                R.string.payment,
            checked = isDeposit,
            onCheckedChange = { isDeposit = it },
            iconChecked = R.drawable.add_icon,
            tintIconChecked = MaterialTheme.colorScheme.background,
            iconUnchecked = R.drawable.remove_icon,
            tintIconUnchecked = MaterialTheme.colorScheme.background,
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
        )

        FormField(
            label = R.string.amount,
            value = "0.00",
            onValueChange = { /*TODO*/ },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        // TODO: Dropdown-Field für USt-Sachverhalte (keine USt, 19% USt, 7% USt) einfuegen

        FormField(
            label = R.string.category,
            value = "Erlöse",
            onValueChange = { /*ToDo*/ },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        FormField(
            label = R.string.receipt_number,
            value = "RG-Nr. 12345",
            onValueChange = { /*ToDo*/ },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        FormField(
            label = R.string.text,
            value = "Beispieltext",
            onValueChange = { /*ToDo*/ },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 64.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = {/* ToDo */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = stringResource(R.string.save))
        }

        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.cancel))
        }
    }
}

@Composable
fun TransactionDetailsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 32.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //TODO: Title Transaktiondetails in TopAppBar setzen
        Text(
            text = "Einzahlung Nr. 1 vom 01.01.2024",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 32.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 64.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            DetailItem(
                label = R.string.date,
                content = "01.01.2024"
            )
            DetailItem(
                label = R.string.amount,
                content = "+ 30,00 €"
            )
            DetailItem(
                label = R.string.category,
                content = "Erlöse"
            )
            DetailItem(
                label = R.string.receipt_number,
                content = "RG-Nr. 1234"
            )
            DetailItem(
                label = R.string.text,
                content = "Barverkauf von verschiedenen Artikeln, die keine Umsatzsteuer enthalten",
                lastItem = true
            )
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.edit))
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.delete))
        }

        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.back))
        }
    }
}

@Composable
fun TransactionListScreen(modifier: Modifier = Modifier) {
    //TODO
    Text("TransactionList under Construction")
    LazyColumn() {
        //items(transactionList)

    }
}

@Composable
fun LogInScreen(modifier: Modifier = Modifier) {
    //Todo: Viewmodel
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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

        FormField(
            label = R.string.e_mail,
            value = email,
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp)
        )

        PasswordField(
            icon =
            if (showPassword)
                R.drawable.visibility_icon
            else
                R.drawable.visibility_off_icon,
            onButtonIconClicked = { showPassword = !showPassword },
            label = R.string.password,
            value = password,
            onValueChange = { password = it },
            showPassword = showPassword,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()

        )

        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(bottom = 64.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 14.sp
            )
        }


        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = stringResource(R.string.login))
        }

        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.register))
        }
    }
}

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

        FormField(
            label = R.string.e_mail,
            value = email,
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp)
        )

        PasswordField(
            icon =
            if (showPassword)
                R.drawable.visibility_icon
            else
                R.drawable.visibility_off_icon,
            onButtonIconClicked = { showPassword = !showPassword },
            label = R.string.password,
            value = password,
            onValueChange = { password = it },
            showPassword = showPassword,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
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
                .padding(bottom = 64.dp)
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

//Ui-Components

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
        onClick = { /*TODO*/ }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ) {
                Text(
                    text = date,
                    modifier = Modifier
                        .weight(1/4f)
                        .padding(horizontal = 4.dp)
                )
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(2/4f)
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = amount,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1/4f)
                        .padding(horizontal = 4.dp)
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
                        //modifier = Modifier.size(SwitchDefaults.IconSize)
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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun KassifyAppPreview() {
    KassifyApp()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransactionFormScreenPreview() {
    TransactionFormScreen()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransactionDetailsScreenPreview() {
    TransactionDetailsScreen()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransactionListScreenPreview() {
    TransactionListScreen()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LogInScreenPreview() {
    LogInScreen()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}