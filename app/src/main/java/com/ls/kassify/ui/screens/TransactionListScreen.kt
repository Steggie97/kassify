package com.ls.kassify.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ls.kassify.data.transactions
import com.ls.kassify.ui.TransactionCard
import java.text.NumberFormat

@Composable
fun TransactionListScreen(modifier: Modifier = Modifier, contentPadding:PaddingValues) {
    //TODO
    LazyColumn(contentPadding = contentPadding,
        modifier = Modifier
            .padding(
                bottom = 16.dp,
                start = 32.dp,
                end = 32.dp
            )
            .fillMaxHeight()
            .statusBarsPadding()
            .safeDrawingPadding()
    ) {
        items(transactions) {
            TransactionCard(
                transactionId = 0,
                date = it.date,
                amount = (NumberFormat.getCurrencyInstance().format(it.amount)),
                text = it.text
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransactionListScreenPreview() {
    TransactionListScreen(contentPadding = PaddingValues(0.dp))
}