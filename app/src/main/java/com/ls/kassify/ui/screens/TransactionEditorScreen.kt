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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.ls.kassify.data.Transaction
import com.ls.kassify.ui.CategoryFormField
import com.ls.kassify.ui.DateField
import com.ls.kassify.ui.FormField
import com.ls.kassify.ui.FormSwitch

@Composable
fun TransactionEditorScreen(
    modifier: Modifier = Modifier,
    onSaveButtonClicked: (Transaction) -> Unit,
    onCancelButtonClicked: () -> Unit,
    transaction: Transaction,
    onDateChange: (String) -> Unit,
    onAmountChange: (Double) -> Unit,
    onCategoryChange: (String) -> Unit,
    onReceiptNoChange: (String) -> Unit,
    onTextChange: (String) -> Unit

) {
    var isDeposit by rememberSaveable { mutableStateOf(true) }
    var category by rememberSaveable { mutableStateOf("Erlose") }
    //TODO: Aktueller Kassenbestand muss in der View sichtbar sein
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Date-Field with Datepicker
        DateField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 8.dp),
            label = R.string.date,
            icon = R.drawable.calendar_icon,
            onDateChange = { onDateChange(it) }
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

        CategoryFormField(
            label = R.string.category,
            defaultLabel = "Wahl",
            onCategoryChange = { newCategory -> category = newCategory },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        FormField(
            label = R.string.amount,
            value = transaction.amount.toString(),
            onValueChange = { onAmountChange(it.toDouble()) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        //Refactoring DropDown-Field:
        FormField(
            label = R.string.category,
            value = transaction.category,
            onValueChange = { onCategoryChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        // TODO: Dropdown-Field für USt-Sachverhalte (keine USt, 19% USt, 7% USt) einfuegen
        /*
        if(!isDeposit){
            val options = List<String>
            val expanded by remember { mutableStateOf( false ) }
            ExposedDropdownMenuBox(expanded = , onExpandedChange = ) {
                
            }
        }

         */

        FormField(
            label = R.string.receipt_number,
            value = transaction.receiptNo,
            onValueChange = { onReceiptNoChange(it) },
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
            value = transaction.text,
            onValueChange = { onTextChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 64.dp)
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
        onCategoryChange = {},
        onAmountChange = {},
        onDateChange = {},
        onTextChange = {},
        onReceiptNoChange = {}
    )
}

