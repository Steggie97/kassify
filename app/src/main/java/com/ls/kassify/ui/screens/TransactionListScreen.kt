package com.ls.kassify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ls.kassify.R
import com.ls.kassify.data.transactions
import com.ls.kassify.ui.TransactionCard
import java.text.NumberFormat

@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit,
    onTransactionCardClicked: () -> Unit
) {
    //TODO: Kassenbestand anzeigen
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "Kassenbestand: 50,00 €",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }


            LazyColumn(
                modifier = Modifier
                    .padding(
                        //top = 80.dp,
                        bottom = 16.dp,
                        start = 24.dp,
                        end = 24.dp
                    )
                    .statusBarsPadding()
                    .safeDrawingPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                reverseLayout = true
            ) {
                items(transactions) {
                    TransactionCard(
                        onClick = { onTransactionCardClicked() },
                        date = it.date,
                        amount = (NumberFormat.getCurrencyInstance().format(it.amount)),
                        text = it.text,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }
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
        onTransactionCardClicked = {}
    )
}