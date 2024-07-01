package com.ls.kassify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
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
import com.ls.kassify.ui.theme.TextDownloadableFontsSnippet2.fontFamily
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
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            modifier = Modifier
                .padding(bottom = 8.dp,top=15.dp)

        )
        Card(
            colors= CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 32.dp)
                .clip(RoundedCornerShape(20.dp)),
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
            Button(
                shape = RectangleShape,
                onClick = { onEditButtonClicked(transaction.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 80.dp)
                    .clip(RoundedCornerShape(10.dp)),
            ) {
                Text(stringResource(R.string.edit),
                    fontSize = 40.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top =40.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Delete Button
                FloatingActionButton(
                    onClick = { onDeleteButtonClicked() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(70.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.delete_24px),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.size(60.dp)
                    )
                }

                FloatingActionButton(
                    onClick = { onCancelButtonClicked() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(70.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.home_24px),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.size(60.dp)
                    )
                }

                /*// Cancel Button
                Button(
                    onClick = { onCancelButtonClicked() },
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f)
                        .padding(start = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(text = stringResource(R.string.back), fontSize = 30.sp)
                }*/
            }
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
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)),
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.outlineVariant,
        textContentColor =  MaterialTheme.colorScheme.outlineVariant,
        onDismissRequest = { onCancelButtonClicked() },
        title = { Text(text = stringResource(R.string.delete_transaction),fontFamily = fontFamily,
            fontSize = 30.sp)},
        text = { Text(text = stringResource(R.string.ask_delete_confirm),fontFamily = fontFamily,
            fontSize = 20.sp) },
        dismissButton = {
            TextButton(onClick = { onCancelButtonClicked() }) {
                Text(text = stringResource(R.string.cancel),color = Color.White,fontFamily = fontFamily,
                    fontSize = 20.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmButtonClicked() }) {
                Text(text = stringResource(R.string.delete),color = Color.White,fontFamily = fontFamily,
                    fontSize = 20.sp)
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
            .accountNo(1600)
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