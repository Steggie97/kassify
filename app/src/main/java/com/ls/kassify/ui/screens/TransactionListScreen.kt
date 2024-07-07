package com.ls.kassify.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.github.mikephil.charting.data.PieEntry
import com.ls.kassify.R
import com.ls.kassify.ui.CashBalanceBox
import com.ls.kassify.ui.PieChartView
import com.ls.kassify.ui.TransactionCard
import com.ls.kassify.ui.chartCategory
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Screen with Lazycolumn: The lazy column contains clickable transaction-cards
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit,
    onTransactionCardClicked: (String) -> Unit,
    transactions: List<Transaction>,
    cashBalance: Double,
    categories: List<Category>,
) {
    var expanded by remember { mutableStateOf(false) }
    val categoryAmountMap = chartCategory(categories, transactions)
    val dataEntries = categoryAmountMap.map { (categoryName, amount) ->
        PieEntry(amount.toFloat(), categoryName)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                // the lastTransaction is shown on the top of the list
                reverseLayout = true,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(transactions) {
                    TransactionCard(
                        shape = RectangleShape,
                        onClick = { onTransactionCardClicked(it.id) },
                        date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(
                            it.date.toDate().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        ),
                        amount =
                        if (it.amountPrefix)
                            it.amount
                        else
                            (it.amount * -1.00),
                        text = it.transactionText,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }
                item { Column(
                    modifier = Modifier
                        .padding(
                            top = 64.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row() {
                            Text(
                                text =
                                if (expanded)
                                    stringResource(R.string.pie_chart_hide_text)
                                else
                                    stringResource(R.string.pie_chart_show_text),
                                modifier = Modifier.clickable { expanded = !expanded },
                                style = MaterialTheme.typography.titleMedium
                            )
                            FloatingActionButton(
                                onClick = { expanded = !expanded },
                                containerColor = MaterialTheme.colorScheme.background,
                                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 4.dp),
                            ) {
                                if (expanded) {
                                    Image(
                                        painter = painterResource(R.drawable.keyboard_double_arrow_up_24px),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.size(30.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(R.drawable.keyboard_double_arrow_down_24px),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                    if (expanded) {
                        Text(
                            text = stringResource(R.string.piechart_title_text),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        PieChartView(
                            dataEntries = dataEntries,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                        )
                    }
                }
            }
        }
        // Add button for creating new transactions
        FloatingActionButton(
            onClick = { onAddButtonClicked() },
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.add_icon), contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
            )
        }
        // shows the current cashBalance in list
        CashBalanceBox(cashBalance = cashBalance)
    }
}

//Preview
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
        categories = listOf(
            Category.builder().categoryName("Erlöse 19%").categoryNo(4400).build()
        ),
    )
}
