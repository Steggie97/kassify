package com.ls.kassify.ui.screens


//import com.github.mikephil.charting.data.PieEntry
//import com.ls.kassify.ui.ReusablePieChart
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.ls.kassify.R
import com.ls.kassify.ui.CashBalanceBox
import com.ls.kassify.ui.TransactionCard
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Screen with Lazycolumn: The lazy column contains clickable transaction-cards
@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit,
    onTransactionCardClicked: (String) -> Unit,
    transactions: List<Transaction>,
    cashBalance: Double,
    transaction: Transaction,
    categories: List<Category>,
) {
    //val percent=cashBalance/
    /*val categoried = categories.find { it.categoryNo == transaction.categoryNo }?.categoryName ?: stringResource(
        R.string.no_category
    )*/
    /*
    val entries = ArrayList<PieEntry>()
    entries.add(PieEntry(40f, "Party A"))
    entries.add(PieEntry(30f, "Party B"))
    entries.add(PieEntry(30f, "Party C"))

     */
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .padding(
                    top = 64.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            // the lastTransaction is shown on the top of the list
            reverseLayout = true
        ) {
            items(transactions) {
                TransactionCard(
                    shape= RectangleShape,
                    onClick = { onTransactionCardClicked(it.id) },
                    date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(it.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                    amount =
                    if(it.amountPrefix)
                        it.amount
                    else
                        (it.amount * -1.00),
                    text = it.transactionText,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
        /*ReusablePieChart(
            dataEntries = entries,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )*/
        // Add button for creating new transactions
        FloatingActionButton(
            onClick = { onAddButtonClicked() },
            containerColor =  MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 16.dp)
        ) {
            Image(painter = painterResource(R.drawable.add_icon), contentDescription = null,
                colorFilter =
                androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.background) )

        }

        // shows the current cashBalance in list
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
        cashBalance = 0.00,
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
    )
}
