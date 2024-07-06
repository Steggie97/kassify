package com.ls.kassify.ui.screens

//import com.ls.kassify.ui.theme.TextDownloadableFontsSnippet2.fontFamily
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.amplifyframework.datastore.generated.model.VatType
import com.ls.kassify.R
import com.ls.kassify.ui.CashBalanceBox
import com.ls.kassify.ui.CategoryFormField
import com.ls.kassify.ui.DateField
import com.ls.kassify.ui.FormField
import com.ls.kassify.ui.FormSwitch
import com.ls.kassify.ui.VatFormField
import com.ls.kassify.ui.theme.customErrorContainerDark
import com.ls.kassify.ui.theme.customErrorContainerLight
import com.ls.kassify.ui.theme.customErrorDark
import com.ls.kassify.ui.theme.customErrorLight
import com.ls.kassify.ui.theme.successContainerDark
import com.ls.kassify.ui.theme.successContainerLight
import com.ls.kassify.ui.theme.successDark
import com.ls.kassify.ui.theme.successLight
import java.text.NumberFormat
import java.time.LocalDate
import java.time.ZoneId

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TransactionEditorScreen(
    modifier: Modifier = Modifier,
    onSaveButtonClicked: (Transaction) -> Unit,
    onCancelButtonClicked: () -> Unit,
    transaction: Transaction,
    amountInput: String,
    amountErrorMessage: String? = null,
    cashBalance: Double,
    onDateChange: (String, String, LocalDate) -> Unit,
    onChange: (String, String) -> Unit,
    dateOfLastTransaction: LocalDate? = null,
    dateOfNextTransaction: LocalDate = LocalDate.now(),
    categories: List<Category>,
    vatList: List<VatType>,
    isError: Boolean = false,
    formatInput: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = modifier
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            DateField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                //
                label = R.string.date,
                icon = R.drawable.calendar_icon,
                onDateChange = { onDateChange("date", "", it) },
                // selected/ saved date converted in LocalDate
                selectedDate = transaction.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                // given dates for the date-validator of date picker dialog
                dateOfLastTransaction = dateOfLastTransaction,
                dateOfNextTransaction = dateOfNextTransaction
            )

            FormSwitch(
                label =
                if (transaction.amountPrefix)
                    R.string.deposit
                else
                    R.string.payment,
                checked = transaction.amountPrefix,
                onCheckedChange = { onChange("prefix", it.toString()) },
                iconChecked = R.drawable.add_icon,
                tintIconChecked = MaterialTheme.colorScheme.background,
                iconUnchecked = R.drawable.remove_icon,
                tintIconUnchecked = MaterialTheme.colorScheme.background,
                // checked-switch is green, unchecked switch is red
                checkedThumbColor =
                if(isSystemInDarkTheme())
                    successDark
                else
                    successLight,
                checkedTrackColor =
                if(isSystemInDarkTheme())
                    successContainerDark
                else
                    successContainerLight,
                uncheckedThumbColor =
                if (isSystemInDarkTheme())
                    customErrorDark
                else
                    customErrorLight,
                uncheckedTrackColor =
                if(isSystemInDarkTheme())
                    customErrorContainerDark
                else
                    customErrorContainerLight,
            )

            FormField(
                label = R.string.amount,
                value = amountInput,
                onValueChange = { onChange("amount", it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                errorMessage = amountErrorMessage,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if(!focusState.isFocused) {
                            formatInput()
                        }
                    }
            )

            CategoryFormField(
                label = R.string.category,
                defaultLabel =
                categories.find { it.categoryNo == transaction.categoryNo }?.categoryName ?: stringResource(
                    R.string.no_category
                ),
                categories =
                // gets the categories for acquisitions/ non-acquisitions
                    when(transaction.amountPrefix) {
                        // category-Types "Normalkonto" and "Ertragskonto" are shown
                        true -> categories.filter { it.isAcquisition == true }
                        // category-Types "Normalkonto" and "Aufwandskonto" are shown
                        false -> categories.filter { it.isAcquisition == false }
                        // fallback: all categories will be shown
                        else -> categories
                    },
                onCategoryChange = { onChange("category", it) },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
            // vat-Dropdown is only shown if the amountPrefix is false and categoryType is "Aufwandskonto"
            if (!transaction.amountPrefix
                && categories.find { it.categoryNo == transaction.categoryNo }!!.categoryType.name == "Aufwandskonto"
                ){
                VatFormField(
                    label = R.string.vat,
                    defaultLabel =
                    // gets the vatType of the given vatNo of the currentTransaction
                    vatList.find { it.vatNo == transaction.vatNo }?.vatType ?: stringResource(
                        R.string.no_tax
                    ),
                    onVatChange = { onChange("vat", it) },
                    vatTypes = vatList,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
            }

            FormField(
                label = R.string.receipt_number,
                value = transaction.receiptNo,
                onValueChange = { onChange("receiptNo", it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
            )

            FormField(
                label = R.string.text,
                value = transaction.transactionText,
                onValueChange = { onChange("text", it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
            )

            Button(
                onClick = { onSaveButtonClicked(transaction) },
                enabled = !isError,
                modifier = Modifier
                    .fillMaxWidth()
                    //.padding(vertical = 40.dp)
                    .padding(top = 30.dp)
            ) {
                Text(
                    text = stringResource(R.string.save),
                    )
            }

            Button(
                onClick = { onCancelButtonClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text =stringResource(R.string.cancel))
            }
        }
        CashBalanceBox(cashBalance = cashBalance)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransactionEditorScreenPreview() {
    TransactionEditorScreen(
        onSaveButtonClicked = {},
        onCancelButtonClicked = {},
        transaction =
        Transaction.builder()
            .date(Temporal.Date(LocalDate.now().toString()))
            .amountPrefix(true)
            .amount(0.00)
            .accountNo(1600)
            .categoryNo(4400)
            .build(),
        onChange = { fieldName, value -> },
        onDateChange = { fieldName, value, date -> },
        cashBalance = 0.00,
        amountInput = "",
        categories = emptyList(),
        vatList = emptyList(),
        formatInput = {}
    )
}
