package com.ls.kassify.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.amplifyframework.auth.AuthCodeDeliveryDetails
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.VatType
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.ls.kassify.R
import com.ls.kassify.ui.theme.TextDownloadableFontsSnippet2.fontFamily
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

//App-UI

//Common Ui-Components
@Composable
fun KassifyAppBar(
    @StringRes title: Int,
    onCancelButtonClicked: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = fontFamily,
                    fontSize = 25.sp
                )

                IconButton(onClick = { onCancelButtonClicked() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }

            }
        }
    )
}

// Composable Functions for AWS Amplify-Authenticator
@Composable
fun AuthHeaderContent(
    @StringRes title: Int
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        )
        {
            Text(
                text = stringResource(title),
                fontFamily = fontFamily,
                fontSize=30.sp,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun DetailsNoticeContent(details: AuthCodeDeliveryDetails) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        )
        {
            Text(
                text = stringResource(
                    R.string.amplify_ui_authenticator_confirmation_code_sent,
                    details.destination
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

        }
    }
}

//Composable-Functions App Content:

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    date: String,
    amount: Double,
    text: String,
    shape: androidx.compose.ui.graphics.Shape,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {
    TextButton(
        onClick = { onClick() },
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        shape = shape
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = date,
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(3 / 12f)
                        .padding(end = 4.dp)
                )
                Text(
                    text = text,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(3 / 10f)
                        .padding(horizontal = 4.dp)
                )
                Text(
                    text = NumberFormat.getCurrencyInstance().format(amount),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    color =
                    if (amount < 0.00)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.surface,
                        //colorResource(R.color.green),
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
        //.padding(start = 16.dp, bottom = 8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label),
            fontSize = 18.sp, fontFamily= fontFamily
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
                checkedBorderColor = Black,
                checkedThumbColor = checkedThumbColor,
                checkedTrackColor = Color(0xFFB7D99B),
                uncheckedThumbColor = uncheckedThumbColor,
                uncheckedTrackColor = Color(0xFFF5A19F)
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
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            label = { Text(stringResource(label),fontSize = 18.sp, fontFamily= fontFamily) },
            value = value,
            isError = errorMessage != null,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
        )
        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Top) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }


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
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions
) {
    Column(modifier = modifier) {
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
            isError = errorMessage != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
        )
        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Top) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }

}

@Composable
fun DateField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    @DrawableRes icon: Int,
    selectedDate: LocalDate = LocalDate.now(),
    onDateChange: (LocalDate) -> Unit,
    dateOfLastTransaction: LocalDate? = null,
    dateOfNextTransaction: LocalDate = LocalDate.now()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val dateDialogState = rememberMaterialDialogState()

    if (interactionSource.collectIsPressedAsState().value) {
        dateDialogState.show()
    }

    //Clickable Text-Field:
    // UI-Composables
    OutlinedTextField(
        modifier = modifier,
        value = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(selectedDate),
        onValueChange = {},
        label = { Text(stringResource(label), fontSize = 18.sp, fontFamily= fontFamily) },
        trailingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null
            )
        },
        interactionSource = interactionSource,
        readOnly = true
    )

    // Datepicker-Dialog with Date-Validator
    MaterialDialog(
        dialogState = dateDialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            positiveButton(
                text = stringResource(R.string.confirm), textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.primary
                )
            ) {

            }
            negativeButton(
                text = stringResource(R.string.cancel),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            )
        }
    ) {
        datepicker(
            initialDate = selectedDate,
            title = "Datum wählen",
            allowedDateValidator = {
                if (dateOfLastTransaction != null)
                //Future dates/ dates after the date of the next transaction & dates before the last transactions can not be picked:
                    it <= dateOfNextTransaction && it >= dateOfLastTransaction
                else
                //Only the future dates can not be picked
                    it <= dateOfNextTransaction
            },
            onDateChange = { onDateChange(it) },
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.surface,
                headerTextColor = MaterialTheme.colorScheme.onBackground,
                calendarHeaderTextColor = MaterialTheme.colorScheme.secondary,
                dateActiveTextColor = MaterialTheme.colorScheme.secondary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.surface,
                dateInactiveTextColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
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
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Thin,
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
    )
    //Content
    Text(
        text = content,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 8.dp)
    )
    if (!lastItem) {
        HorizontalDivider()
    }
}

