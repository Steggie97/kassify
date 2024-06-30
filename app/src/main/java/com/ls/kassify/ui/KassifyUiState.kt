package com.ls.kassify.ui

import androidx.compose.ui.res.stringResource
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.Category
import com.amplifyframework.datastore.generated.model.Transaction
import com.amplifyframework.datastore.generated.model.VatType
import com.ls.kassify.R
import com.ls.kassify.data.TransactionModel
import java.time.LocalDate

data class KassifyUiState(
    val cashBalance: Double = 0.00,
    val transactions: MutableList<Transaction> = mutableListOf(),
    val categoryList: List<Category> = listOf(),
    val vatList: List<VatType> = listOf(),
    val currentTransaction: Transaction = Transaction.builder().date(Temporal.Date(LocalDate.now().toString())).amountPrefix(true).amount(0.00).accountNo(1600).categoryNo(9999).transactionNo(transactions.size + 1).transactionText("").receiptNo("").build(),
    val amountInput: String = "",
    val nextCashBalance: Double = 0.00
)