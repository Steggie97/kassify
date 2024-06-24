package com.ls.kassify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ls.kassify.R
import com.ls.kassify.data.Transaction
import com.ls.kassify.ui.DetailAmount
import com.ls.kassify.ui.DetailItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TransactionDetailsScreen(
    modifier: Modifier = Modifier,
    onEditButtonClicked: (Int) -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onDeleteConfirmedClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    onCancelDeleteDialogClicked: () -> Unit,
    transaction: Transaction,
    showDeleteDialog: Boolean
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Details zur Buchung Nr. ${transaction.transId}:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            DetailItem(
                label = R.string.date,
                content = DateTimeFormatter.ofPattern("dd.MM.yyy").format(transaction.date)
            )
            DetailAmount(
                label = R.string.amount,
                amount =
                if(transaction.isPositiveAmount == true)
                    transaction.amount
                else
                    (transaction.amount * -1.00)
            )
            DetailItem(
                label = R.string.category,
                content = transaction.category
            )
            DetailItem(
                label = R.string.receipt_number,
                content = transaction.receiptNo
            )
            DetailItem(
                label = R.string.text,
                content = transaction.text,
                lastItem = true
            )
        }

        //Edit-Button
        Button(
            onClick = { onEditButtonClicked(transaction.transId) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.edit))
        }

        //Delete-Button
        Button(
            onClick = { onDeleteButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.delete))
        }

        //Cancel-Button
        OutlinedButton(
            onClick = { onCancelButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.back))
        }

        if (showDeleteDialog) {
            DeleteDialog(
                onConfirmButtonClicked = { onDeleteConfirmedClicked() },
                onCancelButtonClicked = { onCancelDeleteDialogClicked() }
            )
        }
    }
}

// Delete Dialog to get a confirmation for deleting a transaction:
@Composable
fun DeleteDialog(
    onConfirmButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancelButtonClicked() },
        title = { Text(text = stringResource(R.string.delete_transaction)) },
        text = { Text(text = stringResource(R.string.ask_delete_confirm)) },
        dismissButton = {
            TextButton(onClick = { onCancelButtonClicked() }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmButtonClicked() }) {
                Text(text = stringResource(R.string.delete))
            }
        }
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransactionDetailsScreenPreview() {
    TransactionDetailsScreen(
        onEditButtonClicked = {},
        onDeleteButtonClicked = {},
        onCancelButtonClicked = {},
        onDeleteConfirmedClicked = {},
        onCancelDeleteDialogClicked = {},
        showDeleteDialog = false,
        transaction = Transaction(
            transId = 0,
            date = LocalDate.now(),
            amount = 30.50,
            category = "laufende KFZ-Kosten",
            receiptNo = "Rg-Nr.12342",
            text = "Aral - tanken"
        )
    )
}