@Composable
fun DetailAmount(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    amount: Double,
) {
    //Label
    Text(
        text = stringResource(label),
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Thin,
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
    )
    //Content
    Text(
        text = NumberFormat.getCurrencyInstance().format(amount),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color =
        if (amount < 0.00)
            colorResource(R.color.red)
        else
            MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun CredentialFields(
    modifier: Modifier = Modifier,
    email: String,
    emailErrorMessage: String? = null,
    passwordErrorMessage: String? = null,
    showPassword: Boolean,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onShowPasswordClick: () -> Unit,
    isLastField: Boolean = false
) {
    FormField(
        label = R.string.e_mail,
        value = email,
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        errorMessage = emailErrorMessage,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
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
        errorMessage = passwordErrorMessage,
        showPassword = showPassword,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction =
            if (isLastField)
                ImeAction.Done
            else
                ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun CategoryFormField(
    @StringRes label: Int,
    defaultLabel: String,
    onCategoryChange: (String) -> Unit,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultLabel) }

    LaunchedEffect(defaultLabel) {
        selectedOption = defaultLabel
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            fontSize = 18.sp, fontFamily= fontFamily,
            text = stringResource(id = label),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Box(modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
            //.background(MaterialTheme.colorScheme.secondary),
            ) {
            TextButton(onClick = { expanded = true }) {
                Text(
                    text = selectedOption,fontSize = 18.sp, fontFamily= fontFamily
                )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null, tint = Black)
            }
            DropdownMenu(
                modifier = Modifier.background(Color(0xFFF7FBF5)),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                //options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.categoryName,
                                //text = option,
                                fontSize = 15.sp,
                                fontFamily = fontFamily
                            )
                        },
                        onClick = {
                            selectedOption = category.categoryName
                                //option
                            onCategoryChange(
                                category.categoryName
                                //option
                                )
                            expanded = false
                        })
                }
            }
        }
    }
}

@Composable
fun VatFormField(
    @StringRes label: Int,
    defaultLabel: String,
    onVatChange: (String) -> Unit,
    vatTypes: List<VatType>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultLabel) }

    LaunchedEffect(defaultLabel) {
        selectedOption = defaultLabel
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            fontSize = 18.sp, fontFamily= fontFamily,
            text = stringResource(id = label),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Box(modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
            //.background(MaterialTheme.colorScheme.secondary),
        )  {
            TextButton(onClick = { expanded = true }) {
                Text(
                    text = selectedOption,
                    //fontSize = 20.sp
                )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null, tint = Black)
            }
            DropdownMenu(
                modifier = Modifier.background(Color(0xFFF7FBF5)),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vatTypes.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.vatType,
                            )
                        },
                        onClick = {
                            selectedOption = option.vatType
                            onVatChange(option.vatType)
                            expanded = false
                        })
                }
            }
        }
    }
}

@Composable
fun CashBalanceBox(cashBalance: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = MaterialTheme.colorScheme.background),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                text =
                "Kassenbestand am ${
                    DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
                        .format(LocalDate.now())
                }",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily,
                fontSize = 18.sp
            )
            Text(
                text = NumberFormat.getCurrencyInstance().format(cashBalance),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
                fontSize = 18.sp,
                color =
                if (cashBalance < 0.00)
                    MaterialTheme.colorScheme.error
                else
                    colorResource(R.color.green)
            )
        }
    }
}

@Composable
fun ReusablePieChart(dataEntries: List<PieEntry>, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                val dataSet = PieDataSet(dataEntries, "Your Label Here")
                dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
                data = PieData(dataSet)
                description.isEnabled = false
                isDrawHoleEnabled = false
                legend.isEnabled = false
                animateY(1400)
            }
        },
        update = { pieChart ->
            pieChart.data.notifyDataChanged()
            pieChart.notifyDataSetChanged()
            pieChart.invalidate()
        }
    )
}
