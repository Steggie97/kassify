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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ls.kassify.R
import com.ls.kassify.ui.DetailItem

@Composable
fun TransactionDetailsScreen(
    modifier: Modifier = Modifier,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
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

        //Edit-Button
        Button(
            onClick = { onEditButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.edit))
        }

        //Delete-Button
        Button(
            onClick = { showDeleteDialog = true },
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
                onConfirmButtonClicked = { onDeleteButtonClicked() },
                onCancelButtonClicked = { showDeleteDialog = false }
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
        onDismissRequest = { /*TODO*/ },
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
        onCancelButtonClicked = {}
    )
}