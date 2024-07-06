package com.ls.kassify.ui.screens


import android.annotation.SuppressLint
import android.widget.ImageButton
import com.github.mikephil.charting.data.PieEntry
//import com.ls.kassify.ui.ReusablePieChart
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
//import com.github.mikephil.charting.data.PieEntry
import com.ls.kassify.R
import com.ls.kassify.ui.CashBalanceBox
import com.ls.kassify.ui.ChartCategory
import com.ls.kassify.ui.KassifyUiState
import com.ls.kassify.ui.PieChartView
import com.ls.kassify.ui.TransactionCard
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.exp

// Screen with Lazycolumn: The lazy column contains clickable transaction-cards
@SuppressLint("StateFlowValueCalledInComposition")
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
    var expanded by remember { mutableStateOf(false) }
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
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(){ Text(
                        text = if (expanded) "verstecken" else "mehr info",
                        modifier = Modifier.clickable { expanded = !expanded },
                        style = MaterialTheme.typography.titleLarge
                    )
                        FloatingActionButton(
                            onClick = { expanded = !expanded },
                            containerColor =  MaterialTheme.colorScheme.onPrimary,
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                            modifier = Modifier
                                .size(30.dp)
                                .padding(start = 4.dp),
                        ) {
                            if (expanded) {
                                Image(painter = painterResource(R.drawable.keyboard_double_arrow_up_24px), contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.size(30.dp)
                                )
                            }else{
                                Image(painter = painterResource(R.drawable.keyboard_double_arrow_down_24px), contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                        }
                    }
                }
            }
            item{
                if (expanded) {
                    val categoryAmountMap = ChartCategory(categories, transactions)
                    val dataEntries = categoryAmountMap.map { (categoryName, amount) ->
                        PieEntry(amount.toFloat(), categoryName)
                    }
                    PieChartView(
                        dataEntries = dataEntries,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .align(Alignment.TopCenter)
                    )
                }
            }
        }
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
