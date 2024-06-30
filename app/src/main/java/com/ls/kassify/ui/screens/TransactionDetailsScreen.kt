package com.ls.kassify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.amplifyframework.datastore.generated.model.VatType
import com.ls.kassify.R
import com.ls.kassify.data.TransactionModel
import com.ls.kassify.ui.DetailAmount
import com.ls.kassify.ui.DetailItem
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TransactionDetailsScreen(
    modifier: Modifier = Modifier,
    onEditButtonClicked: (String) -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onDeleteConfirmedClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    onCancelDeleteDialogClicked: () -> Unit,
    transaction: Transaction,
    categories: List<Category>,
    vatList: List<VatType>,
    showDeleteDialog: Boolean,
    lastTransaction: Boolean
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Details zur Buchung Nr. ${transaction.transactionNo}:",
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
                content = DateTimeFormatter.ofPattern("dd.MM.yyy").format(transaction.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            )
            DetailAmount(
                label = R.string.amount,
                amount =
                if (transaction.amountPrefix == true)
                    transaction.amount
                else
                    (transaction.amount * -1.00)
            )
            DetailItem(
                label = R.string.category,
                content = categories.find { it.categoryNo == transaction.categoryNo }?.categoryName ?: stringResource(
                    R.string.no_category
                )
            )
            if (transaction.vatNo != null) {
                DetailItem(
                    label = R.string.pre_tax,
                    content = vatList.find { it.vatNo == transaction.vatNo }?.vatType ?: stringResource(
                        R.string.no_tax
                    )
                )
            }
            DetailItem(
                label = R.string.receipt_number,
                content = transaction.receiptNo
            )
            DetailItem(
                label = R.string.text,
                content = transaction.transactionText,
                lastItem = true
            )
        }
        // Only the last transaction can be deleted or edited
        if (lastTransaction) {
            //Edit-Button
            Button(
                onClick = { onEditButtonClicked(transaction.id) },
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
        transaction =
        Transaction.builder()
            .date(
                Temporal.Date(LocalDate.now().toString()))
            .amountPrefix(true)
            .amount(0.00)
            .categoryNo(4400)
            .vatNo(null)
            .build(),
        categories = listOf(
            Category.builder().categoryName("Erlöse 19%").categoryNo(4400).build()
        ),
        vatList = listOf(),
        lastTransaction = true
    )
}