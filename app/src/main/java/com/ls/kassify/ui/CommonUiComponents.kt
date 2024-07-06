package com.ls.kassify.ui

//mport com.ls.kassify.ui.theme.CustomColorfulColors
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.amplifyframework.auth.AuthCodeDeliveryDetails
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.amplifyframework.datastore.generated.model.VatType
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.ls.kassify.R
import com.ls.kassify.ui.theme.customErrorDark
import com.ls.kassify.ui.theme.customErrorLight
import com.ls.kassify.ui.theme.getCustomColorfulColors
import com.ls.kassify.ui.theme.successDark
import com.ls.kassify.ui.theme.successLight
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/*
import androidx.compose.ui.viewinterop.AndroidView
import com.ls.kassify.ui.theme.TextDownloadableFontsSnippet2.fontFamily
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
 */



data class Category(val categoryNo: Int, val categoryName: String)
data class Transaction(val categoryNo: Int, val amount: Double, val amountPrefix: Boolean)


//App-UI

//Common Ui-Components: Composable functions with different UI-components which are used in different screens
@Composable
fun KassifyAppBar(
    @StringRes title: Int,
    onCancelButtonClicked: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineMedium,

                    )

                IconButton(onClick = { onCancelButtonClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
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
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
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
                .heightIn(min = 64.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(),

                ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(3 / 12f)
                            .padding(end = 2.dp)
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(3 / 10f)
                            .padding(horizontal = 2.dp)
                    )
                    Text(
                        text = NumberFormat.getCurrencyInstance().format(amount),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.End,
                        color =
                        if (amount < 0.00)
                            if (isSystemInDarkTheme())
                                customErrorDark
                            else
                                customErrorLight
                        else
                            if (isSystemInDarkTheme())
                                successDark
                            else
                                successLight,
                        modifier = Modifier
                            .weight(3 / 12f)
                            .padding(start = 2.dp)
                    )
                }
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
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyLarge
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
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    errorMessage: String? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            label = {
                Text(
                    stringResource(label),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
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
                    style = MaterialTheme.typography.labelMedium,
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
    // variables for clickable textfield. The textfield opens an date picker dialog if the field was pressed
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
        label = {
            Text(
                stringResource(label),
                style = MaterialTheme.typography.bodyLarge
            )
        },
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
                //Future dates/ dates after the date of the next transaction & dates before the last transaction in transactionlist can not be picked:
                    it <= dateOfNextTransaction && it >= dateOfLastTransaction
                else
                //Only the future dates can not be picked
                    it <= dateOfNextTransaction
            },
            onDateChange = { onDateChange(it) },
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.background,
                calendarHeaderTextColor = MaterialTheme.colorScheme.primary,
                dateActiveTextColor = MaterialTheme.colorScheme.background,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                dateInactiveTextColor = MaterialTheme.colorScheme.secondary
            )
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
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp)
    )
    //Content
    Text(
        text = content,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    )
    if (!lastItem) {
        // the last item does not show the horizontal divider
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
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
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp)
    )
    //Content
    Text(
        text = NumberFormat.getCurrencyInstance().format(amount),
        style = MaterialTheme.typography.titleMedium,
        color =
        if (amount < 0.00)
            // the amount is red: checking for darktheme or lighttheme colors
            if (isSystemInDarkTheme())
                customErrorDark
            else
                customErrorLight
        else
        // the amount is green: checking for darktheme or lighttheme colors
            if (isSystemInDarkTheme())
                successDark
            else
                successLight,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSecondaryContainer
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
            text = stringResource(id = label),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(MaterialTheme.colorScheme.background),
        ) {
            TextButton(onClick = { expanded = true }) {
                Text(
                    text = selectedOption,
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            DropdownMenu(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.categoryName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            selectedOption = category.categoryName
                            onCategoryChange(
                                category.categoryName
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
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(id = label),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            TextButton(onClick = { expanded = true }) {
                Text(
                    text = selectedOption,
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            DropdownMenu(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vatTypes.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.vatType,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            selectedOption = option.vatType
                            onVatChange(option.vatType)
                            expanded = false
                        }
                    )
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
            .height(64.dp)
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
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = NumberFormat.getCurrencyInstance().format(cashBalance),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color =
                if (cashBalance < 0.00)
                    if (isSystemInDarkTheme())
                        customErrorDark
                    else
                        customErrorLight
                else
                    if (isSystemInDarkTheme())
                        successDark
                    else
                        successLight,
            )
        }
    }
}

@Composable
fun PieChartView(dataEntries: List<PieEntry>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val typeface: android.graphics.Typeface? =ResourcesCompat.getFont(context, R.font.custom_font)
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                val dataSet = PieDataSet(dataEntries, "Your Label Here")
                dataSet.colors = getCustomColorfulColors().toMutableList()
                dataSet.valueTextSize = 18f
                data = PieData(dataSet)
                typeface?.let { dataSet.valueTypeface = it }
                description.isEnabled = false
                isDrawHoleEnabled = false
                legend.isEnabled = false
                animateY(1400)
            }
        },
        update = { pieChart ->
            val dataSet = PieDataSet(dataEntries, "Your Label Here")
            dataSet.colors = getCustomColorfulColors().toMutableList()
            typeface?.let { dataSet.valueTypeface = it }
            dataSet.valueTextSize = 18f
            dataSet.valueTextColor = androidx.compose.ui.graphics.Color.White.toArgb() // Set your desired color here
            pieChart.data = PieData(dataSet)
            pieChart.invalidate()
        }
    )
}

fun ChartCategory(categories: List<Category>, transactions: List<Transaction>): HashMap<String, Double> {
    val categoryAmountMap = HashMap<String, Double>()

    transactions.forEach { transaction ->
            val categoryName =
                categories.find { it.categoryNo == transaction.categoryNo }?.categoryName
                    ?: "No Category"

            val amount = if (transaction.amountPrefix) transaction.amount else -transaction.amount

            categoryAmountMap[categoryName] =
                categoryAmountMap.getOrDefault(categoryName, 0.0) + amount

    }

    return categoryAmountMap
}