package com.ls.kassify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ls.kassify.R
import com.ls.kassify.data.Transaction
import com.ls.kassify.ui.CashBalanceBox
import com.ls.kassify.ui.CategoryFormField
import com.ls.kassify.ui.DateField
import com.ls.kassify.ui.FormField
import com.ls.kassify.ui.FormSwitch
import com.ls.kassify.ui.UStFormField
import com.ls.kassify.ui.theme.TextDownloadableFontsSnippet2.fontFamily
import java.time.LocalDate

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
    isError: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            DateField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = R.string.date,
                icon = R.drawable.calendar_icon,
                onDateChange = { onDateChange("date", "", it) },
                selectedDate = transaction.date,
                dateOfLastTransaction = dateOfLastTransaction,
                dateOfNextTransaction = dateOfNextTransaction
            )

            FormSwitch(
                label =
                if (transaction.isPositiveAmount)
                    R.string.deposit
                else
                    R.string.payment,
                checked = transaction.isPositiveAmount,
                onCheckedChange = { onChange("prefix", it.toString()) },
                iconChecked = R.drawable.add_icon,
                tintIconChecked = MaterialTheme.colorScheme.background,
                iconUnchecked = R.drawable.remove_icon,
                tintIconUnchecked = MaterialTheme.colorScheme.background,
                checkedThumbColor = colorResource(R.color.green),
                //MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = colorResource(R.color.red),
                //MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
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
            )

            CategoryFormField(
                label = R.string.category,
                defaultLabel = transaction.category,
                onCategoryChange = { onChange("category", it) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            if (!transaction.isPositiveAmount) {
                UStFormField(
                    label = R.string.vat,
                    defaultLabel = "Steuersatz wählen",
                    onUStChange = { onChange("USt", it) },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
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
                    .fillMaxWidth()
            )

            FormField(
                label = R.string.text,
                value = transaction.text,
                onValueChange = { onChange("text", it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            Button(
                onClick = { onSaveButtonClicked(transaction) },
                enabled = !isError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontFamily = fontFamily,
                    fontSize = 18.sp,)
            }

            OutlinedButton(
                onClick = { onCancelButtonClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text =stringResource(R.string.cancel),
                    fontFamily = fontFamily,
                    fontSize = 18.sp,)
            }

            CashBalanceBox(cashBalance = cashBalance)
        }
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
        transaction = Transaction(),
        onChange = { fieldName, value -> },
        onDateChange = { fieldName, value, date -> },
        cashBalance = 0.00,
        amountInput = "",
    )
}
