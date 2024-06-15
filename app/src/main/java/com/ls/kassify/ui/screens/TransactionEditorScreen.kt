package com.ls.kassify.ui.screens

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ls.kassify.R
import com.ls.kassify.data.Transaction
import com.ls.kassify.ui.CashBalanceBox
import com.ls.kassify.ui.CategoryFormField
import com.ls.kassify.ui.DateField
import com.ls.kassify.ui.FormField
import com.ls.kassify.ui.FormSwitch
import java.time.LocalDate

@Composable
fun TransactionEditorScreen(
    modifier: Modifier = Modifier,
    onSaveButtonClicked: (Transaction) -> Unit,
    onCancelButtonClicked: () -> Unit,
    transaction: Transaction,
    amountInput: String,
    cashBalance: Double,
    onDateChange: (String, String, LocalDate) -> Unit,
    onChange: (String,String ) -> Unit,
    dateOfLastTransaction: LocalDate? = null,
    dateOfNextTransaction: LocalDate = LocalDate.now()
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            //Date-Field with Datepicker
            DateField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp),
                label = R.string.date,
                icon = R.drawable.calendar_icon,
                onDateChange = { onDateChange("date", "", it) } ,
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
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )

            CategoryFormField(
                label = R.string.category,
                defaultLabel = transaction.category,
                onCategoryChange = { onChange("category", it) },
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
            )

            if(!transaction.isPositiveAmount) {
                // TODO: Dropdown-Field für USt-Sachverhalte (keine USt, 19% USt, 7% USt) einfuegen
                CategoryFormField(
                    label = R.string.vat,
                    defaultLabel = "Steuersatz wählen",
                    onCategoryChange = {  },
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 8.dp)
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
                    .padding(bottom = 24.dp)
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
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            //Save-Button
            Button(
                onClick = { onSaveButtonClicked(transaction) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(text = stringResource(R.string.save))
            }

            //Cancel-Button
            OutlinedButton(
                onClick = { onCancelButtonClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.cancel))
            }
        }

        //Cash Balance Box
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
        transaction = Transaction(),
        onChange = { fieldName, value -> },
        onDateChange = { fieldName, value, date -> },
        cashBalance = 0.00,
        amountInput = "",
    )
}


