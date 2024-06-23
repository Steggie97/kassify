package com.ls.kassify.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ls.kassify.R
import com.ls.kassify.data.Transaction
import com.ls.kassify.ui.CashBalanceBox
import com.ls.kassify.ui.TransactionCard
import java.time.format.DateTimeFormatter

@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit,
    onTransactionCardClicked: (Int) -> Unit,
    transactions: List<Transaction>,
    cashBalance: Double
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(
                    top = 56.dp,
                    bottom = 16.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            reverseLayout = true
        ) {
            items(transactions) {
                TransactionCard(
                    shape= RectangleShape,
                    onClick = { onTransactionCardClicked(it.transId) },
                    date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(it.date),
                    amount =
                    if(it.isPositiveAmount)
                        it.amount
                    else
                        (it.amount * -1.00),
                    text = it.text,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
        FloatingActionButton(
            onClick = { onAddButtonClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 16.dp)
        ) {
            Image(painter = painterResource(R.drawable.add_icon), contentDescription = null)

        }
        CashBalanceBox(cashBalance = cashBalance)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun TransactionListScreenPreview() {
    TransactionListScreen(
        onAddButtonClicked = {},
        onTransactionCardClicked = {},
        transactions = listOf(),
        cashBalance = 0.00
    )
}